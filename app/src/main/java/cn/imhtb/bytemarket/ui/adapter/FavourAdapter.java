package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.imhtb.bytemarket.bean.Goods;
import cn.imhtb.bytemarket.ui.activity.DetailActivity;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Favour;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * @author PinTeh
 */
public class FavourAdapter extends RecyclerView.Adapter<FavourAdapter.ViewHolder> {

    private List<Favour> list;
    private Context context;
    private int resourceId;
    private ISelfOnCancelClickListener listener;

    public FavourAdapter(List<Favour> list, Context context, int resourceId) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
    }

    public void setListener(ISelfOnCancelClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Favour favour = list.get(position);
        Goods goods = favour.getGoods();
        holder.title.setText(goods.getTitle());
        holder.price.setText(goods.getPrice().toEngineeringString());
        Glide.with(context).load(goods.getUser().getAvatar()).into(holder.avatar);
        Glide.with(context).load(goods.getCover()).into(holder.cover);
        holder.nickName.setText(goods.getUser().getName());

        holder.content.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("GOODS", JSON.toJSONString(goods));
            context.startActivity(intent);
        });

        holder.cancel.setOnClickListener(v -> {listener.cancelClick(position);});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout content;
        TextView title;
        TextView price;
        TextView cancel;
        CircleImageView avatar;
        ImageView cover;
        TextView nickName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_favour_goods_title);
            this.price = itemView.findViewById(R.id.tv_favour_goods_price);
            this.content = itemView.findViewById(R.id.ll_favour_item);
            this.cancel = itemView.findViewById(R.id.tv_favour_cancel);
            this.avatar = itemView.findViewById(R.id.civ_favour_user_avatar);
            this.nickName = itemView.findViewById(R.id.tv_favour_user_nike_name);
            this.cover = itemView.findViewById(R.id.iv_favour_goods_cover);
        }
    }

    public interface ISelfOnCancelClickListener{
        void cancelClick(int position);
    }
}
