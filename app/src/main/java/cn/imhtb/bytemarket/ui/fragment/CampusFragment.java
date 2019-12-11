package cn.imhtb.bytemarket.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.ui.activity.SearchActivity;
import cn.imhtb.bytemarket.ui.adapter.CampusAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampusFragment extends Fragment {

    @BindView(R.id.rv_campus_data_list)
    RecyclerView recyclerView;

    private FragmentActivity context;

    private List<Campus> list = new ArrayList<>();

    public CampusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_campus, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        context = getActivity();
        loadData();
    }

    private void loadData() {
        Executors.newCachedThreadPool().execute(()->{
            OkHttpUtils.doGet(Api.TYPE_CAMPUS,Api.URL_GET_CAMPUS, context, (ICallBackHandler<List<Campus>>) r -> {
                context.runOnUiThread(()->{
                    list = r.getData();

                    LinearLayoutManager manager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(manager);
                    CampusAdapter adapter = new CampusAdapter(list, context,R.layout.item_campus,position -> {
                        Intent intent = new Intent(context, SearchActivity.class);
                        intent.putExtra("TAG", JSON.toJSONString(list.get(position)));
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                });
            },false);
        });
    }
}
