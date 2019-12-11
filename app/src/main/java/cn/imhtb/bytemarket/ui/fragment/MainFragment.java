package cn.imhtb.bytemarket.ui.fragment;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;
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

    private List<Goods> list = new ArrayList<>();

    private FragmentActivity context;

    private ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();

    public MainFragment() {}

    //static 代码段可以防止内存泄露
    static {
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

        //刷新控件
        smartRefreshLayout = view.findViewById(R.id.swipe_refresh);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreData());

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

    private void init() {

        new GetGoods().execute();

        new GetBanner().execute();

        new GetCategory().execute();
    }

    private void loadMoreData() {

        String params = "?page=" + (page++);
        if (entity!=null&&entity.getId()!=0){
            params = params + "&cid=" + entity.getId();
        }

        new LoadMoreGoods().execute(params);
    }

    class LoadMoreGoods extends AsyncTask<String,Void,List<Goods>>{

        List<Goods> data = new ArrayList<>();
        @Override
        protected List<Goods> doInBackground(String... strings) {
            OkHttpUtils.doGet(Api.TYPE_GOODS,Api.URL_GET_GOODS + strings[0],context,(ICallBackHandler<List<Goods>>) response ->{
                data = response.getData();
            },true);
            return data;
        }

        @Override
        protected void onPostExecute(List<Goods> goodsEntities) {
            super.onPostExecute(goodsEntities);
            list.addAll(goodsEntities);
            adapter.notifyDataSetChanged();
            smartRefreshLayout.finishLoadMore();
        }
    }

    class GetGoods extends AsyncTask<Void,Void,List<Goods>>{

        List<Goods> data = new ArrayList<>();
        @Override
        protected List<Goods> doInBackground(Void... voids) {
            OkHttpUtils.doGet(Api.TYPE_GOODS, Api.URL_GET_GOODS, context, (ICallBackHandler<List<Goods>>) response -> {
                data = response.getData();
            },true);
            return data;
        }

        @Override
        protected void onPostExecute(List<Goods> goodsEntities) {
            super.onPostExecute(goodsEntities);
            //初始化适配器及数据
            list.addAll(goodsEntities);
            adapter = new GoodsAdapter(getActivity(),list,position -> {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("GOODS", JSON.toJSONString(list.get(position)));
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    class GetCategory extends AsyncTask<Void,Void,ArrayList<CustomTabEntity>>{

        @Override
        protected ArrayList<CustomTabEntity> doInBackground(Void... voids) {
            customTabEntities.add(new TabEntity("全部",0));
            OkHttpUtils.doGet(Api.TYPE_CATEGORY,Api.URL_GET_CATEGORY,context,(ICallBackHandler<List<Category>>) response ->{
                List<Category> data = response.getData();
                AppComponent.categoryList.clear();
                AppComponent.categoryList.addAll(data);
                data.forEach(v -> customTabEntities.add(new TabEntity(v.getName(),v.getId())));

            },true);
            return customTabEntities;
        }

        @Override
        protected void onPostExecute(ArrayList<CustomTabEntity> tabEntities) {
            super.onPostExecute(tabEntities);
            commonTabLayout.setTabData(tabEntities);
        }
    }

    class GetBanner extends AsyncTask<Void,Void,List<BannerEntity>>{

        List<BannerEntity> list = new ArrayList<>();

        @Override
        protected List<BannerEntity> doInBackground(Void... voids) {
            OkHttpUtils.doGet(Api.TYPE_BANNER,Api.URL_GET_BANNER, context, (ICallBackHandler<List<BannerEntity>>)response -> {
                list = response.getData();
            },true);
            return list;
        }

        @Override
        protected void onPostExecute(List<BannerEntity> bannerEntities) {
            super.onPostExecute(bannerEntities);
            List<String> urls = list.stream().map(BannerEntity::getUrl).collect(Collectors.toList());
            List<String> tips = list.stream().map(BannerEntity::getTips).collect(Collectors.toList());
            contentBanner.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) ->
                    Glide.with(context)
                            .load(model)
                            .placeholder(R.drawable.p_seekbar_thumb_normal)
                            .error(R.drawable.p_seekbar_thumb_normal)
                            .centerCrop()
                            .dontAnimate()
                            .into(itemView));
            contentBanner.setData(urls, tips);
        }
    }

    @OnClick(R.id.ll_main_search)
    void toSearch(){
        startActivity(new Intent(context, SearchActivity.class));
    }

}
