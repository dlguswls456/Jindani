package com.chomedicine.jindani.network;

public interface FirebaseCallback<T> {
    void onCallback(T value);
}