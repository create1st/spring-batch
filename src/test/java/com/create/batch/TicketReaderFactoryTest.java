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

package com.create.batch;

import com.create.model.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.core.io.ClassPathResource;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TicketReaderFactoryTest {

    private TicketReaderFactory factory = new TicketReaderFactory();

    @Test
    public void testCreateReaderAndReadCorrectData() throws Exception {
        // given
        final ExecutionContext executionContext = mock(ExecutionContext.class);
        final LocalDate date = LocalDate.of(2015, 12, 20);

        // when
        final ItemStreamReader<Ticket> reader = factory.createReader(new ClassPathResource("tickets.csv"));
        final Ticket ticket;

        try {
            reader.open(executionContext);
            ticket = reader.read();
        } finally {
            reader.close();
        }

        // then
        assertThat(ticket, notNullValue());
        assertEquals("Ticket_0", ticket.getTag());
        assertEquals(Date.valueOf(date), ticket.getDate());
        assertEquals("Test ticket", ticket.getContent());
    }

    @Test(expected = FlatFileParseException.class)
    public void testCreateReaderAndFailToRead() throws Exception {
        // given
        final ExecutionContext executionContext = mock(ExecutionContext.class);

        // when
        final ItemStreamReader<Ticket> reader = factory.createReader(new ClassPathResource("tickets-fail.csv"));

        try {
            reader.open(executionContext);
            System.out.println(reader.read());
        } finally {
            reader.close();
        }

        // then
    }


}