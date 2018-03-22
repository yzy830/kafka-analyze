package com.gerald.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MyMessage {
    private final String msgId;
    
    private final String msgBody;
    
    @JsonCreator
    public MyMessage(@JsonProperty("msgId") String msgId,
                     @JsonProperty("msgBody") String msgBody) {
        this.msgId = msgId;
        this.msgBody = msgBody;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getMsgBody() {
        return msgBody;
    }
    
    @Override
    public String toString() {
        return "msgId = " + msgId + ", msgBody = " + msgBody;
    }
}
