package cn.imhtb.bytemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        TextView textView = findViewById(R.id.tv_top_desc);
        textView.setText("商品详情");



    }

    @OnClick({R.id.iv_top_back,R.id.iv_goods_detail_chat})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_top_back:
                finish();
                break;
            case R.id.iv_goods_detail_chat:
                Intent intent = new Intent(DetailActivity.this,ChatActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
