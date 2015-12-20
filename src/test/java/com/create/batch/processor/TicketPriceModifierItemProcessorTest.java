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

package com.create.batch.processor;

import com.create.model.Ticket;
import com.create.repository.TicketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketPriceModifierItemProcessorTest {

    public static final String CONTENT = "content";
    public static final String TAG = "tag";
    @Mock
    private TicketRepository ticketRepository;

    private TicketUpdateItemProcessor processor;

    @Before
    public void setup() {
        processor = new TicketUpdateItemProcessor(ticketRepository);
    }

    @Test
    public void testProcessWithoutUpdate() throws Exception {
        // given
        final Ticket ticket = new Ticket();
        ticket.setContent(CONTENT);
        ticket.setTag(TAG);
        when(ticketRepository.findByTag(TAG)).thenReturn(null);

        // when
        final Ticket processedTicket = processor.process(ticket);

        // then
        assertEquals(CONTENT, processedTicket.getContent());
    }

    @Test
    public void testProcessWithUpdate() throws Exception {
        // given
        final Ticket ticket = new Ticket();
        ticket.setContent(CONTENT);
        ticket.setTag(TAG);
        final Ticket ticketOld = new Ticket();
        ticketOld.setContent("old content");
        when(ticketRepository.findByTag(TAG)).thenReturn(ticketOld);

        // when
        final Ticket processedTicket = processor.process(ticket);

        // then
        assertEquals(processedTicket, ticketOld);
        assertEquals(CONTENT, processedTicket.getContent());
    }
}