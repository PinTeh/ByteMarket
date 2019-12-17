package cn.imhtb.bytemarket.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.common.Api;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.civ_detail_user_avatar)
    CircleImageView civAvatar;

    @BindView(R.id.tv_detail_user_username)
    TextView tvUsername;

    @BindView(R.id.stv_detail_price)
    SuperTextView stvPrice;

    @BindView(R.id.tv_detail_title)
    TextView tvTitle;

    @BindView(R.id.tv_detail_describe)
    TextView tvDescribe;

    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;

    @BindView(R.id.web_view_detail)
    WebView webView;

    private String goodsString;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        goodsString = getIntent().getStringExtra("GOODS");
        Goods goods = JSON.parseObject(goodsString,Goods.class);
        if (goods!=null) {
            WebSettings settings = webView.getSettings();
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            webView.loadUrl(Api.URL_DETAIL_HTML + goods.getId());
        }

        toolbar.setNavigationOnClickListener(v->finish());

    }

    @OnClick({R.id.iv_goods_detail_chat})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goods_detail_chat:
                Intent intent = new Intent(DetailActivity.this, PurchaseActivity.class);
                intent.putExtra("GOODS",goodsString);
                startActivity(intent);
                break;
            case 666:

            default:
                break;
        }
    }
}
