package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Address;
import cn.imhtb.bytemarket.bean.Goods;
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
public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.stv_purchase_address)
    SuperTextView stv_address;

    @BindView(R.id.stv_purchase_goods_show)
    SuperTextView stv_goods;

    @BindView(R.id.stv_purchase_total)
    SuperTextView stv_total;

    @BindView(R.id.tv_purchase_total)
    TextView tv_total;

    @BindView(R.id.toolbar_purchase)
    Toolbar toolbar;

    private Address address;

    private Goods goods;

    private String priceFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> finish());

        //获取商品
        goods = JSON.parseObject(getIntent().getStringExtra("GOODS"), Goods.class);
        if (goods == null) {
            return;
        }
        Glide.with(this).load(goods.getCover()).into(stv_goods.getLeftIconIV());
        stv_goods.setLeftTopString(goods.getTitle());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        priceFormat = decimalFormat.format(goods.getPrice());
        String priceString = "￥" + priceFormat;
        stv_goods.setLeftBottomString(priceString);
        stv_total.setRightTopString(priceString);
        tv_total.setText(priceString);

        //获取地址

    }


    @OnClick({R.id.btn_purchase_confirm, R.id.stv_purchase_address})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_purchase_confirm:
                handlePurchase();
                break;
            case R.id.stv_purchase_address:
                startActivity(new Intent(PurchaseActivity.this, AddressActivity.class));
                break;
            default:
        }
    }

    private void handlePurchase() {
        //封装成订单
        User loginUser = UserHelper.getInstance().getLoginUser(this);
        if (loginUser == null) {
            Toast.makeText(PurchaseActivity.this, "请登录后重新尝试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address == null) {
            Toast.makeText(PurchaseActivity.this, "请选择授收货地址", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("userId", loginUser.getId());
        map.put("totalMoney", priceFormat);
        map.put("userAddress", address.getAddress());
        map.put("userPhone", address.getPhone());
        map.put("userName", address.getName());
        map.put("productId", goods.getId());

        Log.d("ttt", "handlePurchase: " + map.toString());
        Log.d("ttt", "handlePurchase: " + address.toString());


        Executors.newCachedThreadPool().execute(() -> {

            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(Api.URL_CREATE_ORDER)
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();
                ServerResponse serverResponse = gson.fromJson(ret, ServerResponse.class);
                Log.d("ttt", "购买" + ret);
                if (serverResponse.isSuccess()) {
                    Looper.prepare();
                    Toast.makeText(PurchaseActivity.this, serverResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    finish();
                    //TODO 跳到订单页面
                } else {
                    Looper.prepare();
                    Toast.makeText(PurchaseActivity.this, serverResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    Log.d("ttt", "购买失败" + ret);
                }
            } catch (IOException e) {
                Log.e("ttt", "购买异常" + e);
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


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent event) {
        if ("address:selected".equals(event.getMessage())) {
            String data = event.getData();
            address = JSON.parseObject(data, Address.class);
            stv_address.setLeftTopString(address.getName());
            stv_address.setCenterTopString(address.getPhone());
            stv_address.setLeftString(address.getAddress());
        }
    }
}
