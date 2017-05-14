package me.cizezsy.yourdrawiguess.model;


import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GameMessage {
    @SerializedName("type")
    private int type;
    @SerializedName("data")
    private JsonElement data;
    @SerializedName("time")
    private Date time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
