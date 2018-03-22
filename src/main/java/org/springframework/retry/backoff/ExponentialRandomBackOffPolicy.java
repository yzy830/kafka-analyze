/*
 * Copyright 2006-2012 the original author or authors.
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

package org.springframework.retry.backoff;

import org.springframework.retry.RetryContext;

import java.util.Random;

/**
 * Implementation of {@link org.springframework.retry.backoff.ExponentialBackOffPolicy} that
 * chooses a random multiple of the interval.  The random multiple is selected based on
 * how many iterations have occurred.
 *
 * This has shown to at least be useful in testing scenarios where excessive contention is generated
 * by the test needing many retries.  In test, usually threads are started at the same time, and thus
 * stomp together onto the next interval.  Using this {@link BackOffPolicy} can help avoid that scenario.
 *
 * Example:
 *   initialInterval = 50
 *   multiplier      = 2.0
 *   maxInterval     = 3000
 *   numRetries      = 5
 *
 * {@link ExponentialBackOffPolicy} yields:           [50, 100, 200, 400, 800]
 *
 * {@link ExponentialRandomBackOffPolicy} may yield   [50, 100, 100, 100, 600]
 *                                               or   [50, 100, 150, 400, 800]
 * @author Jon Travis
 * @author Dave Syer
 */
public class ExponentialRandomBackOffPolicy extends ExponentialBackOffPolicy {
    /**
     * Returns a new instance of {@link org.springframework.retry.backoff.BackOffContext}, seeded with this
     * policies settings.
     */
    public BackOffContext start(RetryContext context) {
        return new ExponentialRandomBackOffContext(getInitialInterval(), getMultiplier(), getMaxInterval());
    }

    protected ExponentialBackOffPolicy newInstance() {
        return new ExponentialRandomBackOffPolicy();
    }

    static class ExponentialRandomBackOffContext extends ExponentialBackOffPolicy.ExponentialBackOffContext {
        private final Random r = new Random();

        public ExponentialRandomBackOffContext(long expSeed, double multiplier, long maxInterval) {
            super(expSeed, multiplier, maxInterval);
        }
        
        @Override
        public synchronized long getSleepAndIncrement() {
        	long next = super.getSleepAndIncrement();
        	next = (long)(next*(1 + r.nextFloat()*(getMultiplier()-1)));
        	return next;
        }

    }
}
