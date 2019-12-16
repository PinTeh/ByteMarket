package cn.imhtb.bytemarket.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.TabEntity;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.bean.BannerEntity;
import cn.imhtb.bytemarket.bean.Category;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.ui.activity.DetailActivity;
import cn.imhtb.bytemarket.ui.activity.SearchActivity;
import cn.imhtb.bytemarket.ui.adapter.GoodsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindArray(R.array.goods_title)
    String[] titles;

    @BindArray(R.array.subTitle)
    String[] subTitles;

    @BindArray(R.array.goods_price)
    String[] prices;

    @BindArray(R.array.goods_image)
    TypedArray images;

    @BindView(R.id.banner_guide_content)
    BGABanner contentBanner;

    @BindView(R.id.ctl_main_classify)
    CommonTabLayout commonTabLayout;

    @BindView(R.id.rv_goods)
    RecyclerView recyclerView;

    private int page = 1;

    private TabEntity entity;

    private SmartRefreshLayout smartRefreshLayout;

    private GoodsAdapter adapter;

    private List<Goods> list = new LinkedList<>();

    private FragmentActivity context;

    private ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();

    public MainFragment() {}

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorBackgroundLightGray, android.R.color.darker_gray);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context)
                    .setDrawableSize(20)
                    .setFinishDuration(0)
                    .setPrimaryColor(context.getResources().getColor(R.color.colorBackgroundLightGray));
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        context = getActivity();

        init();
        initComponent(view);

    }

    private void initComponent(View view) {
        //设置瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new GoodsAdapter(getActivity(),list,position -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("GOODS", JSON.toJSONString(list.get(position)));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        //刷新控件
        smartRefreshLayout = view.findViewById(R.id.swipe_refresh);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> refreshData());
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreData());
        //smartRefreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多


        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                page = 1;
                entity = (TabEntity) customTabEntities.get(position);
                if (entity!=null){
                    list.clear();
                    loadMoreData();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void refreshData() {
        page = 1;
        loadMoreData();
    }


    private void init() {

        loadMoreData();

        getBanner();

        getCategory();
    }

    private void loadMoreData() {
        String params = "&page=" + (page++);
        if (entity!=null && entity.getId()!=0){
            params = params + "&cid=" + entity.getId();
        }

        loadMore(params);
    }

    private void loadMore(String ...strings){
        recyclerView.stopScroll();
        recyclerView.stopNestedScroll();
        Executors.newCachedThreadPool().execute(()-> {
            OkHttpUtils.doGet(Api.TYPE_GOODS, Api.URL_GET_GOODS + strings[0], context, (ICallBackHandler<List<Goods>>) response ->
                    context.runOnUiThread(() -> {
                        List<Goods> data = response.getData();
                        list.addAll(data);
                        adapter.notifyDataSetChanged(); // adapter没设高度 会发生抖动
//                        if (data.size()>0) {
//                            //不然会报错
//                            adapter.notifyItemInserted(data.size());
//                        }
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                    }), false);
        });
    }

    @SuppressLint("NewApi")
    private void getCategory(){
        Executors.newCachedThreadPool().execute(()->{
            customTabEntities.add(new TabEntity("全部",0));
            OkHttpUtils.doGet(Api.TYPE_CATEGORY,Api.URL_GET_CATEGORY,context,(ICallBackHandler<List<Category>>) response ->{
                context.runOnUiThread(()-> {
                    List<Category> data = response.getData();
                    AppComponent.categoryList.clear();
                    AppComponent.categoryList.addAll(data);
                    data.forEach(v -> customTabEntities.add(new TabEntity(v.getName(), v.getId())));
                    commonTabLayout.setTabData(customTabEntities);
                });
            },false);
        });
    }

    @SuppressLint("NewApi")
    private void getBanner(){
        Executors.newCachedThreadPool().execute(()->{
            OkHttpUtils.doGet(Api.TYPE_BANNER,Api.URL_GET_BANNER, context, (ICallBackHandler<List<BannerEntity>>)response -> {
                context.runOnUiThread(()-> {
                    List<BannerEntity> list = response.getData();
                    List<String> urls = list.stream().map(BannerEntity::getUrl).collect(Collectors.toList());
                    List<String> tips = list.stream().map(BannerEntity::getTips).collect(Collectors.toList());
                    contentBanner.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) ->
                            Glide.with(context)
                                    .load(model)
                                    .placeholder(R.color.white)
                                    .error(R.color.white)
                                    .centerCrop()
                                    .dontAnimate()
                                    .into(itemView));
                    contentBanner.setData(urls, tips);
                });
            },false);
        });
    }

    @OnClick(R.id.ll_main_search)
    void toSearch(){
        startActivity(new Intent(context, SearchActivity.class));
    }

}
