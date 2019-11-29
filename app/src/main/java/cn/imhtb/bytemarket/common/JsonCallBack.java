package cn.imhtb.bytemarket.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.imhtb.bytemarket.bean.UserEntity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonCallBack implements Callback {

    private Handler handler;

    private Context context;

    private ICallBackHandler iCallBackHandler;

    public JsonCallBack(Context context,ICallBackHandler iCallBackHandler){
        handler = new Handler(Looper.getMainLooper());
        this.context = context;
        this.iCallBackHandler = iCallBackHandler;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        handler.post(()->{
            Toast.makeText(context,"[http request error]",Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        handler.post(()->{
            try {
                ServerResponse<UserEntity> serverResponse = JSON.parseObject(response.body().string(),new TypeReference<ServerResponse<UserEntity>>(){});
                if (serverResponse.isSuccess()){
                    iCallBackHandler.success(serverResponse);
                }else {
                    Toast.makeText(context,"[http request error]",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
