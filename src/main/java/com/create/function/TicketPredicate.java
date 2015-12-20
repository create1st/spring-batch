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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.function.Predicate;

/**
 * Ticket predicates.
 */
public class TicketPredicate {

    /**
     * Check {@link Ticket} type.
     *
     * @param ticketType Ticket type.
     * @return True when {@link Ticket} type match provided ticket type.
     */
    public static Predicate<Ticket> hasType(final TicketType ticketType) {
        return ticket -> ticket.getType().equals(ticketType);
    }

    /**
     * Check if {@link Ticket} date is a Today date.
     *
     * @return True when {@link Ticket} is Today date.
     */
    public static Predicate<Ticket> hasTodayDate() {
        final LocalDate today = LocalDate.now();
        return ticket -> {
            final LocalDate date = Instant.ofEpochMilli(ticket.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            return date.isEqual(today);
        };
    }
}
