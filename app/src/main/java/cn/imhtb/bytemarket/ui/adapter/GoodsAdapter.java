package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.bean.GoodsEntity;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsHolder> {

    private Context context;
    private ISelfOnItemClickListener listener;
    private List<Goods> list;

    public GoodsAdapter(Context context, List<Goods> list,ISelfOnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoodsHolder(LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsHolder holder, final int position) {

        Goods goods = list.get(position);

        String images = goods.getImages();
        String[] imageArr = images.split(",");

        holder.price.setText((goods.getPrice().toEngineeringString()));
        holder.title.setText((goods.getTitle()));
        holder.username.setText((goods.getUser().getName()));
        Glide.with(context).load(goods.getUser().getAvatar()).into(holder.avatar);
        if (imageArr.length>0) {
            Glide.with(context).load(imageArr[0]).into(holder.image);
        }else {
            holder.image.setImageResource(R.mipmap.goods1);
        }
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
