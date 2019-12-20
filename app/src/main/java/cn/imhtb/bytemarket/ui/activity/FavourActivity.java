package cn.imhtb.bytemarket.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Favour;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ICallBackHandler;
import cn.imhtb.bytemarket.common.OkHttpUtils;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.ui.adapter.FavourAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author PinTeh
 */
public class FavourActivity extends AppCompatActivity {


    private List<Favour> list = new ArrayList<>();

    private FavourAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favour);

        RecyclerView recyclerView = findViewById(R.id.rv_content);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new FavourAdapter(list, FavourActivity.this, R.layout.item_favour);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        //设置默认动画
        DefaultItemAnimator animator = new DefaultItemAnimator();
        //设置动画时间
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        recyclerView.setItemAnimator(animator);

        adapter.setListener(position -> {
            Integer id = list.get(position).getId();
            handleDelete(id, position);
        });


        ImageView back = findViewById(R.id.iv_top_back);
        TextView topTitle = findViewById(R.id.tv_top_desc);

        // 设置标题
        Intent intent = getIntent();
        String desc = intent.getStringExtra("desc");
        topTitle.setText(desc);

        String url = Api.URL_GET_COLLECT;
        if ("收藏".equals(desc)) {
            url = Api.URL_GET_COLLECT;
        } else if ("历史记录".equals(desc)) {
            url = Api.URL_GET_HISTORY;
        }

        getFavour(url);

        back.setOnClickListener(v -> finish());
    }

    private void handleDelete(Integer id, int position) {
        Executors.newCachedThreadPool().execute(() -> {
            OkHttpUtils.doDel(Api.TYPE_FAVOUR, Api.URL_FAVOUR_DELETE + "/" + id, FavourActivity.this, (ICallBackHandler<List<Favour>>) response -> {
                runOnUiThread(() -> {
                    if (response.isSuccess()) {
                        list.remove(position);
                        adapter.notifyItemRemoved(position);
                        //通知数据与界面重新绑定
                        adapter.notifyItemRangeChanged(position, list.size() - position);
                    }
                });
            });
        });
    }


    private void getFavour(String url) {

        User user = UserHelper.getInstance().getLoginUser(FavourActivity.this);
        if (user == null) {
            Toast.makeText(this, "请登录后尝试", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newCachedThreadPool().execute(() -> {
            OkHttpUtils.doGet(Api.TYPE_FAVOUR, url + "?uid=" + user.getId(), FavourActivity.this, (ICallBackHandler<List<Favour>>) r -> {
                runOnUiThread(() -> {
                    List<Favour> data = r.getData();
                    list.clear();
                    list.addAll(data);
                    adapter.notifyDataSetChanged();
                });
            }, false);
        });
    }

}
