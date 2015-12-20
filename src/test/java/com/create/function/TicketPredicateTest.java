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

package com.create.function;

import com.create.model.Ticket;
import com.create.model.enums.TicketType;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TicketPredicateTest {

    @Test
    public void testHasType() throws Exception {
        // given
        final Ticket ticket = new Ticket();
        ticket.setType(TicketType.EXTERNAL);

        // when

        // then
        assertTrue(TicketPredicate.hasType(TicketType.EXTERNAL).test(ticket));
    }

    @Test
    public void testDoesNotHaveAName() throws Exception {
        // given
        final Ticket ticket = new Ticket();
        ticket.setType(TicketType.INTERNAL);

        // when

        // then
        assertFalse(TicketPredicate.hasType(TicketType.EXTERNAL).test(ticket));
    }

    @Test
    public void testHasTodayDate() throws Exception {
        // given
        final Ticket ticket = new Ticket();
        final LocalDate date = LocalDate.now();
        ticket.setDate(Date.valueOf(date));

        // when

        // then
        assertTrue(TicketPredicate.hasTodayDate().test(ticket));
    }

    @Test
    public void testHasNotATodayDate() throws Exception {
        // given
        final Ticket ticket = new Ticket();
        final LocalDate date = LocalDate.of(2015, 12, 19);
        ticket.setDate(Date.valueOf(date));

        // when

        // then
        assertFalse(TicketPredicate.hasTodayDate().test(ticket));
    }
}