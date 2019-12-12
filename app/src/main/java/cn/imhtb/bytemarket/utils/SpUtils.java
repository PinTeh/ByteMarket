package cn.imhtb.bytemarket.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import cn.imhtb.bytemarket.common.SpConstants;

import static android.content.Context.MODE_PRIVATE;

public class SpUtils {

    public static boolean login(Context context,String username){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SpConstants.LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(SpConstants.KEY_USER_NAME,username);
        return edit.commit();
    }

    public static boolean isLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SpConstants.LOGIN_FILE, MODE_PRIVATE);
        String name = sharedPreferences.getString(SpConstants.KEY_USER_NAME, "");
        return !TextUtils.isEmpty(name);
    }

    public static boolean logout(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SpConstants.LOGIN_FILE, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(SpConstants.KEY_USER_NAME);
        return edit.commit();
    }

    public static boolean initGuide(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SpConstants.CONFIG_FILE, MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(SpConstants.KEY_IS_FIRST,false);
        return edit.commit();
    }

    public static boolean isShowGuide(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SpConstants.CONFIG_FILE, MODE_PRIVATE);
        return sharedPreferences.getBoolean(SpConstants.KEY_IS_FIRST, true);
    }
}
