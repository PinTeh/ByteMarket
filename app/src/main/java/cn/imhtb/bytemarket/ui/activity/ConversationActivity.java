package cn.imhtb.bytemarket.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.base.BaseActivity;
import cn.imhtb.bytemarket.bean.Goods;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_conversation)
    TextView title;

    @BindView(R.id.toolbar_conversation)
    Toolbar toolbar;

    private String mTargetId;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v->finish());

        // 获取开启会话携带过来的参数
        Intent intent = getIntent();
        getIntentDate(intent);
        Bundle extras = intent.getExtras();
        if (extras==null){
            RelativeLayout relativeLayout = findViewById(R.id.rl_chat_goods_show);
            relativeLayout.setVisibility(View.GONE);
            return;
        }

        // 填充会话框上的商品信息
        String goodString = extras.getString("GOODS");
        Goods goods = JSON.parseObject(goodString, Goods.class);
        if (goods != null){
            ImageView cover = findViewById(R.id.iv_chat_goods_image);
            TextView price = findViewById(R.id.tv_chat_goods_price);
            Glide.with(this).load(goods.getCover()).into(cover);
            String priceString = "￥" + goods.getPrice().toEngineeringString();
            price.setText(priceString);

            Button buy = findViewById(R.id.btn_chat_buy);
            buy.setOnClickListener(v->{
                Intent intentPurchase = new Intent(this, PurchaseActivity.class);
                intentPurchase.putExtra("GOODS",goodString);
                startActivity(intentPurchase);
            });
        }

    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        String t = intent.getData().getQueryParameter("title");
        if (t!=null){
            title.setText(t);
        }

        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);

    }



    /**
     * 加载会话页面 ConversationFragment
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    /**
     * 重连
     */
    private void reconnect(String token) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {
                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
    }
}
