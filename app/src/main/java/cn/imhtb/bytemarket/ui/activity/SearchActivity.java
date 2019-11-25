package cn.imhtb.bytemarket.ui.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;
import cn.imhtb.bytemarket.ui.adapter.GoodsAdapter;

public class SearchActivity extends AppCompatActivity {

    @BindArray(R.array.goods_title)
    String[] titles;

    @BindArray(R.array.goods_price)
    String[] prices;

    @BindArray(R.array.goods_image)
    TypedArray images;

    @BindView(R.id.toolbar_search)
    Toolbar toolbar;

    @BindView(R.id.srl_search_refresh)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.rv_search_result_list)
    RecyclerView recyclerView;

    private GoodsAdapter adapter;

    private List<GoodsEntity> list = new ArrayList<>();

    static {
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) ->
             new ClassicsFooter(context)
                    .setDrawableSize(20)
                    .setFinishDuration(0)
                    .setPrimaryColor(context.getResources().getColor(R.color.colorBackgroundLightGray))
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initData();

        //设置瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //刷新控件
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreData());

        //初始化适配器及数据
        adapter = new GoodsAdapter(this,list,position -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("GOODS", JSON.toJSONString(list.get(position)));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadMoreData() {

        Handler handler = new Handler();
        handler.postDelayed(()->{

            for (int i = 0; i < titles.length; i++) {
                GoodsEntity goods = new GoodsEntity();
                goods.setTitle(titles[i]);
                goods.setPrice(new BigDecimal(prices[i]));
                goods.setImageId(images.getResourceId(i,0));
                UserEntity userEntity = new UserEntity();
                userEntity.setUsername("人称江湖梁总");
                goods.setAuthor(userEntity);
                list.add(goods);
            }

            adapter.notifyDataSetChanged();
            smartRefreshLayout.finishLoadMore();
        },1000);
    }

    private void initData() {
        for (int i = 0; i < titles.length; i++) {
            GoodsEntity goods = new GoodsEntity();
            goods.setTitle(titles[i]);
            goods.setDescribe(titles[i] + titles[i]);
            goods.setPrice(new BigDecimal(prices[i]));
            goods.setImageId(images.getResourceId(i,0));
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("人称江湖梁总");
            goods.setAuthor(userEntity);
            list.add(goods);
        }

    }
}
