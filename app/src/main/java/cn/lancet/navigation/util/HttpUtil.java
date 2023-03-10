package cn.lancet.navigation.util;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http 工具类
 */
public class HttpUtil {

    public static void post(String requestUrl, String accessToken, String params) {

        String url = requestUrl + "?access_token=" + accessToken;

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        RequestBody body = RequestBody.create(params, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Connection", "Keep-Alive")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("BDAI onFailure  ", "" + e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("BDAI onResponse  ", "" + response.body().string());
                }
            }
        });

    }
}
