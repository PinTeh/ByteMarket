package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;

import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.AddressEntity;
import cn.imhtb.bytemarket.bean.FavourEntity;
import cn.imhtb.bytemarket.ui.activity.DetailActivity;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<AddressEntity> list;
    private Context context;
    private int resourceId;

    public AddressAdapter(List<AddressEntity> list, Context context, int resourceId) {
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
        AddressEntity addressEntity = list.get(position);

        holder.namePhone.setCenterString(addressEntity.getName());
        holder.namePhone.setRightString(addressEntity.getPhone());
        holder.address.setText(addressEntity.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout content;
        SuperTextView namePhone;
        TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.namePhone = itemView.findViewById(R.id.stv_address_name_phone);
            this.address = itemView.findViewById(R.id.tv_address_address);
            this.content = itemView.findViewById(R.id.ll_address_content);
        }
    }
}
