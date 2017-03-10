package me.cizezsy.yourdrawiguess.model;


public class Room {

    private Integer roomId;
    private Integer maxPeople;
    private Integer currPeople;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Integer getCurrPeople() {
        return currPeople;
    }

    public void setCurrPeople(Integer currPeople) {
        this.currPeople = currPeople;
    }
}
