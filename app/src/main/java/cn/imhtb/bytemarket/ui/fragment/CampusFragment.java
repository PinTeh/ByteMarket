package cn.imhtb.bytemarket.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.CampusEntity;
import cn.imhtb.bytemarket.bean.FavourEntity;
import cn.imhtb.bytemarket.ui.activity.FavourActivity;
import cn.imhtb.bytemarket.ui.activity.SearchActivity;
import cn.imhtb.bytemarket.ui.adapter.CampusAdapter;
import cn.imhtb.bytemarket.ui.adapter.FavourAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampusFragment extends Fragment {

    private FragmentActivity context;

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
        context = getActivity();

        List<CampusEntity> list = new ArrayList<>();
        list.add(CampusEntity.newInstance());
        list.add(CampusEntity.newInstanceAlpha());
        list.add(CampusEntity.newInstanceBeta());
        list.add(CampusEntity.newInstance());
        list.add(CampusEntity.newInstance());
        list.add(CampusEntity.newInstance());

        RecyclerView recyclerView = view.findViewById(R.id.rv_campus_data_list);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        CampusAdapter adapter = new CampusAdapter(list, context,R.layout.item_campus,position -> {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("TAG", JSON.toJSONString(list.get(position)));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

    }

    private void init() {

    }
}
