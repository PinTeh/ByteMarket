package cn.imhtb.bytemarket.app;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.imhtb.bytemarket.bean.Category;

public class AppComponent extends Application {

    public static boolean isLogin = false;

    public static List<Category> categoryList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }



}
