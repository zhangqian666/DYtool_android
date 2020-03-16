package com.zq.dytool.adbmodel.wss;

import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author 张迁-zhangqian
 * @Data 2019-10-02 08:50
 * @Package com.wuzi.qa.wss
 **/
public class WssServer {
    private WebSocket currentWebSocket;
    private OnMsgListener onMsgListener;
    //创建基本线程池
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 1, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(50));

    private AsyncHttpClient.WebSocketConnectCallback connectCallBack = new AsyncHttpClient.WebSocketConnectCallback() {
        @Override
        public void onCompleted(Exception ex, final WebSocket webSocket) {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            if (onMsgListener != null) {
                onMsgListener.onMessage("链接上了服务器");
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            sendMsg("发送心跳消息");
                            try {
                                Thread.sleep(30000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                onMsgListener.onConnect(true);
            }


            WssServer.this.currentWebSocket = webSocket;
            webSocket.setStringCallback(new WebSocket.StringCallback() {
                public void onStringAvailable(String s) {
                    if (onMsgListener != null)
                        onMsgListener.onMessage(s);
                }
            });

            webSocket.setClosedCallback(new CompletedCallback() {
                @Override
                public void onCompleted(Exception ex) {
                    threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                AsyncHttpClient.getDefaultInstance().websocket(base_url, "my-protocol", connectCallBack);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (onMsgListener != null) {
                        onMsgListener.onConnect(false);
                    }
                }
            });
        }
    };
    private String base_url;

    public void init(String url) {
        base_url = url;
        AsyncHttpClient.getDefaultInstance().websocket(base_url, "my-protocol", connectCallBack);
    }

    public interface OnMsgListener {
        void onMessage(String str);

        void onConnect(boolean isConnect);
    }

    public void setOnMsgListener(OnMsgListener onMsgListener) {
        this.onMsgListener = onMsgListener;
    }

    public boolean sendMsg(String msg) {
        if (currentWebSocket != null) {
            currentWebSocket.send(msg);
            return true;
        } else
            return false;
    }
}
