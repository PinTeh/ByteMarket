package cn.imhtb.bytemarket.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

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

    //TODO 删除
    int count = 0;
    Runnable runnable;
    private void loadInterestingImages() {
        runnable = new Runnable() {
            @Override
            public void run() {
                count++;
                getGif(count);
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);
    }

    public void getGif(int i){
        ImageView imageView = findViewById(R.id.iv_start);
        int index;
        switch (i){
            case 2:
                index =  R.drawable.i3;break;
            case 3:
                index = R.drawable.i2;break;
            case 4:
                index = R.drawable.i4;break;
            case 5:
                index = R.drawable.i5;break;
            default:
                index = R.drawable.i1;
        }
        Glide.with(StartActivity.this)
                .asGif()
                .load(index)
                .into(imageView);
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
            loadInterestingImages();
            handler.sendEmptyMessageDelayed(GO_MAIN,5000);
        }

        //状态栏设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            View view = window.getDecorView();
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
