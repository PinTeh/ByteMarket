package cn.imhtb.bytemarket.ui.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.CampusEntity;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;
import cn.imhtb.bytemarket.ui.adapter.CampusAdapter;
import cn.imhtb.bytemarket.ui.adapter.GoodsAdapter;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.et_search_key)
    EditText editText;

    @BindView(R.id.toolbar_search)
    Toolbar toolbar;

    @BindArray(R.array.goods_title)
    String[] titles;

    @BindArray(R.array.goods_price)
    String[] prices;

    @BindArray(R.array.goods_image)
    TypedArray images;

    @BindView(R.id.srl_search_refresh)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.rv_search_result_list)
    RecyclerView recyclerView;

    @BindView(R.id.rv_search_campus_list)
    RecyclerView recyclerViewSearch;

    @BindView(R.id.btn_search_campus_filter)
    Button campusFilter;

    @BindViews({R.id.stv_search_time_filter,R.id.stv_search_price_filter})
    List<SuperTextView> stvFilters;

    @BindView(R.id.drawer_search)
    DrawerLayout drawerLayout;

    @BindView(R.id.ll_search_right)
    LinearLayout linearLayout;

    private GoodsAdapter adapter;

    private List<GoodsEntity> list = new ArrayList<>();

    private Map<Integer,Integer> map;

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

        initFilterComponent();

        init();

        initDrawer();

    }

    private void initDrawer() {
        List<CampusEntity> list = new ArrayList<>();
        list.add(CampusEntity.newInstance());
        list.add(CampusEntity.newInstanceAlpha());
        list.add(CampusEntity.newInstanceBeta());
        list.add(CampusEntity.newInstance());

        LinearLayoutManager manager = new LinearLayoutManager(this);
        CampusAdapter adapter = new CampusAdapter(list, this,R.layout.item_campus_search, position -> {
            Log.d("ttt", "initDrawer: " + "执行了");
        });
        recyclerViewSearch.setAdapter(adapter);
        recyclerViewSearch.setLayoutManager(manager);
    }

    private void initFilterComponent() {
        //默认选中
        map = new LinkedHashMap<>();
        map.put(0,0);
        map.put(1,0);
    }

    private void setFilterSuperTextViewColor(int index){

        SuperTextView current = stvFilters.get(index);
        Integer count = map.get(index);
        if (count!=null){
            if (count == 0){
                //第一次选中
                map.put(index,count+1);
                current.setRightIcon(R.mipmap.sort_down_red);
                current.setCenterTextColor(getResources().getColor(R.color.colorSelected));
            }else if (count == 1) {
                //第二次选中
                map.put(index,count+1);
                current.setRightIcon(R.mipmap.sort_up_red);
                current.setCenterTextColor(getResources().getColor(R.color.colorSelected));
            }else if (count==2){
                //第三次选中
                map.put(index,0);
                current.setCenterTextColor(getResources().getColor(R.color.colorUnSelected));
                current.setRightIcon(R.mipmap.sort_down);
            }
        }

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

    private void init(){

        initComponent();

        initData();

    }

    private void initComponent() {
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

        //TODO
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    //松开事件
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //使用 adapter.notifyDataSetChanged() 时，必须保证传进 Adapter 的数据 List 是同一个 List
                        //而不能是其他对象，否则无法更新。
                        //list = list.stream().filter(g -> g.getTitle().contains(editText.getText().toString())).collect(Collectors.toList());
                        Collections.copy(list,list.stream().filter(g -> g.getTitle().contains(editText.getText().toString())).collect(Collectors.toList()));
                        adapter.notifyDataSetChanged();
                        editText.clearFocus();
                    }

                }
            }
            return false;
        });
    }

    @OnClick({R.id.btn_search_campus_filter,R.id.stv_search_price_filter,R.id.stv_search_time_filter})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search_campus_filter:
                drawerLayout.openDrawer(linearLayout);
                break;
            case R.id.stv_search_price_filter:
                setFilterSuperTextViewColor(1);
                break;
            case R.id.stv_search_time_filter:
                setFilterSuperTextViewColor(0);
                break;
        }
    }
}
