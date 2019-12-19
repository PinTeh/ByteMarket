package cn.imhtb.bytemarket.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    @BindView(R.id.ll_detail_bottom)
    LinearLayout bottom;

    private String goodsString;

    private Goods goods;

    private User loginUser;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        goodsString = getIntent().getStringExtra("GOODS");
        goods = JSON.parseObject(goodsString,Goods.class);
        if (goods!=null) {
            WebSettings settings = webView.getSettings();
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            webView.loadUrl(Api.URL_DETAIL_HTML + goods.getId());
        }
        toolbar.setNavigationOnClickListener(v->finish());

        loginUser = UserHelper.getInstance().getLoginUser(DetailActivity.this);
        if (loginUser!=null){
            if (loginUser.getId().equals(goods.getUserId())){
                bottom.setVisibility(View.INVISIBLE);
            }
            handleFavour(Api.URL_GET_HISTORY);
        }


    }

    @OnClick({R.id.iv_goods_detail_buy,R.id.ll_detail_collect,R.id.iv_goods_detail_chat})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goods_detail_buy:
                Intent intent = new Intent(DetailActivity.this, PurchaseActivity.class);
                intent.putExtra("GOODS",goodsString);
                startActivity(intent);
                break;
            case R.id.ll_detail_collect:
                handleFavour(Api.URL_GET_COLLECT);
                break;
            case R.id.iv_goods_detail_chat:

                Bundle bundle = new Bundle();
                bundle.putString("GOODS",goodsString);
                //将商品信息string通过title传过去
                //RongIM.getInstance().startPrivateChat(DetailActivity.this, String.valueOf(goods.getUserId()), goods.getUser().getName());
                RongIM.getInstance().startConversation(DetailActivity.this, Conversation.ConversationType.PRIVATE,String.valueOf(goods.getUserId()), goods.getUser().getName(),bundle);


            default:
                break;
        }
    }

    private void handleFavour(String url) {
        Executors.newCachedThreadPool().execute(()-> {

            if (loginUser == null) {
                return;
            }

            int id = goods.getId();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS).build();

            Map<String, Object> map = new HashMap<>();
            map.put("userId", loginUser.getId());
            map.put("productId", id);

            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try {
                Response res = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(res.body()).string();
                ServerResponse serverResponse = gson.fromJson(ret, ServerResponse.class);
                if (serverResponse.isSuccess() && url.equals(Api.URL_GET_COLLECT)) {
                    Looper.prepare();
                    Toast.makeText(DetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
