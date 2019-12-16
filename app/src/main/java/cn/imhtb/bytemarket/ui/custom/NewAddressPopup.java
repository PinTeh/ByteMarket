package cn.imhtb.bytemarket.ui.custom;

import android.content.Context;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lxj.xpopup.core.BottomPopupView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author PinTeh
 */
public class NewAddressPopup extends BottomPopupView {

    TextView receiver;

    TextView phone;

    TextView address;

    Context context;

    public NewAddressPopup(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_new_address;
    }


    @Override
    protected void onShow() {
        super.onShow();
        receiver = findViewById(R.id.et_address_new_receiver);
        phone = findViewById(R.id.et_address_new_phone);
        address = findViewById(R.id.et_address_new_address);
        Button save = findViewById(R.id.bt_address_new_save);
        save.setOnClickListener(v->{
            handleLogin();
        });
    }

    public void handleLogin(){
        User loginUser = UserHelper.getInstance().getLoginUser(context);
        if (loginUser==null){
            return;
        }
        Executors.newCachedThreadPool().execute(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS).build();

            String nameString = receiver.getText().toString();
            String phoneString = phone.getText().toString();
            String addressString = address.getText().toString();
            Map<String,Object> map = new HashMap<>();
            map.put("name",nameString);
            map.put("phone",phoneString);
            map.put("address",addressString);
            map.put("userId",loginUser.getId());

            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(Api.URL_ADDRESS_ADD)
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();
                ServerResponse serverResponse = gson.fromJson(ret,ServerResponse.class);
                if (serverResponse.isSuccess()) {
                    EventBus.getDefault().post(new MessageEvent("address:success"));
                } else {
                    Looper.prepare();
                    Toast.makeText(context,serverResponse.getMsg(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } catch (IOException e) {
                //
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }
}
