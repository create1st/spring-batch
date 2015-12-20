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
import com.create.model.enums.TicketType;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class InternalTicketLatestCountUpdaterTest {
    private InternalTicketLatestCountUpdater updater;

    @Before
    public void setup() {
        updater = new InternalTicketLatestCountUpdater();
    }

    @Test
    public void testGetInitialMetricValue() throws Exception {
        // given
        // when
        // then
        assertEquals(0L, updater.getMetricValue());
    }

    @Test
    public void testGetEvaluateMetricValue() throws Exception {
        // given

        // when
        for (int i = 1; i < 200; i++) {
            final LocalDate date = LocalDate.of(2015, 1 + i / 28, i % 28 + 1);
            final Ticket ticket = new Ticket();
            ticket.setType(i % 2 == 0 ? TicketType.EXTERNAL : TicketType.INTERNAL);
            ticket.setDate(Date.valueOf(date));
            updater.updateWith(ticket);
        }

        // then
        assertEquals(50L, updater.getMetricValue());
    }

    @Test
    public void testGetMetricName() throws Exception {
        // given
        // when
        // then
        assertEquals("Latest internal ticket count", updater.getMetricName());
    }
}