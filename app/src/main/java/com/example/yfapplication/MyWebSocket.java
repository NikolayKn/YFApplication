package com.example.yfapplication;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Request;
import okhttp3.WebSocket;
import okio.ByteString;

public class MyWebSocket implements WebSocket {
    @Override
    public void cancel() {

    }

    @Override
    public boolean close(int i, @Nullable String s) {
        return false;
    }

    @Override
    public long queueSize() {
        return 0;
    }

    @NotNull
    @Override
    public Request request() {
        return null;
    }

    @Override
    public boolean send(@NotNull String s) {
        return false;
    }

    @Override
    public boolean send(@NotNull ByteString byteString) {
        return false;
    }
}
