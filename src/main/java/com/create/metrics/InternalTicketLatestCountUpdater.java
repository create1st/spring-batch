/*
 * Copyright 2015 Sebastian Gil.
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
 *
 */

package com.create.metrics;

import com.create.model.Ticket;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Calculate how many of the latest tickets is coming from internal source in a thread safe way.
 */
public class InternalTicketLatestCountUpdater implements MetricUpdater<Ticket>, MetricProvider {

    private AtomicReference<InternalTicketLatestCount> internalTicketLatestCount = new AtomicReference<>(new InternalTicketLatestCount());

    @Override
    public void updateWith(final Ticket ticket) {
        internalTicketLatestCount.updateAndGet(value -> value.add(ticket.getDate(), ticket.getType()));
    }

    @Override
    public Object getMetricValue() {
        return internalTicketLatestCount.get().evaluate();
    }

    @Override
    public String getMetricName() {
        return "Latest internal ticket count";
    }


}
