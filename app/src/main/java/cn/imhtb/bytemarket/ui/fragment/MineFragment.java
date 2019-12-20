package cn.imhtb.bytemarket.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.ui.activity.AddressActivity;
import cn.imhtb.bytemarket.ui.activity.FavourActivity;
import cn.imhtb.bytemarket.ui.activity.LoginActivity;
import cn.imhtb.bytemarket.ui.activity.PersonalActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;

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

    @BindView(R.id.tv_user_school_name)
    TextView tv_school_name;

    @BindView(R.id.tv_mine_gc)
    TextView tv_gc_count;

    @BindView(R.id.tv_mine_sc)
    TextView tv_sc_count;

    @BindView(R.id.tv_mine_pc)
    TextView tv_pc_count;


    private FragmentActivity context;

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
        context = getActivity();
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
        if (user==null){
            return;
        }
        //初始化个人信息
        if (user.getAvatar()!=null){
            Glide.with(Objects.requireNonNull(getActivity())).load(user.getAvatar()).into(iv_avatar);
        }

        tv_nickname.setText(user.getNickName());
        String signature = "签名: " + (!TextUtils.isEmpty(user.getDescription())?user.getDescription():"这人很懒，什么都没留下~");
        tv_signature.setText(signature);

        if (!TextUtils.isEmpty(user.getSchoolName())){
            String schoolName = "学校: " + user.getSchoolName();
            tv_school_name.setText(schoolName);
        }else {
            tv_school_name.setText("");
        }

        //初始化数量
        initCount(user.getId());
    }

    private void initCount(Integer uid) {
        if (uid==null){
            tv_gc_count.setText("0");
            tv_pc_count.setText("0");
            tv_sc_count.setText("0");
            return;
        }
        Executors.newCachedThreadPool().execute(()->{
            OkHttpUtils.doGet(Api.TYPE_MAP, Api.URL_PRODUCT_STATUS + "?uid=" + uid, getActivity(), (ICallBackHandler<Map<String, Integer>>) response -> {
                context.runOnUiThread(()->{
                    Map<String, Integer> data = response.getData();
                    if (data.get("gc") !=null){
                        tv_gc_count.setText(String.valueOf(data.get("gc")));
                    }
                    if (data.get("pc")!=null){
                        tv_pc_count.setText(String.valueOf(data.get("pc")));
                    }
                    if (data.get("sc")!=null){
                        tv_sc_count.setText(String.valueOf(data.get("sc")));
                    }
                });
            },false);
        });
    }

    @OnClick({R.id.ll_mine_sold,R.id.ll_mine_got,R.id.ll_mine_publishing,R.id.rl_mine_fragment_personal_center,R.id.ll_favour,R.id.ll_history,R.id.ll_mine_un_login,R.id.rl_mine_login,R.id.btn_mine_logout,R.id.rl_mine_fragment_address})
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
            case R.id.rl_mine_login:
            case R.id.rl_mine_fragment_personal_center: {
                if (UserHelper.getInstance().getLoginUser(getActivity())==null){
                    Toast.makeText(getActivity(),"请登陆后尝试",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(getActivity(), PersonalActivity.class));
            } break;
            case R.id.btn_mine_logout:{
                handleLogout();
            } break;
            case R.id.rl_mine_fragment_address:{
                startActivity(new Intent(getActivity(), AddressActivity.class));
            } break;
            case R.id.ll_mine_publishing:{
                Intent intent = new Intent(getActivity(), FavourActivity.class);
                intent.putExtra("desc","发布中");
                startActivity(intent);
            } break;
            case R.id.ll_mine_got:{
                //TODO
            } break;
            case R.id.ll_mine_sold: {
                //TODO
            } break;
            default:
        }
    }

    private void handleLogout() {
        AppComponent.isLogin = false;
        initLoginComponent(false);
        RongIM.getInstance().logout();
        UserHelper.getInstance().setLogout(getActivity());
        initCount(null);
        Toast.makeText(getActivity(),"已登出",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(MessageEvent event) {
        if ("notice:reload".equals(event.getMessage())){
           initUserInfo();
        }
    }
}
