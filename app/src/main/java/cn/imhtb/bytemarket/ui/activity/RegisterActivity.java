package cn.imhtb.bytemarket.ui.activity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Address;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ServerResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etusername;

    private EditText etnickname;

    private EditText etpassword;

    private EditText etagain;

    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etusername = findViewById(R.id.et_username);
        etnickname = findViewById(R.id.et_nickname);
        etpassword = findViewById(R.id.et_password);
        etagain = findViewById(R.id.et_again);
        final Button bt = findViewById(R.id.bt_register);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        error = findViewById(R.id.tv_error);
        String username = etusername.getText().toString();
        String nickname = etnickname.getText().toString();
        String password = etpassword.getText().toString();
        String again = etagain.getText().toString();

        handleRegister(username,nickname,password);

    }


    public void handleRegister(String username,String nickName,String password){

        Executors.newCachedThreadPool().execute(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS).build();

            Map<String,String> map = new HashMap<>();
            map.put("username",username);
            map.put("nickName",nickName);
            map.put("password",password);

            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(Api.URL_REGISTER)
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();
                ServerResponse serverResponse = gson.fromJson(ret,ServerResponse.class);
                if (serverResponse.isSuccess()) {
                    EventBus.getDefault().post(new MessageEvent("register:success"));
                } else {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,serverResponse.getMsg(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } catch (IOException e) {
                Log.e("ttt","注册异常" + e);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if ("register:success".equals(event.getMessage())){
            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("注册成功");
            builder.setPositiveButton("确定", (dialogInterface, i) -> {
                finish();
            });
            builder.show();
        }
    }
}
