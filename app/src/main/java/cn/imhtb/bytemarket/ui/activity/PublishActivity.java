package cn.imhtb.bytemarket.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.bean.User;
import cn.imhtb.bytemarket.common.Api;
import cn.imhtb.bytemarket.common.Constants;
import cn.imhtb.bytemarket.common.ServerResponse;
import cn.imhtb.bytemarket.helps.UserHelper;
import cn.imhtb.bytemarket.utils.PixelUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PublishActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.ll_images_group)
    LinearLayout imageGroup;

    @BindView(R.id.toolbar_publish)
    Toolbar toolbar;

    @BindView(R.id.spinner_publish_category)
    NiceSpinner spinner;

    @BindView(R.id.et_publish_title)
    EditText et_title;

    @BindView(R.id.et_publish_describe)
    EditText et_describe;

    @BindView(R.id.et_publish_price)
    EditText et_price;

    @BindView(R.id.btn_publish_publish)
    Button button;

    private List<LocalMedia> mediaList;

    private List<String> afterCompress = new LinkedList<>();

    private Integer categorySelectedId = Constants.DEFAULT_CATEFORY;

    private ThreeBounce threeBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v->finish());
        //final List<String> collect = AppComponent.categoryList.stream().map(Category::getName).collect(Collectors.toList());
        List<Category> categoryList = AppComponent.categoryList;
        spinner.attachDataSource(categoryList);
        spinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            Category item = (Category)spinner.getItemAtPosition(position);
            categorySelectedId = item.getId();
        });

        threeBounce = new ThreeBounce();
        threeBounce.setBounds(0, 20, 100, 100);
        //noinspection deprecation
        threeBounce.setColor(getResources().getColor(R.color.colorFontLightDark));
    }

    private void openPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(5)
                .selectionMode(PictureConfig.MULTIPLE)
                .compress(true)
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

            File file = new File("/sdcard/byteMarket");
            if (!file.exists()&&!file.mkdirs()){
                return;
            }

            Luban.with(this)
                    .load(mediaList.stream().map(LocalMedia::getPath).collect(Collectors.toList()))
                    .ignoreBy(100)
                    .setTargetDir(file.getPath())
                    .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            afterCompress.add(file.getPath());
                            Log.d("ttt", "onSuccess: 压缩成功" + file.getPath());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("ttt","error" + e);
                        }
                    }).launch();
        }
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


    /*
    private void uploadImages(List<LocalMedia> localMedias){
       Executors.newCachedThreadPool().execute(()->{
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
                    Log.d("ttt", "上传失败 " + e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.d("ttt", response.body().string());
                }
            });
        });
    }
    */

    public String handleUploadImages(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS).build();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String compress : afterCompress) {
            builder.addFormDataPart("file"
                    , ""
                    , RequestBody.create(MediaType.parse("image/*"), new File(compress)));
        }

        Request request = new Request.Builder()
                .url(Api.URL_UPLOAD_IMAGES)
                .post(builder.build())
                .build();

        try {
            Response res = okHttpClient.newCall(request).execute();
            String ret =  Objects.requireNonNull(res.body()).string();
            Gson gson = new Gson();
            ServerResponse serverResponse = gson.fromJson(ret,ServerResponse.class);
            return serverResponse.getMsg();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void handlePublish(){
        //判断登录
        boolean login = UserHelper.getInstance().isLogin(this);
        if (!login){
            Toast.makeText(this,"请登录后再发布",Toast.LENGTH_SHORT).show();
            return;
        }

        //正在发布
        button.setCompoundDrawables(null, threeBounce, null, null);
        button.setText("");
        button.setEnabled(false);
        threeBounce.start();

        Executors.newCachedThreadPool().execute(()->{

            String images = handleUploadImages();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120,TimeUnit.SECONDS).build();

            User loginUser = UserHelper.getInstance().getLoginUser(PublishActivity.this);

            Map<String,Object> map = new HashMap<>();
            map.put("title",et_title.getText().toString());
            map.put("description",et_describe.getText().toString());
            map.put("price",et_price.getText().toString());
            map.put("images",images);
            map.put("userId", loginUser.getId());
            map.put("categoryId", categorySelectedId);
            map.put("schoolId", loginUser.getSchoolId());


            Gson gson = new Gson();
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody body = RequestBody.create(type, gson.toJson(map));

            Request request = new Request.Builder()
                    .url(Api.URL_ADD_PRODUCT)
                    .post(body)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                String ret = Objects.requireNonNull(response.body()).string();
                ServerResponse<Goods> serverResponse = gson.fromJson(ret,Api.TYPE_SINGLE_GOODS);
                if (serverResponse.isSuccess()) {
                    Log.d("ttt","发布成功");
                    Intent intent = new Intent(PublishActivity.this, DetailActivity.class);
                    intent.putExtra("GOODS",gson.toJson(serverResponse.getData()));
                    startActivity(intent);
                    Looper.prepare();
                    Toast.makeText(PublishActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    finish();
                } else {
                    Looper.prepare();
                    Toast.makeText(PublishActivity.this,serverResponse.getMsg(),Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    Log.d(TAG, "发布失败" + ret);
                }
            } catch (IOException e) {
                Log.e(TAG,"发布异常" + e);
            }
        });
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


    @Override
    protected void onResume() {
        super.onResume();
        //threeBounce.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        threeBounce.stop();
    }
}
