package com.wyman.myapplication;

/**
 * @author wyman
 * @date 2018/6/7
 * description :
 */
public class Base {

    private String baseName = "base";

    public Base() {
        callName();
    }

    public void callName() {
        System.out.println(baseName);
    }

     static class Sub extends Base {
        private  String baseName = "sub";

        public void callName() {
            System.out.println(baseName);
        }
    }

    public static void main(String[] args) {
        Base demo = new Sub();















        Thread t=new Thread(){
            @Override
            public void run() {
                pong();
            }


        };


        t.run();
        System.out.println("ping");

    }

    private static void pong() {
        System.out.println("pong");
    }

}
