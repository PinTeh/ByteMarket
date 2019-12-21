package cn.imhtb.bytemarket.app;

import android.app.Application;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.bean.Category;
import cn.imhtb.bytemarket.bean.CloudUserInfo;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class AppComponent extends Application {

    public static boolean isLogin = false;

    public static List<Category> categoryList = new ArrayList<>();

    public static List<Campus> campusList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this,"kj7swf8okn4q2");
        RongIM.setUserInfoProvider(this::findById, true);
    }

    private UserInfo findById(String userId){
        Executors.newCachedThreadPool().execute(()->{
            OkHttpUtils.doGet(Api.TYPE_RONG_USER_INFO, Api.URL_GET_RONG_USER_INFO + "?uid=" + userId, getApplicationContext(), (ICallBackHandler<CloudUserInfo>) response -> {
                CloudUserInfo data = response.getData();
                UserInfo userInfo = new UserInfo(userId, data.getName(), Uri.parse(data.getAvatar()));
                RongIM.getInstance().refreshUserInfoCache(userInfo);
            },false);
        });
        return null;
    }
}
