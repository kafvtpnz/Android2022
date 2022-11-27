package com.example.hw;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class HelloActivity extends Activity implements OnClickListener {
    Button btn_StartExternal, btn_StartInternal;
    ListView textList;
    ArrayList<String> StringArray = new ArrayList<String>();
    ArrayAdapter<String> TextAdapter;

    final Looper looper = Looper.getMainLooper();
    final Message message = Message.obtain();

    final Handler handler = new Handler(looper) {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            if (msg.sendingUid == 1) {
                for (String i : (ArrayList<String>) msg.obj) {
                    StringArray.add(i);
                }
                textList.setAdapter(TextAdapter);
                btn_StartExternal.setEnabled(true);
            }

            if (msg.sendingUid == 2) {
                btn_StartInternal.setEnabled((Boolean) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloactivity);

        textList = findViewById(R.id.textList);

        btn_StartExternal = findViewById(R.id.btn_Start1);
        btn_StartInternal = findViewById(R.id.btn_Start2);
        btn_StartExternal.setOnClickListener(this);
        btn_StartInternal.setOnClickListener(this);
        TextAdapter = new ArrayAdapter(this,
                 android.R.layout.simple_expandable_list_item_1, StringArray);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Start1:
                btn_StartExternal.setEnabled(false);
               new ThreadTask(handler).doSomething();
               break;

            case R.id.btn_Start2:
                btn_StartInternal.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            // Имитируем высокую нагрузку
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            message.sendingUid = 2;
                        }
                        message.obj = true;
                        handler.sendMessage(message);
                    }
                }).start();
        }
    }
}
