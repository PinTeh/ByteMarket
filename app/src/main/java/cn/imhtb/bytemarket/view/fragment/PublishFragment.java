package cn.imhtb.bytemarket.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.utils.PixelUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class PublishFragment extends Fragment {

    @BindView(R.id.ll_images_group)
    LinearLayout imageGroup;

    @BindView(R.id.ll_add_image)
    LinearLayout openAlbums;

    @BindView(R.id.tv_top_desc)
    TextView topDesc;

    private Context context;

    public PublishFragment() {
        // 自定义图片加载器
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        context = getActivity();

        topDesc.setText("发布商品");
        openAlbums.setOnClickListener(v->
            openPictureSelector()
        );
    }

    private void openPictureSelector() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(5)
                //.loadImageEngine(GlideEngine.createGlideEngine())
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
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                PixelUtils.dpToPx(context,80),
                PixelUtils.dpToPx(context,80));
        int dp = PixelUtils.dpToPx(context,10);
        params.setMargins(dp,0,0,0);
        imageView.setLayoutParams(params);

        return imageView;
    }

}
