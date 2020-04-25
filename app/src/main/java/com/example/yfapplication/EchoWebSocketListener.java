package com.example.yfapplication;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public final class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private Context mcontext;

    EchoWebSocketListener(Context context){
        this.mcontext = context;
        //mcontext = context;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        webSocket.send("Hello, it's the second module!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        output("Receiving : " + text);


    }

    private void output(String text) {
    //mcontext.MainActivity.
        MainActivity.setText(text);

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
    }

}
