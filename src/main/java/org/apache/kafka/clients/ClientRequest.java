/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.kafka.clients;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.common.protocol.ApiKeys;
import org.apache.kafka.common.requests.AbstractRequest;
import org.apache.kafka.common.requests.ProduceRequest;
import org.apache.kafka.common.requests.RequestHeader;

/**
 * <p>
 *   统一封装客户端请求，其中{@link #requestBuilder}是策略模式，将具体请求的构建委托给具体上层业务。例如，在KafkaProducer中，客户请求使用
 *   {@link ProduceRequest#Builder}
 * </p>
 * 
 * <p>
 *   {@code ClientRequest}保存了统一的元数据信息：destination(nodeId)、correlationId(请求需要)、clientId(客户端id)、
 *   expectResponse(是否需要响应)、call(响应回调，会包装上层的{@link Callback})
 * </p>
 * 
 * A request being sent to the server. This holds both the network send as well as the client-level metadata.
 */
public final class ClientRequest {

    private final String destination;
    private final AbstractRequest.Builder<?> requestBuilder;
    private final int correlationId;
    private final String clientId;
    private final long createdTimeMs;
    private final boolean expectResponse;
    private final RequestCompletionHandler callback;

    /**
     * @param destination The brokerId to send the request to
     * @param requestBuilder The builder for the request to make
     * @param correlationId The correlation id for this client request
     * @param clientId The client ID to use for the header
     * @param createdTimeMs The unix timestamp in milliseconds for the time at which this request was created.
     * @param expectResponse Should we expect a response message or is this request complete once it is sent?
     * @param callback A callback to execute when the response has been received (or null if no callback is necessary)
     */
    public ClientRequest(String destination,
                         AbstractRequest.Builder<?> requestBuilder,
                         int correlationId,
                         String clientId,
                         long createdTimeMs,
                         boolean expectResponse,
                         RequestCompletionHandler callback) {
        this.destination = destination;
        this.requestBuilder = requestBuilder;
        this.correlationId = correlationId;
        this.clientId = clientId;
        this.createdTimeMs = createdTimeMs;
        this.expectResponse = expectResponse;
        this.callback = callback;
    }

    @Override
    public String toString() {
        return "ClientRequest(expectResponse=" + expectResponse +
            ", callback=" + callback +
            ", destination=" + destination +
            ", correlationId=" + correlationId +
            ", clientId=" + clientId +
            ", createdTimeMs=" + createdTimeMs +
            ", requestBuilder=" + requestBuilder +
            ")";
    }

    public boolean expectResponse() {
        return expectResponse;
    }

    public ApiKeys apiKey() {
        return requestBuilder.apiKey();
    }

    public RequestHeader makeHeader() {
        return new RequestHeader(requestBuilder.apiKey().id,
                requestBuilder.version(), clientId, correlationId);
    }

    public AbstractRequest.Builder<?> requestBuilder() {
        return requestBuilder;
    }

    public String destination() {
        return destination;
    }

    public RequestCompletionHandler callback() {
        return callback;
    }

    public long createdTimeMs() {
        return createdTimeMs;
    }

    public int correlationId() {
        return correlationId;
    }
}
