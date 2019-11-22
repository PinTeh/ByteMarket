//package cn.imhtb.bytemarket.utils;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.core.graphics.drawable.RoundedBitmapDrawable;
//import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.luck.picture.lib.engine.ImageEngine;
//
//public class GlideEngine implements ImageEngine {
//
//    @Override
//    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
//        Glide.with(context).load(url).into(imageView);
//    }
//
//    @Override
//    public void loadFolderAsBitmapImage(@NonNull Context context, @NonNull String url,
//                                        @NonNull ImageView imageView, int placeholderId) {
//        Glide.with(context)
//                .asBitmap()
//                .override(180, 180)
//                .centerCrop()
//                .sizeMultiplier(0.5f)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(placeholderId)
//                .load(url)
//                .into(new BitmapImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.
//                                        create(context.getResources(), resource);
//                        circularBitmapDrawable.setCornerRadius(8);
//                        imageView.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
//    }
//
//
//    @Override
//    public void loadAsGifImage(@NonNull Context context, @NonNull String url,
//                               @NonNull ImageView imageView) {
//        Glide.with(context)
//                .asGif()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .priority(Priority.HIGH)
//                .load(url)
//                .into(imageView);
//    }
//
//    @Override
//    public void loadAsBitmapGridImage(@NonNull Context context, @NonNull String url,
//                                      @NonNull ImageView imageView, int placeholderId) {
//        Glide.with(context)
//                .asBitmap()
//                .override(200, 200)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(placeholderId)
//                .load(url)
//                .into(imageView);
//    }
//
//
//    private GlideEngine() {
//    }
//
//    private static GlideEngine instance;
//
//    public static GlideEngine createGlideEngine() {
//        if (null == instance) {
//            synchronized (GlideEngine.class) {
//                if (null == instance) {
//                    instance = new GlideEngine();
//                }
//            }
//        }
//        return instance;
//    }
//}
