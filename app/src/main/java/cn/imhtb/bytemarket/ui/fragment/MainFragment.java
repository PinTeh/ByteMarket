package cn.imhtb.bytemarket.ui.fragment;


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.imhtb.bytemarket.bean.BannerEntity;
import cn.imhtb.bytemarket.bean.CategoryEntity;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.ui.activity.DetailActivity;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.TabEntity;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;
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

    private int page = 1;

    private TabEntity entity;

    private SmartRefreshLayout smartRefreshLayout;

    private GoodsAdapter adapter;

    private List<GoodsEntity> list = new ArrayList<>();

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
        RecyclerView recyclerView = view.findViewById(R.id.rv_goods);
        //设置瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //初始化适配器及数据
        adapter = new GoodsAdapter(getActivity(),list,position -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("GOODS", JSON.toJSONString(list.get(position)));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

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
//                    OkHttpUtils.doGet(Api.TYPE_GOODS,Api.URL_GET_GOODS+"?cid="+entity.getId(),context,(ICallBackHandler<List<GoodsEntity>>) response ->{
//                        List<GoodsEntity> data = response.getData();
//                        list.addAll(data);
//                    });
                    adapter.notifyDataSetChanged();
                    loadMoreData();
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initData() {
        for (int j = 0; j < 2; j++) {
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

//        OkHttpUtils.doGet(Api.TYPE_GOODS,Api.URL_GET_GOODS,context,(ICallBackHandler<List<GoodsEntity>>) response ->{
//            List<GoodsEntity> data = response.getData();
//            list.addAll(data);
//        });

    }

    private void loadMoreData() {

//        String params = "?page=" + (++page);
//        if (entity.getId()!=0){
//            params = params + "&cid=" + entity.getId();
//        }
//            OkHttpUtils.doGet(Api.TYPE_GOODS,Api.URL_GET_GOODS+params,context,(ICallBackHandler<List<GoodsEntity>>) response ->{
//                List<GoodsEntity> data = response.getData();
//                list.addAll(data);
//            });

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

    private void init() {

        initData();

        initBanner();

        initCategoryBar();
    }

    private void initCategoryBar() {
//        OkHttpUtils.doGet(Api.TYPE_CATEGORY,Api.URL_GET_CATEGORY,context,(ICallBackHandler<List<CategoryEntity>>) response ->{
//            List<CategoryEntity> data = response.getData();
//            customTabEntities.add(new TabEntity("全部",0));
//            data.forEach(v ->{
//                customTabEntities.add(new TabEntity(v.getName(),v.getId()));
//            });
//        });
        entity = new TabEntity("全部",0);
        customTabEntities.add(new TabEntity("全部",0));
        for (String mTitle : subTitles) {
            customTabEntities.add(new TabEntity(mTitle));
        }
        commonTabLayout.setTabData(customTabEntities);
    }

    private void initBanner() {
        contentBanner.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) ->
                Glide.with(context)
                        .load(model)
                        .placeholder(R.drawable.p_seekbar_thumb_normal)
                        .error(R.drawable.p_seekbar_thumb_normal)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView));

        /*
        OkHttpUtils.doGet(Api.TYPE_BANNER,Api.URL_GET_BANNER, context, (ICallBackHandler<List<BannerEntity>>)response -> {
            List<BannerEntity> list = response.getData();
            List<String> urls = list.stream().map(BannerEntity::getUrl).collect(Collectors.toList());
            List<String> tips = list.stream().map(BannerEntity::getTips).collect(Collectors.toList());
            contentBanner.setData(urls, tips);
        });
         */

        contentBanner.setData(
                Arrays.asList("http://image.imhtb.cn/3f5ff03fa0c024b930f515e63ae2c702.jpg_945x288_7dff4510.jpg",
                        "http://image.imhtb.cn/76e68eda4ed089c0e5b0ce2367efe428.jpg"),
                Arrays.asList("tips1", "tips1"));
    }

    @OnClick(R.id.ll_main_search)
    public void toSearch(){
        startActivity(new Intent(context, SearchActivity.class));
    }
}
