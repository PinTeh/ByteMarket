package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.CampusEntity;
import cn.imhtb.bytemarket.bean.FavourEntity;
import cn.imhtb.bytemarket.ui.activity.DetailActivity;


public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.ViewHolder> {

    private List<CampusEntity> list;
    private Context context;
    private int resourceId;
    private ISelfOnItemClickListener listener;
    private int selectedIndex = -1;

    public CampusAdapter(List<CampusEntity> list, Context context, int resourceId,ISelfOnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId,parent,false);
        ViewHolder holder = new ViewHolder(view);

        //设置单选
        RadioButton radioButton= holder.radioButton;
        if (radioButton!=null){
            radioButton.setOnClickListener(v->{
                int position = holder.getAdapterPosition();
                selectedIndex = selectedIndex==position?-1:position;
                notifyDataSetChanged();
            });
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CampusEntity campusEntity = list.get(position);
        holder.name.setText(campusEntity.getName());
        holder.number.setText(campusEntity.getNumber());
        Glide.with(context).load(campusEntity.getAvatar()).into(holder.avatar);
        holder.content.setOnClickListener(v -> {
            listener.itemClick(position);
        });
        // 搜索布局控件
        if (holder.describe!=null) {
            holder.describe.setText(campusEntity.getDescribe());
        }
        RadioButton radioButton= holder.radioButton;
        if (radioButton!=null) {
            radioButton.setChecked(selectedIndex==position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView content;
        TextView name;
        TextView number;
        TextView describe;
        ImageView avatar;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tv_item_campus_name);
            this.number = itemView.findViewById(R.id.tv_item_campus_number);
            this.describe = itemView.findViewById(R.id.tv_item_campus_describe);
            this.avatar = itemView.findViewById(R.id.iv_item_campus_logo);
            this.content = itemView.findViewById(R.id.cv_item_campus_content);
            this.radioButton = itemView.findViewById(R.id.rb_search_item);
        }
    }

    public interface ISelfOnItemClickListener{
        void itemClick(int position);
    }
}
