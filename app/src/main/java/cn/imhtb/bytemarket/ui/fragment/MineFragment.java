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
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.ui.activity.FavourActivity;
import cn.imhtb.bytemarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.rl_mine_login)
    RelativeLayout relativeLayout;

    @BindView(R.id.ll_mine_un_login)
    LinearLayout linearLayout;

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
        ButterKnife.bind(this,view);
        initLoginComponent(false);
    }

    private void initLoginComponent(boolean login) {
        if (!login){
            linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.ll_favour,R.id.ll_history,R.id.ll_mine_un_login,R.id.rl_mine_login})
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
            } break;
            case R.id.ll_mine_un_login:{
                initLoginComponent(true);
            } break;
            case R.id.rl_mine_login:{
                initLoginComponent(false);
            }
        }
    }

}
