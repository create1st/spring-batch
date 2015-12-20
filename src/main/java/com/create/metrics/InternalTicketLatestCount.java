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


import com.create.model.enums.TicketType;

import java.util.Date;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Latest internal tickets counter. Evaluates how many of latest 100 tickets is coming from internal source.
 */
public class InternalTicketLatestCount implements MetricEvaluator<Long> {
    private static final int MAX_ITEMS = 100;

    private final NavigableMap<Date, TicketType> latest = new ConcurrentSkipListMap<>();

    public InternalTicketLatestCount add(final Date date, final TicketType ticketType) {
        if (latest.size() < MAX_ITEMS) {
            latest.put(date, ticketType);
        } else if (latest.firstKey().before(date)) {
            latest.remove(latest.firstKey());
            latest.put(date, ticketType);
        }

        return this;
    }

    @Override
    public Long evaluate() {
        return latest.values().stream()
                .filter(TicketType.INTERNAL::equals)
                .map(ticket -> 1L)
                .reduce(0L, Math::addExact);
    }
}
