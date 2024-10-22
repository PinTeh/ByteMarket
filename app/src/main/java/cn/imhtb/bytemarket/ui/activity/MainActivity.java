package cn.imhtb.bytemarket.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.TabEntity;
import cn.imhtb.bytemarket.base.BaseActivity;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.ui.NotScrollViewPager;
import cn.imhtb.bytemarket.ui.adapter.HomeAdapter;
import cn.imhtb.bytemarket.ui.fragment.CampusFragment;
import cn.imhtb.bytemarket.ui.fragment.MainFragment;
import cn.imhtb.bytemarket.ui.fragment.MessageFragment;
import cn.imhtb.bytemarket.ui.fragment.MineFragment;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ctl_main_bottom_bar)
    CommonTabLayout commonTabLayout;

    @BindView(R.id.vp_main_content)
    NotScrollViewPager viewPager = null;

    @BindArray(R.array.unSelect)
    TypedArray unSelected;

    @BindArray(R.array.select)
    TypedArray selected;

    @BindView(R.id.ll_main_add)
    LinearLayout llAdd;

    @BindView(R.id.iv_main_lequal)
    ImageView imageView;

    private ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    final IUnReadMessageObserver observer = i -> {
        if (i > 0){
            setMessagePoint(i);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //状态栏设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //沉浸式
            //View view = window.getDecorView();
            //view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        //自适应新增按钮宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        llAdd.getLayoutParams().width = dm.widthPixels / 5;
        llAdd.setOnClickListener(v->startActivity(new Intent(this,PublishActivity.class)));

        //initLequal();

        initCheckPermission();

        initBottomBar();

        UserHelper.getInstance().connectRongCloud(this);

        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);

    }

    private void initLequal() {
        Glide.with(MainActivity.this)
                .asGif()
                .load(R.mipmap.lequal)
                .into(imageView);
        Handler handler = new Handler();
        handler.postDelayed(()->imageView.setImageResource(R.mipmap.add_main),3000);
    }

    private void initBottomBar() {
        String[] titles = getResources().getStringArray(R.array.title);

        for (int i = 0; i < titles.length; i++) {
            TabEntity tabEntity = new TabEntity(
                    titles[i],
                    selected.getResourceId(i, 0),
                    unSelected.getResourceId(i, 0)
            );
            customTabEntities.add(tabEntity);
        }

        fragments.add(new MainFragment());
        fragments.add(new CampusFragment());
        // 添加按钮
        fragments.add(new Fragment());
        fragments.add(new Fragment());
        fragments.add(new MineFragment());

        viewPager.setAdapter(new HomeAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(fragments.size());

        commonTabLayout.setTabData(customTabEntities);

        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(position == 2){
                    return;
                }
                if (position == 3){
                    commonTabLayout.hideMsg(3);
                    startActivity(new Intent(MainActivity.this,ConversationListActivity.class));
                    return;
                }
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 3){
                    commonTabLayout.hideMsg(3);
                    startActivity(new Intent(MainActivity.this,ConversationListActivity.class));
                }
            }
        });


    }

    private void initCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) {
                Toast.makeText(this,"提示",Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }else{
                    Log.d(TAG,"SDK问题");
                }
            }
        } else {
            //init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"授权成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * 注销已注册的未读消息数变化监听器。
         */
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
    }


    private void setMessagePoint(int i) {
        commonTabLayout.showMsg(3,i);
        commonTabLayout.setMsgMargin(3, -30, 5);
    }
}
