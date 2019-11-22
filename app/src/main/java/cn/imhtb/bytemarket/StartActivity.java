package cn.imhtb.bytemarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

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

        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        init();
    }

    private void init() {
        boolean isFirstEnter = sharedPreferences.getBoolean("isFirstEnter",true);
        if (isFirstEnter){
            handler.sendEmptyMessageDelayed(GO_GUIDE,200);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("isFirstEnter",false);
            edit.apply();
        }else {
            handler.sendEmptyMessageDelayed(GO_MAIN,200);
        }
    }
}
