package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.entity.FavourEntity;
import cn.imhtb.bytemarket.ui.adapter.FavourAdapter;

public class FavourActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favour);

        List<FavourEntity> list = new ArrayList<>();
        list.add(new FavourEntity("标题1",new BigDecimal("110")));
        list.add(new FavourEntity("标题2",new BigDecimal("12304.3")));
        list.add(new FavourEntity("标题3",new BigDecimal("23.5")));
        list.add(new FavourEntity("标题4",new BigDecimal("9")));
        list.add(new FavourEntity("标题5",new BigDecimal("12")));
        RecyclerView recyclerView = findViewById(R.id.rv_content);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        FavourAdapter adapter = new FavourAdapter(list,FavourActivity.this,R.layout.item_favour);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);


        ImageView back = findViewById(R.id.iv_top_back);
        TextView topTitle = findViewById(R.id.tv_top_desc);

        // 设置标题
        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        topTitle.setText(desc);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
