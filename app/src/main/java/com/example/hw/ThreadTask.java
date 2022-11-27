package com.example.hw;

import android.os.Build;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import androidx.annotation.RequiresApi;

public class ThreadTask {

    Handler thr_handler;

    final Message message = Message.obtain();

    ThreadTask(Handler main_handler){
        this.thr_handler = main_handler;
    }

    public void doSomething() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    // Имитируем высокую нагрузку
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ArrayList<String> StringArray = new ArrayList<String>();
                for(int i=0; i<5; i++){
                    StringArray.add(i + " string from ThreadTask");
                }

                message.sendingUid = 1;
                message.obj = StringArray;
                thr_handler.sendMessage(message);
            }
        }).start();
    }
}
