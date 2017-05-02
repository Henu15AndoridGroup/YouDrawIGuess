package me.cizezsy.yourdrawiguess.model;


import com.google.gson.annotations.SerializedName;

public class WebSocketMessage {
    @SerializedName("type")
    private int type;
    @SerializedName("data")
    private String data;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
