package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.entity.GoodsEntity;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsHolder> {

    private Context context;
    private ISelfOnItemClickListener listener;
    private List<GoodsEntity> list;

    public GoodsAdapter(Context context, List<GoodsEntity> list,ISelfOnItemClickListener listener) {
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
        holder.avatar.setImageResource(R.mipmap.avatar);
        holder.image.setImageResource(list.get(position).getImageId());
        holder.price.setText((list.get(position).getPrice().toEngineeringString()));
        holder.title.setText((list.get(position).getTitle()));
        holder.username.setText((list.get(position).getAuthor().getUsername()));

        holder.content.setOnClickListener(v -> {
            //实现方式一
            /*
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);
             */
            listener.itemClick(position);
        });

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
