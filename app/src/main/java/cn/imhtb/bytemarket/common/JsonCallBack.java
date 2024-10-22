package cn.imhtb.bytemarket.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonCallBack<T> implements Callback {

    private Type type;

    private Handler handler;

    private Context context;

    private ICallBackHandler<T> iCallBackHandler;

    public JsonCallBack(Context context, ICallBackHandler<T> iCallBackHandler, Type type) {
        handler = new Handler(Looper.getMainLooper());
        this.context = context;
        this.iCallBackHandler = iCallBackHandler;
        this.type = type;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        makeToast("HTTP请求失败");
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) {
        try {
            String responseString = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            ServerResponse<T> serverResponse = gson.fromJson(responseString, type);
            if (serverResponse.isSuccess()) {
                iCallBackHandler.onSuccess(serverResponse);
            } else {
                makeToast("服务端响应失败请求");
            }
        } catch (IOException e) {
            Log.e("[HTTP]:error", Objects.requireNonNull(e.getMessage()));
            makeToast("Gson类型转换异常");
        }
    }

    private void makeToast(String message) {
        Looper.prepare();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
