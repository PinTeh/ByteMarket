package cn.imhtb.bytemarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView desc = findViewById(R.id.tv_top_desc);
        desc.setText("聊天");

        ImageView back = findViewById(R.id.iv_top_back);
        back.setOnClickListener(v -> finish());
    }
}
