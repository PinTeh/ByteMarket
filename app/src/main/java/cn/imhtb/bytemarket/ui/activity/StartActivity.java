package cn.imhtb.bytemarket.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.utils.SpUtils;

public class StartActivity extends AppCompatActivity {

    public static final int GO_MAIN = 1;
    public static final int GO_GUIDE = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case GO_MAIN:
                    startHomeActivity();
                    break;
                case GO_GUIDE:
                    startGuideActivity();
                    break;
                default:
                    break;
            }
        }
    };

    private void startGuideActivity() {
        Intent intent = new Intent(StartActivity.this,GuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(StartActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    private void init() {
        boolean isFirstEnter = SpUtils.isShowGuide(StartActivity.this);
        if (isFirstEnter){
            handler.sendEmptyMessageDelayed(GO_GUIDE,500);
            SpUtils.initGuide(StartActivity.this);
        }else {
            handler.sendEmptyMessageDelayed(GO_MAIN,2000);
        }
    }
}
