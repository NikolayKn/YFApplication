
package com.example.yfapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class BucketWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private Context mcontext;

    private static final String TAG = "myLogs";

    BucketWebSocketListener(Context context) {
        this.mcontext = context;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "91f19onOpen");
        webSocket.send("Hello, it's the second module!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(TAG, "91f19onMessage");
        //mcontext.MainActivity.
        Log.d(TAG, "91f19output");
        // MainActivity.setText(text);
        // Data.addmessage(text);
        Data.getInstance().setVariableMessage(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d(TAG, "91f19 onMessageBytes");
        try {
            JSONObject json = new JSONObject(bytes.toString());
            Data.getInstance().JsonParser(json);
        } catch (JSONException e) {
            Log.e(TAG, "91f19 Unable to parse JSON");
            e.printStackTrace();
        }
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        Log.d(TAG, "91f19onClosing " + code + " / " + reason);
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }


    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d(TAG, "91f19onFailure  " + t.getMessage());
    }

}
