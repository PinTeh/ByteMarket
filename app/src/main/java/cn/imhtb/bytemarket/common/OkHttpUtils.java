package cn.imhtb.bytemarket.common;

import android.content.Context;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkHttpUtils {

    private static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    private OkHttpUtils(){}

    public static void doGet(Context context, String url, ICallBackHandler callback){
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new JsonCallBack(context,callback));
    }

}
