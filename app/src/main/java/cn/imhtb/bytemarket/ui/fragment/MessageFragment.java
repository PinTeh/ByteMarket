package cn.imhtb.bytemarket.ui.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.ui.activity.ConversationListActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

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
        //开启会话列表
//        Map map = new HashMap();
//        RongIM.getInstance().startConversationList(context , map);

        //String title = getActivity().getIntent().getData().getQueryParameter("title");
        Button button = view.findViewById(R.id.test);
        button.setOnClickListener(v->{
            RongIM.getInstance().startPrivateChat(context, "2", "测试");
        });
        Button button2 = view.findViewById(R.id.test2);
        button2.setOnClickListener(v->{
            handleRongCloud();
        });

        Button button3 = view.findViewById(R.id.test3);
        button3.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ConversationListActivity.class));
        });

    }


    private void handleRongCloud() {

        String u1 = "EuiM+gN1/iD4F5O34H3rKXoMoz16rXivd3u4kf3eyrroMpd3xvfS1q3z3wW3fRiLd0nUCGxtheqbQUlKF8lidA==";
        String u2 = "X1vJTiyYnN8CYrpv42dRfFV4U0g/MyVYgUhEmSFbD1cDaANsub0wR45MQI+rRQzPr9iD7sed54g=";
        String token;
        User loginUser = UserHelper.getInstance().getLoginUser(getActivity());
        if (loginUser==null){
            Log.d("ttt", "handleRongCloud: 未登录");
            return;
        }
        if (loginUser.getId()==1){
            token = u1;
        }else {
            token = u2;
        }
        Log.d("ttt", "handleRongCloud: " + token);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.d("ttt", "--onTokenIncorrect" );

            }
            @Override
            public void onSuccess(String userid) {
                Log.d("ttt", "--onSuccess" + userid);

            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.d("ttt", "--onSuccess" + errorCode);
            }
        });
    }

}
