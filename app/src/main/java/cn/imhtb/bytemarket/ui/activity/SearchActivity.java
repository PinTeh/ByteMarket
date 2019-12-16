package cn.imhtb.bytemarket.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
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

    private int page = 1;

    private GoodsAdapter adapter;

    private List<Goods> list = new ArrayList<>();

    //搜索条件
    private Map<Integer,Integer> condition;

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

        init();

    }

    private void initDrawer() {
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
        recyclerViewSearch.setLayoutManager(manager);

        Executors.newCachedThreadPool().execute(()->{
            OkHttpUtils.doGet(Api.TYPE_CAMPUS,Api.URL_GET_CAMPUS, SearchActivity.this, (ICallBackHandler<List<Campus>>) r -> {
                runOnUiThread(()->{
                    List<Campus> list = r.getData();
//                    CampusAdapter adapter = new CampusAdapter(list
//                            ,this
//                            ,R.layout.item_campus_search
//                            ,p ->  Log.d("ttt", "initDrawer: " + "执行了")
//                            ,p -> {
//                        condition.put(2, list.get(p).getId());

                    CampusAdapter adapter = new CampusAdapter(list
                            ,this
                            ,R.layout.item_campus_search);

                    adapter.setListener(position -> {
                        int selectedIndex = adapter.getSelectedIndex() == position ? -1 : position;
                        adapter.setSelectedIndex(selectedIndex);
                        adapter.notifyDataSetChanged();
                    });
                    recyclerViewSearch.setAdapter(adapter);

                    loadData(true);
                });
            },false);
        });
    }

    private void initFilterComponent() {

        //获取条件
        Intent intent = getIntent();
        int campusId = intent.getIntExtra("ID",0);

        //默认选中
        condition = new LinkedHashMap<>();
        condition.put(0,0);
        condition.put(1,0);
        condition.put(2,campusId);
    }

    private void setFilterSuperTextViewColor(int index){

        SuperTextView current = stvFilters.get(index);
        Integer count = condition.get(index);
        if (count!=null){
            if (count == 0){
                //第一次选中
                condition.put(index,count+1);
                current.setRightIcon(R.mipmap.sort_down_red);
                current.setCenterTextColor(getResources().getColor(R.color.colorSelected));
            }else if (count == 1) {
                //第二次选中
                condition.put(index,count+1);
                current.setRightIcon(R.mipmap.sort_up_red);
                current.setCenterTextColor(getResources().getColor(R.color.colorSelected));
            }else if (count==2){
                //第三次选中
                condition.put(index,0);
                current.setCenterTextColor(getResources().getColor(R.color.colorUnSelected));
                current.setRightIcon(R.mipmap.sort_down);
            }
        }

    }

    private void loadMoreData() {
        page++;
        loadData(false);
    }

    private void init(){

        initFilterComponent();

        initComponent();

        initDrawer();

        loadData(false);

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

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 监听到回车键，会执行2次该方法。按下与松开
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    //松开事件
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //使用 adapter.notifyDataSetChanged() 时，必须保证传进 Adapter 的数据 List 是同一个 List
                        page = 0;
                        loadData(true);
                    }

                }
            }
            return false;
        });
    }

    @SuppressLint("NewApi")
    private void loadData(boolean isClear) {
        if (isClear){
            page = 1;
            list.clear();
        }
        String params = "?page=" + page;
        String key = editText.getText().toString().trim();
        if (!key.isEmpty()){
            params =  params + "&key="+key;
        }
        Integer time = condition.getOrDefault(0,0);
        Integer price = condition.getOrDefault(1,0);
        Integer school = condition.getOrDefault(2,0);
        // 这串东西连我自己都看不懂
        if (time!=null){
            if (time==1){
                params = params + "&time=desc";
            }else if (time==2){
                params = params + "&time=asc";
            }
        }
        if (price!=null) {
            if (price == 1) {
                params = params + "&price=desc";
            } else if (price == 2) {
                params = params + "&price=asc";
            }
        }
        if (school!=null&&school!=0){
            params = params + "&school=" + school;
        }

        searchGoods(params);
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
                loadData(true);
                break;
            case R.id.stv_search_time_filter:
                setFilterSuperTextViewColor(0);
                loadData(true);
                break;
        }
    }

    private void searchGoods(String ...strings){
        Executors.newCachedThreadPool().execute(()->
            OkHttpUtils.doGet(Api.TYPE_GOODS,Api.URL_SEARCH_GOODS + strings[0],SearchActivity.this,(ICallBackHandler<List<Goods>>) response ->
                runOnUiThread(()->{
                    List<Goods> data = response.getData();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                    editText.clearFocus();
                    smartRefreshLayout.finishLoadMore();
                })
            ,true)
        );
    }

}
