package com.vrp.tool.common;

import java.io.Serializable;

public class SessionKey implements Serializable {
    private boolean connected;
    private String token;

    private volatile long lastaccessed;
    private volatile boolean exit;

    public SessionKey(String token,boolean connected,long time){
        this.connected=connected;
        this.token=token;
        lastaccessed=time;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setCoonected(boolean coonected) {
        this.connected = coonected;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLastaccessed() {
        return lastaccessed;
    }

    public void setLastaccessed(long lastaccessed) {
        this.lastaccessed = lastaccessed;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionKey that = (SessionKey) o;

        if (connected != that.connected) return false;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result = (connected ? 1 : 0);
        result = 31 * result + token.hashCode();
        return result;
    }
}
