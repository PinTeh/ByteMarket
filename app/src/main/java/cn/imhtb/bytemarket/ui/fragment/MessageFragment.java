package cn.imhtb.bytemarket.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.GoodsEntity;
import cn.imhtb.bytemarket.bean.UserEntity;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.common.ServerResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    private FragmentActivity context;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        context = getActivity();
    }

    @OnClick(R.id.btn_http_test)
    public void request(){

        OkHttpUtils.doGet(Api.TYPE_TEST, Api.URL_TEST, context, (ICallBackHandler<UserEntity>) response -> {
            UserEntity strings = response.getData();
            Toast.makeText(getActivity(),strings.toString(),Toast.LENGTH_SHORT).show();
        });
    }

    @OnClick(R.id.btn_http_test2)
    public void request2(){

        OkHttpUtils.doGet(Api.TYPE_TEST2,Api.URL_TEST2,getActivity(), (ICallBackHandler<List<GoodsEntity>>)response -> {
            List<GoodsEntity> list = response.getData();
            Toast.makeText(getActivity(),list.size()+"",Toast.LENGTH_SHORT).show();
        });
    }
}
