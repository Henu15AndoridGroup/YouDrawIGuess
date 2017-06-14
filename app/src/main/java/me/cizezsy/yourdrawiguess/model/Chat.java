package me.cizezsy.yourdrawiguess.model;

public class Chat {
    private String username;
    private String content;

    private transient Type type = Type.USER;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        SYSTEM, USER
    }
}
