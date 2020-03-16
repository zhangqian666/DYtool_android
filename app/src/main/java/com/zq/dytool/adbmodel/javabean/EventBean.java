package com.zq.dytool.adbmodel.javabean;

/**
 * @Author 张迁-zhangqian
 * @Data 2020/3/6 3:43 PM
 * @Package com.zq.dytool.adbmodel.javabean
 **/
public class EventBean {
    /**
     * type : 0
     * text : 发送内容
     * delay_time : 1000
     * click_potion : {"x":100,"y":100}
     * swipe : {"x1":1,"y1":1,"x2":1,"y2":1}
     * key_event_code : 1
     */

    private int type; // 0: 打开app； 1：延迟时间； 2：点击； 3：滑动； 4：系统事件 5:发送text 6:发送中文 7：发送B64 8:copy 9:回到本体app；
    private String text; // type 0 情况下，发送文字的具体内容
    private long delay_time;
    private ClickPotionBean click_potion;
    private SwipeBean swipe;
    private int key_event_code;
    private String app_package;
    private String user_url;

    public String getApp_package() {
        return app_package;
    }

    public void setApp_package(String app_package) {
        this.app_package = app_package;
    }

    public String getUser_url() {
        return user_url;
    }

    public void setUser_url(String user_url) {
        this.user_url = user_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(long delay_time) {
        this.delay_time = delay_time;
    }

    public ClickPotionBean getClick_potion() {
        return click_potion;
    }

    public void setClick_potion(ClickPotionBean click_potion) {
        this.click_potion = click_potion;
    }

    public SwipeBean getSwipe() {
        return swipe;
    }

    public void setSwipe(SwipeBean swipe) {
        this.swipe = swipe;
    }

    public int getKey_event_code() {
        return key_event_code;
    }

    public void setKey_event_code(int key_event_code) {
        this.key_event_code = key_event_code;
    }

    public static class ClickPotionBean {
        /**
         * x : 100.0
         * y : 100.0
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

    public static class SwipeBean {
        /**
         * x1 : 1.0
         * y1 : 1.0
         * x2 : 1.0
         * y2 : 1.0
         * time : 100
         */

        private double x1;
        private double y1;
        private double x2;
        private double y2;
        private int time;

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public double getX1() {
            return x1;
        }

        public void setX1(double x1) {
            this.x1 = x1;
        }

        public double getY1() {
            return y1;
        }

        public void setY1(double y1) {
            this.y1 = y1;
        }

        public double getX2() {
            return x2;
        }

        public void setX2(double x2) {
            this.x2 = x2;
        }

        public double getY2() {
            return y2;
        }

        public void setY2(double y2) {
            this.y2 = y2;
        }
    }
}
