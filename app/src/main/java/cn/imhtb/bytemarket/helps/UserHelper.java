package cn.imhtb.bytemarket.helps;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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

    public boolean validate(Context context,String nickName,String username,String password,String confirm){
        boolean flag = true;

         if (username==null || username.length() < 6||username.length()>12){
            Toast.makeText(context,"用户名长度应在6-12之间",Toast.LENGTH_SHORT).show();
            flag =false;
        } else if (nickName==null || nickName.length() < 4){
            Toast.makeText(context,"昵称长度不能小于4",Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if (password==null || password.length()<6 || password.length()>18){
            Toast.makeText(context,"密码长度应在6-18之间",Toast.LENGTH_SHORT).show();
            flag =  false;
        }
        else if (confirm==null || !confirm.equals(password)){
            Toast.makeText(context,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
            flag =  false;
        }
        return flag;
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
