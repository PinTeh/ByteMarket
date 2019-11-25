package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String goodsString = getIntent().getStringExtra("GOODS");
        GoodsEntity goodsEntity = JSON.parseObject(goodsString, GoodsEntity.class);
        if (goodsEntity != null) {
            //civAvatar.setImageResource();
            UserEntity author = goodsEntity.getAuthor();
            if (author != null) {
                tvUsername.setText(author.getUsername());
            }
            stvPrice.setCenterString(goodsEntity.getPrice().toString());
            tvTitle.setText(goodsEntity.getTitle());
            tvDescribe.setText(goodsEntity.getDescribe());
        }

        toolbar.setNavigationOnClickListener(v->finish());

    }

    @OnClick({R.id.iv_goods_detail_chat})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goods_detail_chat:
                Intent intent = new Intent(DetailActivity.this, ChatActivity.class);
                startActivity(intent);
                break;
            case 666:

            default:
                break;
        }
    }
}
