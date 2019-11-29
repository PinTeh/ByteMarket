package cn.imhtb.bytemarket.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.UserEntity;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.common.ServerResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


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
    }

    @OnClick(R.id.btn_http_test)
    public void request(){
        String url = "http://www.mockhttp.cn/mock/foo/bar";
        OkHttpUtils.doGet(getActivity(), url, serverResponse -> {
            UserEntity strings = (UserEntity) serverResponse.getData();
            Log.d("ttt",strings.toString());
            Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
        });
    }
}
