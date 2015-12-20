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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Optional;

/**
 * Import {@link Ticket} item processor.
 */
public class TicketUpdateItemProcessor implements ItemProcessor<Ticket, Ticket> {
    private static final Logger log = LoggerFactory.getLogger(TicketUpdateItemProcessor.class);

    private final TicketRepository ticketRepository;

    public TicketUpdateItemProcessor(final TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket process(final Ticket item) throws Exception {
        log.debug("processing : {}", item);
        final Ticket ticket = Optional
                .ofNullable(ticketRepository.findByTag(item.getTag()))
                .map(i -> {
                    i.setContent(item.getContent());
                    return i;
                })
                .orElse(item);
        log.debug("using : {}", ticket);
        return ticket;
    }
}
