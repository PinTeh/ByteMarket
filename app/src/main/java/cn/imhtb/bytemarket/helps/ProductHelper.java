package cn.imhtb.bytemarket.helps;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;

public class ProductHelper {

    private static ProductHelper instance;

    private ProductHelper(){}

    public static ProductHelper getInstance(){
        if (instance==null){
            synchronized (ProductHelper.class){
                if (instance==null){
                    instance = new ProductHelper();
                }
            }
        }
        return instance;
    }

    public boolean validate(Context context, String title, List<String> images, String description, String price) {

        boolean flag = true;

        if (images == null || images.size()<1) {
            Toast.makeText(context, "请上传至少一张商品图片", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "请输入商品标题", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(context, "请输入商品价格", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (null == description || description.length() < 15) {
            Toast.makeText(context, "请添加15字以上的商品描述哦", Toast.LENGTH_SHORT).show();
            flag = false;
        }

        return flag;
    }

}
