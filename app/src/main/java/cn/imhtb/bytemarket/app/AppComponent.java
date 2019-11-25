package cn.imhtb.bytemarket.app;

import android.app.Application;

public class AppComponent extends Application {

    public static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
