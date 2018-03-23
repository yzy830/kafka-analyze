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
import org.apache.kafka.clients.producer.internals.Sender;

/**
 * <p>
 *   底层的请求完成处理其，无论成功或者失败，都会调用这个。上层的{@link Callback}会被包装为一个{@code RequestCompletionHandler}，参考{@link Sender#sendProduceRequest}
 * </p>
 * 
 * A callback interface for attaching an action to be executed when a request is complete and the corresponding response
 * has been received. This handler will also be invoked if there is a disconnection while handling the request.
 */
public interface RequestCompletionHandler {

    public void onComplete(ClientResponse response);

}
