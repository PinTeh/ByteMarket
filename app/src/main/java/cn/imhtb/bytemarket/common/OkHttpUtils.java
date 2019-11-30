package cn.imhtb.bytemarket.common;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtils {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    private OkHttpUtils(){}

    public static<T> void doGet(Type type, String url, Context context,ICallBackHandler<T> callback){
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new JsonCallBack<T>(context,callback,type));
    }

    public static<T> void doPost(Type type, String url, String data, Context context,ICallBackHandler<T> callback){
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        Gson gson = new Gson();
        String json = gson.toJson(data);
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType,json))
                .build();

        Call call = client.newCall(request);

        call.enqueue(new JsonCallBack<T>(context,callback,type));
    }
}
