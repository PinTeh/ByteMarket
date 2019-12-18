package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Goods;

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

        String images = goods.getImages();
        String cover;
        if (images!=null){
            String[] imageArr = images.split(",");
            if (imageArr.length>0) {
                cover = imageArr[0];

                Integer height = map.get(position);
                ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
                if (height == null) {
                    height = new Random().nextInt(3) * 50 + 400;
                    map.put(position,height);
                }
                layoutParams.height = height;
                holder.image.setLayoutParams(layoutParams);

                Glide.with(context).load(cover).into(holder.image);
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
