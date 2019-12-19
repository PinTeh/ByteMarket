package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.base.BaseActivity;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.et_login_username)
    EditText et_account;

    @BindView(R.id.et_login_password)
    EditText et_password;

    @BindView(R.id.civ_login_avatar)
    CircleImageView civ_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnTextChanged(R.id.et_login_username)
    public void accountChangeListener(){
        //TODO
        if (et_account.getText().toString().trim().length() == 6){
            civ_avatar.setImageResource(R.mipmap.avatar);
        }else {
            civ_avatar.setImageResource(R.mipmap.avatar_un_login);
        }
    }

    public void handleLogin(){

        Executors.newCachedThreadPool().execute(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS).build();

            String username = et_account.getText().toString();
            String password = et_password.getText().toString();
            Map<String,String> map = new HashMap<>();
            map.put("username",username);
            map.put("password",password);

            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(Api.URL_LOGIN)
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();
                Log.d("ttt", "handleLogin: ret = " + ret);
                ServerResponse serverResponse = gson.fromJson(ret,ServerResponse.class);
                //ServerResponse serverResponse = gson.fromJson(response.body().string(),new TypeToken<ServerResponse<Object>>() {}.getType());
                if (serverResponse!=null && serverResponse.isSuccess()) {
                    AppComponent.isLogin = true;
                    String userString = gson.toJson(serverResponse.getData());
                    Log.d("ttt", "handleLogin: " + userString);
                    UserHelper.getInstance().setAutoLogin(this,userString);

                    EventBus.getDefault().post(new MessageEvent());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,serverResponse.getMsg(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    Log.d(TAG, "登录失败" + ret);
                }
            } catch (IOException e) {
                Log.e(TAG,"登录异常" + e);
            }
        });
    }

    @OnClick({R.id.btn_login_login,R.id.tv_go_to_register})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_login:{
               handleLogin();
            } break;
            case R.id.tv_go_to_register: {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            } break;
            default:
        }
    }
}
