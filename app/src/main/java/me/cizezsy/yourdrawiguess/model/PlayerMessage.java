package me.cizezsy.yourdrawiguess.model;

import com.google.gson.annotations.SerializedName;

public class PlayerMessage<T> {

    private Type type;
    private T data;

    public PlayerMessage(Type type, T data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum Type {
        @SerializedName("1")
        DRAW,
        @SerializedName("2")
        MESSAGE
    }


}
