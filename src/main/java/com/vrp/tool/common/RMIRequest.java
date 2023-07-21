package com.vrp.tool.common;

import java.io.Serializable;

public class RMIRequest <V> implements Serializable {
    private final SessionKey sessionKey;
    V Body;
    public RMIRequest(SessionKey sessionKey){
        this.sessionKey=sessionKey;
    }

    public SessionKey getSessionKey(){
        return sessionKey;
    }

    public V getBody() {
        return Body;
    }

    public void setBody(V body) {
        Body = body;
    }
}
