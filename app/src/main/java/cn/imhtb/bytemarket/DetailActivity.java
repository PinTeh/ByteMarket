package cn.imhtb.bytemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView textView = findViewById(R.id.tv_top_desc);
        textView.setText("商品详情");

        ImageView back = findViewById(R.id.iv_top_back);
        back.setOnClickListener(this);


        TextView chat = findViewById(R.id.iv_goods_detail_chat);
        chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_top_back:
                finish();
                break;
            case R.id.iv_goods_detail_chat:
                Log.d("DetailActivity","yes");
                Intent intent = new Intent(DetailActivity.this,ChatActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
