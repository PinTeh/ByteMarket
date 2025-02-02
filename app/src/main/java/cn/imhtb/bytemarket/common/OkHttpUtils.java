package cn.imhtb.bytemarket.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import cn.imhtb.bytemarket.ui.activity.FavourActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    private OkHttpUtils(){}

    public static<T> void doGet(Type type, String url, Context context,ICallBackHandler<T> callback,boolean sync) {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        Log.d("ttt",url);
        if (sync){
            try {
                Response response = call.execute();
                String responseString = Objects.requireNonNull(response.body()).string();
                Gson gson = new Gson();
                ServerResponse<T> serverResponse = gson.fromJson(responseString, type);
                if (serverResponse.isSuccess()) {
                    callback.onSuccess(serverResponse);
                }
            } catch (IOException e) {
                Log.e("[HTTP]:error", Objects.requireNonNull(e.getMessage()));
            }
        }else {
            call.enqueue(new JsonCallBack<T>(context, callback, type));
        }
    }

    public static<T> void doPost(String url, Map<String,Object> data, Context context, ICallBackHandler callback){
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");

        Gson gson = new Gson();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType,gson.toJson(data)))
                .build();

        Call call = client.newCall(request);

    }

    public static<T> void doDel(Type type, String url, Context context,ICallBackHandler<T> callback){
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Call call = client.newCall(request);

        call.enqueue(new JsonCallBack<>(context,callback,type));
    }

}
