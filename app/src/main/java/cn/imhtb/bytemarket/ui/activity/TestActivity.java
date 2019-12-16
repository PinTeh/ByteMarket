package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.ui.adapter.GoodsAdapter;

public class TestActivity extends AppCompatActivity {

    List<Goods> list = new LinkedList<>();

    int page = 1;

    RecyclerView recyclerView;

    GoodsAdapter adapter;

    SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.rv_goods2);
        refreshLayout = findViewById(R.id.swipe_refresh2);

        //设置瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //解决抖动
        //manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GoodsAdapter(this,list, position -> {
            Intent intent = new Intent(this, DetailActivity.class);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        //刷新控件
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreData());

        loadMoreData();
    }

    private void loadMoreData() {
        String params = "&page=" + (page++);
        loadMore(params);
    }

    private void loadMore(String ...strings){
        Executors.newCachedThreadPool().execute(()-> {
            OkHttpUtils.doGet(Api.TYPE_GOODS, Api.URL_GET_GOODS + strings[0], this, (ICallBackHandler<List<Goods>>) response ->
                    this.runOnUiThread(() -> {
                        List<Goods> data = response.getData();
                        list.addAll(data);
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishLoadMore();
                    }), false);
        });
    }

}
