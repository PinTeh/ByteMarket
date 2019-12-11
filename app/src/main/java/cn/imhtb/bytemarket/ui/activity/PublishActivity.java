package cn.imhtb.bytemarket.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.app.AppComponent;
import cn.imhtb.bytemarket.base.BaseActivity;
import cn.imhtb.bytemarket.bean.Category;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.utils.PixelUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.ll_images_group)
    LinearLayout imageGroup;

    @BindView(R.id.toolbar_publish)
    Toolbar toolbar;

    @BindView(R.id.spinner_publish_category)
    Spinner spinner;

    private List<LocalMedia> mediaList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v->finish());
        List<String> collect = AppComponent.categoryList.stream().map(Category::getName).collect(Collectors.toList());

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,collect);
        spinner.setAdapter(adapter);
    }

    private void openPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(5)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            mediaList = PictureSelector.obtainMultipleResult(data);

            if (mediaList==null){
                return;
            }
            for (LocalMedia media : mediaList) {
                imageGroup.addView(createImage(media.getPath()));
            }
        }
    }

    //尝试从布局中获取视图
    private LinearLayout createLinearLayout(){
        LinearLayout viewById = findViewById(R.id.item_picture);
        return (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_picture,viewById,true);
    }

    private ImageView createImage(String path){
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                PixelUtils.dpToPx(this,80),
                PixelUtils.dpToPx(this,80));
        int dp = PixelUtils.dpToPx(this,10);
        params.setMargins(dp,0,0,0);
        imageView.setLayoutParams(params);

        return imageView;
    }

    private void handlePublish_bak() {

        LocalMedia media = mediaList.get(0);
        String imagePath = media.getPath();
        Executors.newCachedThreadPool().execute(()->{
            OkHttpClient okHttpClient = new OkHttpClient();
            File file = new File(imagePath);


            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "", body)
                    .build();

            Request request = new Request.Builder()
                    .url(Api.URL_UPLOAD_IMAGES)
                    .post(requestBody)
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.d("ttt", "上传失败 "+ e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("ttt",response.body().string());
                }
            });

        });
    }

    private void handlePublish() {
        uploadImages(mediaList);
    }

    private void uploadImages(List<LocalMedia> localMedias){
        new Thread(()->{
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20,TimeUnit.SECONDS).build();

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            localMedias.forEach(v -> {
                builder.addFormDataPart("file"
                        , ""
                        , RequestBody.create(MediaType.parse("image/*"), new File(v.getPath())));
            });

            Request request = new Request.Builder()
                    .url(Api.URL_UPLOAD_IMAGES)
                    .post(builder.build())
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                    Log.d("ttt", "上传失败 " + e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("ttt", response.body().string());
                }
            });
        }).start();
    }

    @OnClick({R.id.ll_add_image,R.id.btn_publish_publish})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add_image:
                openPictureSelector();
            break;
            case R.id.btn_publish_publish:
                handlePublish();
                break;
            default:
                break;
        }
    }


}
