package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Address;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.bean.MessageEvent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v->finish());

        //获取商品
        Goods goods = JSON.parseObject(getIntent().getStringExtra("GOODS"),Goods.class);
        if (goods==null){
            return;
        }
        Glide.with(this).load(goods.getCover()).into(stv_goods.getLeftIconIV());
        stv_goods.setLeftTopString(goods.getTitle());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String priceString = "￥" + decimalFormat.format(goods.getPrice());
        stv_goods.setLeftBottomString(priceString);
        stv_total.setRightTopString(priceString);
        tv_total.setText(priceString);

        //获取地址

    }


    @OnClick({R.id.btn_purchase_confirm,R.id.stv_purchase_address})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_purchase_confirm:
                handlePurchase();
                break;
            case R.id.stv_purchase_address:
                startActivity(new Intent(PurchaseActivity.this,AddressActivity.class));
                break;
            default:
        }
    }

    private void handlePurchase() {

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

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MessageEvent event) {
        if ("address:selected".equals(event.getMessage())){
            String data = event.getData();
            Address address = JSON.parseObject(data, Address.class);
            stv_address.setLeftTopString(address.getName());
            stv_address.setCenterTopString(address.getPhone());
            stv_address.setLeftString(address.getAddress());
        }
    }
}
