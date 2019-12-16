package cn.imhtb.bytemarket.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.utils.PixelUtils;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsHolder> {

    private Context context;
    private ISelfOnItemClickListener listener;
    private List<Goods> list;
    private Map<Integer,Integer> map;

    public GoodsAdapter(Context context, List<Goods> list,ISelfOnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        map = new HashMap<>();
    }

    @NonNull
    @Override
    public GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoodsHolder(LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsHolder holder, final int position) {

        final Goods goods = list.get(position);

        holder.image.requestLayout();

        String images = goods.getImages();
        String cover;
        if (images!=null){
            String[] imageArr = images.split(",");
            if (imageArr.length>0) {
                cover = imageArr[0];
                Glide.with(context).asBitmap().load(cover).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Integer height = map.get(position);
                        ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
                        if (height==null) {
                            float density = context.getResources().getDisplayMetrics().density;
                            Log.d("ttt", "onResourceReady: " + resource.getHeight() + ":" + resource.getWidth()+":"+density);
                            height = getAutoHeight(resource.getWidth(),resource.getHeight());

                            map.put(position,height);
                        }
                        layoutParams.height = height;
                        holder.image.setLayoutParams(layoutParams);
                        holder.image.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }else {
                holder.image.setImageResource(R.mipmap.goods1);
            }
        }

        holder.price.setText((goods.getPrice().toEngineeringString()));
        holder.title.setText((goods.getTitle()));
        holder.username.setText((goods.getUser().getName()));
        Glide.with(context).load(goods.getUser().getAvatar()).into(holder.avatar);


        holder.content.setOnClickListener(v -> listener.itemClick(position));

    }

    public int getAutoHeight(int width,int height){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        double w = dm.widthPixels / 2.0;
        double h = (w / width) * height;
        return (int)h;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class GoodsHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView price;
        ImageView avatar;
        TextView username;
        LinearLayout content;

        public GoodsHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.iv_goods);
            this.title = itemView.findViewById(R.id.tv_goods_title);
            this.price = itemView.findViewById(R.id.tv_goods_price);
            this.avatar = itemView.findViewById(R.id.iv_goods_author_avatar);
            this.username = itemView.findViewById(R.id.tv_goods_author_username);
            this.content = itemView.findViewById(R.id.ll_goods);
        }
    }

    public interface ISelfOnItemClickListener{
        void itemClick(int position);
    }

}
