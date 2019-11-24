package cn.imhtb.bytemarket.ui.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


import cn.imhtb.bytemarket.ui.activity.FavourActivity;
import cn.imhtb.bytemarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.colorAccent, R.color.colorAccent);//全局设置主题颜色
            return new ClassicsHeader(context);
            //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
    }

    public MineFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout favour = view.findViewById(R.id.ll_favour);
        LinearLayout history = view.findViewById(R.id.ll_history);
        favour.setOnClickListener(this);
        history.setOnClickListener(this);

        SmartRefreshLayout smartRefreshLayout = view.findViewById(R.id.srl_mine_wrap);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(v->smartRefreshLayout.finishRefresh());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_favour:{
                Intent intent = new Intent(getActivity(), FavourActivity.class);
                intent.putExtra("desc","我收藏的");
                startActivity(intent);
            } break;
            case R.id.ll_history:{
                Intent intent = new Intent(getActivity(), FavourActivity.class);
                intent.putExtra("desc","历史记录");
                startActivity(intent);
            }
        }
    }

}
