package cn.imhtb.bytemarket.view.fragment;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.TabEntity;
import cn.imhtb.bytemarket.entity.GoodsEntity;
import cn.imhtb.bytemarket.entity.UserEntity;
import cn.imhtb.bytemarket.view.adapter.GoodsAdapter;

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


    private SmartRefreshLayout smartRefreshLayout;

    private GoodsAdapter adapter;

    private List<GoodsEntity> list = new ArrayList<>();

    private ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();

    public MainFragment() {}

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20).setFinishDuration(0);
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

        RecyclerView recyclerView = view.findViewById(R.id.rv_goods);
        //设置瀑布流布局
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        //初始化适配器及数据
        adapter = new GoodsAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        init();

        //刷新控件
        smartRefreshLayout = view.findViewById(R.id.swipe_refresh);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreData());

        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                //TODO 如何区分？
                list.clear();
                adapter.notifyDataSetChanged();
                loadMoreData();

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

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

    private void init() {
        for (int i = 0; i < titles.length; i++) {
            GoodsEntity goods = new GoodsEntity();
            goods.setTitle(titles[i]);
            goods.setPrice(new BigDecimal(prices[i]));
            goods.setImageId(images.getResourceId(i,0));
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("人称江湖梁总");
            goods.setAuthor(userEntity);
            list.add(goods);
            list.add(goods);
        }

        contentBanner.setAdapter((BGABanner.Adapter<ImageView, String>) (banner, itemView, model, position) ->
                Glide.with(getActivity())
                .load(model)
                .placeholder(R.drawable.p_seekbar_thumb_normal)
                .error(R.drawable.p_seekbar_thumb_normal)
                .centerCrop()
                .dontAnimate()
                .into(itemView));

        contentBanner.setData(
                Arrays.asList("http://image.imhtb.cn/3f5ff03fa0c024b930f515e63ae2c702.jpg_945x288_7dff4510.jpg",
                        "http://image.imhtb.cn/76e68eda4ed089c0e5b0ce2367efe428.jpg"),
                Arrays.asList("tips1", "tips1"));

        for (String mTitle : subTitles) {
            customTabEntities.add(new TabEntity(mTitle));
        }
        commonTabLayout.setTabData(customTabEntities);
    }



}
