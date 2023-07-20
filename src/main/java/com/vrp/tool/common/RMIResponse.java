package com.vrp.tool.common;

import java.io.Serializable;

public class RMIResponse <V> implements Serializable {
    ClientHeaders clientHeaders;
    V body;

    String status;
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

    public String getStatus() {
        return status;
    }
}
