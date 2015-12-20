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

package com.create.application.configuration;

import com.create.batch.FilterItemReaderAdapter;
import com.create.batch.TicketImportJobExecutionListener;
import com.create.batch.TicketReaderFactory;
import com.create.batch.processor.MetricItemProcessor;
import com.create.batch.processor.TicketUpdateItemProcessor;
import com.create.function.TicketPredicate;
import com.create.metrics.InternalTicketLatestCountUpdater;
import com.create.metrics.MetricProvider;
import com.create.metrics.TicketCounterUpdater;
import com.create.model.Ticket;
import com.create.model.enums.TicketType;
import com.create.repository.TicketRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.create.function.TicketPredicate.hasType;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public TicketReaderFactory ticketReaderFactory() {
        return new TicketReaderFactory();
    }

    @Bean
    @Autowired
    public ItemReader<Ticket> ticketReader(final TicketReaderFactory ticketReaderFactory,
                                           final @Value("${ticket.file}") Resource tickets) {
        return new FilterItemReaderAdapter<>(ticketReaderFactory.createReader(tickets), TicketPredicate.hasTodayDate());
    }

    @Bean
    @Autowired
    public ItemWriter<Ticket> ticketWriter(final TicketRepository repository) {
        final RepositoryItemWriter<Ticket> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("saveAndFlush");
        return writer;
    }

    @Bean
    public TicketCounterUpdater sumEvaluator() {
        return new TicketCounterUpdater();
    }

    @Bean
    public InternalTicketLatestCountUpdater internalTicketLatestCountUpdater() {
        return new InternalTicketLatestCountUpdater();
    }

    @Bean
    @Autowired
    public ItemProcessor<Ticket, Ticket> importTicketProcessor(final TicketRepository ticketRepository,
                                                               final @Value("${ticket.metrics.count}") String countTicketType,
                                                               final TicketCounterUpdater ticketCounterUpdater,
                                                               final InternalTicketLatestCountUpdater internalTicketLatestCountUpdater) {
        final List<ItemProcessor<Ticket, Ticket>> delegates = Stream.of(
                new TicketUpdateItemProcessor(ticketRepository),
                new MetricItemProcessor<>(hasType(TicketType.valueOf(countTicketType)), ticketCounterUpdater),
                new MetricItemProcessor<>(ticket -> true, internalTicketLatestCountUpdater))
                .collect(Collectors.toList());
        final CompositeItemProcessor<Ticket, Ticket> processor = new CompositeItemProcessor<>();
        processor.setDelegates(delegates);
        return processor;
    }

    @Bean
    @Autowired
    public TicketImportJobExecutionListener ticketJobExecutionListener(final TicketCounterUpdater ticketCounterUpdater,
                                                                       final InternalTicketLatestCountUpdater internalTicketLatestCountUpdater) {
        final List<MetricProvider> metrics = Stream.of(
                ticketCounterUpdater,
                internalTicketLatestCountUpdater)
                .collect(Collectors.toList());
        return new TicketImportJobExecutionListener(metrics);
    }

    @Bean
    @Autowired
    public Job importTicketsJob(final JobBuilderFactory jobs,
                                final Step importTicketStep,
                                final TicketImportJobExecutionListener ticketImportJobExecutionListener) {
        return jobs.get("importTicketsJob")
                .incrementer(new RunIdIncrementer())
                .listener(ticketImportJobExecutionListener)
                .flow(importTicketStep)
                .end()
                .build();
    }

    @Bean
    @Autowired
    public Step importTicketStep(final StepBuilderFactory stepBuilderFactory,
                                 final @Value("${ticket.chunk.size}") int chunkSize,
                                 final ItemReader<Ticket> ticketReader,
                                 final ItemWriter<Ticket> ticketWriter,
                                 final ItemProcessor<Ticket, Ticket> importTicketProcessor) {
        return stepBuilderFactory.get("importTicketStep")
                .<Ticket, Ticket>chunk(chunkSize)
                .reader(ticketReader)
                .processor(importTicketProcessor)
                .writer(ticketWriter)
                .build();
    }
}
