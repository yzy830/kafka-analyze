/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.kafka.listener;

/**
 * Marker interface for all listeners. While the abstract container will verify the
 * listener implements this interface, concrete container implementations will further
 * validate that the listener is one supported by that container.
 *
 * @param <T> the type handled by the listener.
 *
 * @author Gary Russell
 * @since 1.1
 *
 */
public interface KafkaDataListener<T> {

}
