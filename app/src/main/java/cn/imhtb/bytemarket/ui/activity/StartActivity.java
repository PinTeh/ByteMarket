package cn.imhtb.bytemarket.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
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
        boolean isLogin = SpUtils.isLogin(StartActivity.this);
        if (isLogin){
            AppComponent.isLogin = true;
        }
        if (isFirstEnter){
            handler.sendEmptyMessageDelayed(GO_GUIDE,500);
            SpUtils.initGuide(StartActivity.this);
        }else {
            handler.sendEmptyMessageDelayed(GO_MAIN,500);
        }

        //状态栏设置
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        View view = window.getDecorView();
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
