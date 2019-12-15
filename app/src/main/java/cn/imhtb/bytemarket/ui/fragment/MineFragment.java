package cn.imhtb.bytemarket.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.ui.activity.AddressActivity;
import cn.imhtb.bytemarket.ui.activity.FavourActivity;
import cn.imhtb.bytemarket.ui.activity.LoginActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.rl_mine_login)
    RelativeLayout relativeLayout;

    @BindView(R.id.ll_mine_un_login)
    LinearLayout linearLayout;

    @BindView(R.id.btn_mine_logout)
    Button btn_logout;

    @BindView(R.id.iv_user_avatar)
    CircleImageView iv_avatar;

    @BindView(R.id.tv_user_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_user_signature)
    TextView tv_signature;

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
        Log.d("ttt", "mine fragment  onViewCreate");

        initLoginComponent(AppComponent.isLogin);
    }

    private void initLoginComponent(boolean login) {
        if (!login){
            //未登录
            linearLayout.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }else{
            //已登录
            initUserInfo();
            linearLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initUserInfo(){
        User user = UserHelper.getInstance().getLoginUser(getActivity());
        Glide.with(Objects.requireNonNull(getActivity())).load(user.getAvatar()).into(iv_avatar);
        tv_nickname.setText(user.getNickName());
        String signature = "签名:" + (TextUtils.isEmpty(user.getDescription())?user.getDescription():"这人很懒，什么都没留下~");
        tv_signature.setText(signature);
    }

    @OnClick({R.id.ll_favour,R.id.ll_history,R.id.ll_mine_un_login,R.id.rl_mine_login,R.id.btn_mine_logout,R.id.rl_mine_fragment_address})
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
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } break;
            case R.id.rl_mine_login:{
                //initLoginComponent(false);
            } break;
            case R.id.btn_mine_logout:{
                handleLogout();
            } break;
            case R.id.rl_mine_fragment_address:{
                startActivity(new Intent(getActivity(), AddressActivity.class));
            } break;
        }
    }

    private void handleLogout() {
        AppComponent.isLogin = false;
        initLoginComponent(false);
        UserHelper.getInstance().setLogout(getActivity());
        Toast.makeText(getActivity(),"已登出",Toast.LENGTH_SHORT).show();
    }

}
