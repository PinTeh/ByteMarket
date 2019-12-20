package cn.imhtb.bytemarket.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.angmarch.views.NiceSpinner;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.bean.Campus;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.bean.MessageEvent;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonalActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.civ_personal_avatar)
    CircleImageView avatar;

    @BindView(R.id.tv_personal_username)
    TextView username;

    @BindView(R.id.tv_personal_description)
    EditText description;

    @BindView(R.id.tv_personal_nickname)
    EditText nickName;

    @BindView(R.id.toolbar_personal)
    Toolbar toolbar;

    @BindView(R.id.spinner_school)
    NiceSpinner spinner;


    private List<String> data;

    private int selectedPosition = -1;

    private String avatarNew;

    private Integer defaultSchoolIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v->finish());

        User loginUser = UserHelper.getInstance().getLoginUser(this);
        if (loginUser==null){
            Toast.makeText(this,"未登录",Toast.LENGTH_SHORT).show();
            return;
        }

        Glide.with(this).load(loginUser.getAvatar()).into(avatar);
        username.setText(loginUser.getUsername());
        nickName.setText(loginUser.getNickName());
        description.setText(loginUser.getDescription());

        data = new ArrayList<>();
        for (int i = 0; i < AppComponent.campusList.size(); i++) {
            Campus campus = AppComponent.campusList.get(i);
            data.add(campus.getName());
            if (loginUser.getSchoolName().equals(campus.getName())){
                defaultSchoolIndex = i;
            }
        }
        spinner.attachDataSource(data);

        if (defaultSchoolIndex!=null){
            spinner.setSelectedIndex(defaultSchoolIndex);
        }
        spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            selectedPosition = position;
        });
    }


    @OnClick({R.id.civ_personal_avatar,R.id.btn_personal_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_personal_avatar:{
                handleUpdateAvatar();
            } break;
            case R.id.btn_personal_save :{
                handleUpdateInfo();
            }break;
            default:
        }
    }

    private void handleUpdateInfo() {
        Integer campusId = -1;
        if (selectedPosition!=-1){
            Campus campus = AppComponent.campusList.get(selectedPosition);
            campusId = campus.getId();
        }
        final Integer cid = campusId;
        final String name = nickName.getText().toString();
        final String desc = description.getText().toString();

        Executors.newCachedThreadPool().execute(()-> {


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS).build();

            User loginUser = UserHelper.getInstance().getLoginUser(PersonalActivity.this);

            Map<String, Object> map = new HashMap<>();
            map.put("nickName", name);
            map.put("description", desc);
            if (cid != -1){
                map.put("schoolId", cid);
            }
            map.put("id", loginUser.getId());
            if (!TextUtils.isEmpty(avatarNew)){
                map.put("avatar", avatarNew);
            }

            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(Api.URL_USER_UPDATE)
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();

                ServerResponse<User> serverResponse = gson.fromJson(ret, new TypeToken<ServerResponse<User>>() {}.getType());
                if (serverResponse.isSuccess()){
                    Log.d("ttt", "handleUpdateInfo: " + JSON.toJSONString(serverResponse.getData()));
                    EventBus.getDefault().post(new MessageEvent("update:success", JSON.toJSONString(serverResponse.getData())));
                }else {
                    Log.d("ttt",ret);
                }

            } catch (IOException e) {

            }
        });
    }

    private void handleUpdateAvatar() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .enableCrop(true)
                .compress(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            List<LocalMedia> media = PictureSelector.obtainMultipleResult(data);
            if (media!=null && media.get(0).isCompressed()){
                LocalMedia localMedia = media.get(0);
                handleUploadAvatar(localMedia.getCompressPath());
            }
        }
    }

    public void handleUploadAvatar(String path){
        Executors.newCachedThreadPool().execute(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS).build();

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("file"
                    , ""
                    , RequestBody.create(MediaType.parse("image/*"), new File(path)));

            Request request = new Request.Builder()
                    .url(Api.URL_UPLOAD_IMAGE)
                    .post(builder.build())
                    .build();

            try {
                Response res = okHttpClient.newCall(request).execute();
                String ret =  Objects.requireNonNull(res.body()).string();
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(ret,ServerResponse.class);
                String p = serverResponse.getMsg();
                EventBus.getDefault().post(new MessageEvent("upload:success",p));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if ("upload:success".equals(event.getMessage())){
            if (!TextUtils.isEmpty(event.getData())){
                avatarNew = event.getData();
                Glide.with(this).load(avatarNew).into(avatar);
            }else {
                Toast.makeText(this,"上传异常",Toast.LENGTH_SHORT).show();
            }
        }

        if ("update:success".equals(event.getMessage())){
            UserHelper.getInstance().setAutoLogin(this,event.getData());
            Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
            EventBus.getDefault().postSticky(new MessageEvent("notice:reload"));
            finish();
        }
    }
}
