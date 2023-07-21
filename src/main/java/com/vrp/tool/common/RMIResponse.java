package com.vrp.tool.common;

import java.io.Serializable;

public class RMIResponse <V> implements Serializable {
    ClientHeaders clientHeaders;
    V body;

    String status;

    public RMIResponse(){}
    public  RMIResponse(V body,ClientHeaders clientHeaders,String status){
          this.body=body;
          this.clientHeaders=clientHeaders;
          this.status=status;
    }

    public ClientHeaders getClientHeaders() {
        return clientHeaders;
    }

    public V getBody() {
        return body;
    }

    public void setBody(V body){
        this.body=body;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }
}
