package cn.imhtb.bytemarket.ui.activity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Address;
import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.ui.adapter.AddressAdapter;
import cn.imhtb.bytemarket.ui.custom.NewAddressPopup;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.rv_address_list)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar_address)
    Toolbar toolbar;

    private BasePopupView popupView;


    private AddressAdapter addressAdapter;

    private List<Address> addresses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        addressAdapter = new AddressAdapter(addresses,
                AddressActivity.this,
                R.layout.item_address,
                (position, text) -> {
                    if ("编辑".equals(text)){
                        Toast.makeText(this,"编辑功能懒得开发了",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    handleDelete(addresses.get(position).getId());
                });

        addressAdapter.setSelectedListener((position, text) -> {
            Address address = addresses.get(position);
            Log.d("ttt", "onCreate: " + "发送事件");
            EventBus.getDefault().postSticky(new MessageEvent("address:selected", JSON.toJSONString(address)));
            finish();
        });

        recyclerView.setAdapter(addressAdapter);
        recyclerView.setLayoutManager(manager);
        getAddress();
        toolbar.setNavigationOnClickListener(v->finish());
    }

    private void handleDelete(Integer id) {

        Executors.newCachedThreadPool().execute(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS).build();

            Gson gson = new Gson();
            Request request = new Request.Builder()
                    .url(Api.URL_ADDRESS_DELETE + "/" + id)
                    .delete()
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();
                ServerResponse serverResponse = gson.fromJson(ret,ServerResponse.class);
                if (serverResponse.isSuccess()) {
                    EventBus.getDefault().post(new MessageEvent("address:delete"));
                } else {
                    Looper.prepare();
                    Toast.makeText(AddressActivity.this,serverResponse.getMsg(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } catch (IOException e) {
                //
            }
        });
    }

    @OnClick(R.id.btn_address_add)
    public void add(){
        popupView = new XPopup.Builder(AddressActivity.this)
                .autoOpenSoftInput(true)
                .asCustom(new NewAddressPopup(AddressActivity.this))
                .show();
    }

    private void getAddress(){

        User user = UserHelper.getInstance().getLoginUser(AddressActivity.this);
        if (user==null){
            Toast.makeText(this,"请登录后尝试",Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newCachedThreadPool().execute(()->{
            OkHttpUtils.doGet(Api.TYPE_ADDRESS,Api.URL_GET_ADDRESS + "?uid=" + user.getId(), AddressActivity.this, (ICallBackHandler<List<Address>>) r -> {
                runOnUiThread(()->{
                    List<Address> list = r.getData();
                    addresses.clear();
                    addresses.addAll(list);
                    addressAdapter.notifyDataSetChanged();
                });
            },false);
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
        if ("address:success".equals(event.getMessage())){
            Toast.makeText(this,"操作成功",Toast.LENGTH_SHORT).show();
            getAddress();
            popupView.delayDismiss(250);
        }
        if ("address:delete".equals(event.getMessage())){
            Toast.makeText(this,"操作成功",Toast.LENGTH_SHORT).show();
            getAddress();
        }
    }
}
