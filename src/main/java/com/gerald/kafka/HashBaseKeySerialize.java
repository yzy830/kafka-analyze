package com.gerald.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.Serializer;

public class HashBaseKeySerialize<T> implements Serializer<T> {
    private IntegerSerializer intSerializer = new IntegerSerializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if(data == null) {
            return null;
        }
        
        int hash = data.hashCode();
        
        return intSerializer.serialize(topic, hash);
    }

    @Override
    public void close() {
        intSerializer.close();
    }

}
