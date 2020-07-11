package com.example.yfapplication;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BucketWebSocketListenerTrue extends WebSocketAdapter {
    private static final String TAG = "myLogs";

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        Log.d(TAG, "91f19 onConnected");
        JSONObject json = new JSONObject();
        JSONObject dataJson = new JSONObject();
        try {
            json.put("com", "InitModuleLcd");
            dataJson.put("moduleId", Data.getInstance().getBucket() + 1);
            json.put("data", dataJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "91f19 " + json.toString());
        websocket.sendText(json.toString());
    }

    @Override
    public void onTextMessage(WebSocket websocket, byte[] data) throws Exception {
        super.onTextMessage(websocket, data);
        String text = Arrays.toString(data);
        Log.d(TAG, "91f19 onTextMessageBytes");
        try {
            JSONObject json = new JSONObject(text);
            if (json.getJSONObject("data").getInt("ModuleId") == Data.getInstance().getBucket() + 1)
                Data.getInstance().JsonParser(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);
        Log.d(TAG, "91f19 onTextMessageString");
        Log.d(TAG, "91f19 " + text);
        try {
            JSONObject json = new JSONObject(text);
            if (json.getJSONObject("data").getInt("ModuleId") == Data.getInstance().getBucket() + 1)
                Data.getInstance().JsonParser(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        Log.d(TAG, "91f19 WebsSocket closed");
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        super.onTextMessageError(websocket, cause, data);
        String err = cause.getError().toString();
        Log.d(TAG, "Error occurred - " + err);
    }
}
