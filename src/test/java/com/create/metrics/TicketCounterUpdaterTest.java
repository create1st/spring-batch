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
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TicketCounterUpdaterTest {

    private TicketCounterUpdater updater;

    @Before
    public void setup() {
        updater = new TicketCounterUpdater();
    }

    @Test
    public void testGetInitialMetricValue() throws Exception {
        // given
        // when
        // then
        assertEquals(BigDecimal.ZERO, updater.getMetricValue());
    }

    @Test
    public void testGetEvaluateMetricValue() throws Exception {
        // given
        final Ticket ticket = new Ticket();

        // when
        updater.updateWith(ticket);
        updater.updateWith(ticket);

        // then
        assertEquals(BigDecimal.valueOf(2), updater.getMetricValue());
    }

    @Test
    public void testGetMetricName() throws Exception {
        // given
        // when
        // then
        assertEquals("Count", updater.getMetricName());
    }
}