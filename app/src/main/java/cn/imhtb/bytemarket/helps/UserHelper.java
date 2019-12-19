package cn.imhtb.bytemarket.helps;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.utils.SpUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

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

    public boolean setAutoLogin(Context context,String body){
        //TODO check

        return SpUtils.login(context, body);
    }

    public User getLoginUser(Context context){
        return SpUtils.getLoginUser(context);
    }

    public boolean setLogout(Context context){
        return SpUtils.logout(context);
    }

    public boolean isLogin(Context context){
        return SpUtils.isLogin(context);
    }

    public void connectRongCloud(Context context){
        User loginUser = getLoginUser(context);

        if (loginUser==null){
            Log.d("ttt", "handleRongCloud: 未登录");
            return;
        }
        String token = loginUser.getRongCloudToken();
        Log.d("ttt", "connectRongCloud: " + loginUser.toString());
        Log.d("ttt", "handleRongCloud: " + token);

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d("ttt", "--onTokenIncorrect" );
            }
            @Override
            public void onSuccess(String userid) {
                Log.d("ttt", "--onSuccess" + userid);

            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("ttt", "--onSuccess" + errorCode);
            }
        });

    }
}
