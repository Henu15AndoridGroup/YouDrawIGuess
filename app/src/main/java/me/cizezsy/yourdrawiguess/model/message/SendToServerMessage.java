package me.cizezsy.yourdrawiguess.model.message;

import com.google.gson.annotations.SerializedName;

public class SendToServerMessage<T> {

    private Type type;
    private T data;

    public SendToServerMessage(Type type, T data) {
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
        MESSAGE,
        @SerializedName("3")
        PHRASE,
    }


}
