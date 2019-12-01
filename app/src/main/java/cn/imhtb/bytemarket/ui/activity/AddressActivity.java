package cn.imhtb.bytemarket.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.AddressEntity;
import cn.imhtb.bytemarket.ui.adapter.AddressAdapter;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.rv_address_list)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar_address)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        List<AddressEntity> list = new ArrayList<>();
        list.add(new AddressEntity("梁艺可","18888888888","广西壮族自治区桂林市灵川县桂林电子科技大学花江校区"));
        list.add(new AddressEntity("王胤凯","18778731412","广西壮族自治区桂林市灵川县桂林电子科技大学花江校区"));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        AddressAdapter adapter = new AddressAdapter(list,AddressActivity.this,R.layout.item_address);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        toolbar.setNavigationOnClickListener(v->finish());
    }
}
