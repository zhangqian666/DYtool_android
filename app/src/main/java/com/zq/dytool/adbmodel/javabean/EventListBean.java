package com.zq.dytool.adbmodel.javabean;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/6 3:43 PM
 * @Package com.zq.dytool.adbmodel.javabean
 **/
public class EventListBean {

    /**
     * id : 1
     * title : 发送弹幕信息
     * body : [{"type":0,"text":"发送内容","delay_time":1000,"click_potion":{"x":100,"y":100},"swipe":{"x1":1,"y1":1,"x2":1,"y2":1},"key_event_code":1}]
     */

    private int id;
    private String title;
    private boolean refresh;
    private int refresh_times;
    private List<EventBean> body;
    private boolean shut_down;

    public boolean isShut_down() {
        return shut_down;
    }

    public void setShut_down(boolean shut_down) {
        this.shut_down = shut_down;
    }

    public int getRefresh_times() {
        return refresh_times;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void setRefresh_times(int refresh_times) {
        this.refresh_times = refresh_times;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<EventBean> getBody() {
        return body;
    }

    public void setBody(List<EventBean> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", refresh=" + refresh +
                ", refresh_times=" + refresh_times +
                ", body=" + body +
                '}';
    }

}
