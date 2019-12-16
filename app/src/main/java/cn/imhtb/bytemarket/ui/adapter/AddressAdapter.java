package cn.imhtb.bytemarket.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.List;

import cn.imhtb.bytemarket.R;
import cn.imhtb.bytemarket.bean.Address;

import static java.security.AccessController.getContext;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<Address> list;
    private Context context;
    private int resourceId;
    private ISelfOnItemClickListener listener;


    public AddressAdapter(List<Address> list, Context context, int resourceId,ISelfOnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.resourceId = resourceId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.content.setOnClickListener(v->
            new XPopup.Builder(context)
                    .hasShadowBg(false)
                    .atView(v)
                    .asAttachList(new String[]{"编辑", "删除"},
                            new int[]{},
                            (position, text) -> listener.itemClick(position,text))
                    .show()
        );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Address address = list.get(position);

        holder.namePhone.setCenterString(address.getName());
        holder.namePhone.setRightString(address.getPhone());
        holder.address.setText(address.getAddress());
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

    public interface ISelfOnItemClickListener{
        void itemClick(int position,String text);
    }
}
