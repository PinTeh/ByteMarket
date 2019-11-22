package cn.imhtb.bytemarket.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.imhtb.bytemarket.DetailActivity;
import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.entity.FavourEntity;


public class FavourAdapter extends RecyclerView.Adapter<FavourAdapter.ViewHolder> {

    private List<FavourEntity> list;
    private Context context;
    private int resourceId;

    public FavourAdapter(List<FavourEntity> list, Context context, int resourceId) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        FavourEntity favourEntity = list.get(position);
        holder.title.setText(favourEntity.getTitle());
        holder.price.setText(favourEntity.getPrice().toEngineeringString());

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("desc",position + "");
                context.startActivity(intent);
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cancel.setText("收藏");
            }
        });

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_favour_title);
            this.price = itemView.findViewById(R.id.tv_favour_price);
            this.content = itemView.findViewById(R.id.ll_favour_item);
            this.cancel = itemView.findViewById(R.id.tv_favour_cancel_collect);
        }
    }
}
