package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Campus;


public class CampusAdapter extends RecyclerView.Adapter<CampusAdapter.ViewHolder> {

    private List<Campus> list;
    private Context context;
    private int resourceId;
    private ISelfOnItemClickListener listener;
    private int selectedIndex = -1;

    public CampusAdapter(List<Campus> list, Context context, int resourceId) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
    }

    public CampusAdapter(List<Campus> list, Context context, int resourceId, ISelfOnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
        this.listener = listener;
    }

    public void setListener(ISelfOnItemClickListener listener) {
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
        Campus campus = list.get(position);
        holder.name.setText(campus.getName());
        holder.number.setText(campus.getNumber());
        Glide.with(context).load(campus.getAvatar()).into(holder.avatar);
        holder.content.setOnClickListener(v -> listener.itemClick(position));
        // Search布局
        if (holder.describe!=null) {
            holder.describe.setText(campus.getDescribe());
        }
        //Search布局
        if (holder.selected!=null) {
            if (selectedIndex == position) {
                holder.selected.setVisibility(View.VISIBLE);
            } else {
                holder.selected.setVisibility(View.INVISIBLE);
            }
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
        ImageView selected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tv_item_campus_name);
            this.number = itemView.findViewById(R.id.tv_item_campus_number);
            this.describe = itemView.findViewById(R.id.tv_item_campus_describe);
            this.avatar = itemView.findViewById(R.id.iv_item_campus_logo);
            this.content = itemView.findViewById(R.id.cv_item_campus_content);
            this.selected = itemView.findViewById(R.id.iv_search_selected);
        }
    }

    public interface ISelfOnItemClickListener{
        void itemClick(int position);
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public void setSelectedIndex(int index){
        this.selectedIndex = index;
    }
}
