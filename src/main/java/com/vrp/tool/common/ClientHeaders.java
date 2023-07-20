package com.vrp.tool.common;

import java.io.Serializable;

public class ClientHeaders implements Serializable {
    private String IP;
    private long l;

    public ClientHeaders(String IP,long l){
        this.IP=IP;
        this.l=l;
    }

    public String getIP() {
        return IP;
    }

    public long getL() {
        return l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientHeaders that = (ClientHeaders) o;

        if (l != that.l) return false;
        return IP.equals(that.IP);
    }

    @Override
    public int hashCode() {
        int result = IP.hashCode();
        result = 31 * result + (int) (l ^ (l >>> 32));
        return result;
    }
}
