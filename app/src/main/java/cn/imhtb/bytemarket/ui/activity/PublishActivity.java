package cn.imhtb.bytemarket.ui.activity;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.base.BaseActivity;
import cn.imhtb.bytemarket.utils.PixelUtils;

public class PublishActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.ll_images_group)
    LinearLayout imageGroup;

    @BindView(R.id.ll_add_image)
    LinearLayout openAlbums;

    @BindView(R.id.tv_top_desc)
    TextView topDesc;

    @BindView(R.id.iv_top_back)
    ImageView topBack;

    @BindView(R.id.btn_publish_publish)
    Button btnPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        topDesc.setText("发布商品");
        openAlbums.setOnClickListener(this);
        topBack.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
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

            List<LocalMedia> mediaList = PictureSelector.obtainMultipleResult(data);

            if (mediaList==null){
                return;
            }
            for (LocalMedia media : mediaList) {
                imageGroup.addView(createImage(media.getPath()));
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add_image:
                openPictureSelector();
            break;
            case R.id.btn_publish_publish:
                Toast.makeText(this,"发布成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_top_back:
                finish();
                break;
            default:
                break;
        }
    }
}