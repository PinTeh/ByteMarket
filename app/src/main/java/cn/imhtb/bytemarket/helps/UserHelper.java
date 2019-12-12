package cn.imhtb.bytemarket.helps;

import android.content.Context;

import cn.imhtb.bytemarket.utils.SpUtils;

public class UserHelper {

    private static UserHelper instance;

    private UserHelper(){}

    public static UserHelper getInstance(){
        if (instance==null){
            synchronized (UserHelper.class){
                if (instance==null){
                    instance = new UserHelper();
                }
            }
        }
        return instance;
    }

    public boolean setAutoLogin(Context context,String username){
        //TODO check

        return SpUtils.login(context, username);
    }

    public boolean setLogout(Context context){
        return SpUtils.logout(context);
    }

    public boolean isLogin(Context context){
        return SpUtils.isLogin(context);
    }
}
