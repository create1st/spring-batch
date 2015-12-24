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

import org.apache.commons.logging.impl.SLF4JLog;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.xml.LoggingChannelAdapterParser;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.transaction.PlatformTransactionManager;

import javax.transaction.TransactionManager;
import java.io.File;

@Configuration
@EnableIntegration
@ComponentScan(basePackages = {
        "com.create.batch.integration"
})
public class IntegrationConfiguration {
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new JpaTransactionManager();
//    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        final PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }

    @Bean
    public DirectChannel inboundFileChannel() {
        return new DirectChannel();
    }

    @Bean
    public DirectChannel outboundJobRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "loggingChannel")
    public LoggingHandler loggingHandler() {
        return new LoggingHandler(LoggingHandler.Level.INFO.name());
    }

    @Bean
    @InboundChannelAdapter(value = "inboundFileChannel", poller = @Poller(cron="${ticket.poller.cron}"))
    public MessageSource<File> fileMessageSource(@Value("${ticket.poller.path}")final String path,
                                                 @Value("${ticket.poller.fileMask}")final String fileMask) {
        final FileReadingMessageSource source = new FileReadingMessageSource();
        final CompositeFileListFilter<File> compositeFileListFilter = new CompositeFileListFilter<>();
        final SimplePatternFileListFilter simplePatternFileListFilter = new SimplePatternFileListFilter(fileMask);
        final AcceptOnceFileListFilter<File> acceptOnceFileListFilter = new AcceptOnceFileListFilter<>();
        compositeFileListFilter.addFilter(simplePatternFileListFilter);
        compositeFileListFilter.addFilter(acceptOnceFileListFilter);
        source.setFilter(compositeFileListFilter);
         source.setDirectory(new File(path));
        return source;
    }

    @ServiceActivator(inputChannel = "outboundJobRequestChannel", outputChannel = "loggingChannel")
    @Bean
    public JobLaunchingMessageHandler jobLaunchingGateway(final JobLauncher jobLauncher) {
        return new JobLaunchingMessageHandler(jobLauncher);
    }

//    @ServiceActivator(inputChannel = "outboundJobRequestChannel")
//    @Bean
//    public JobLaunchingGateway jobLaunchingGateway(final JobLauncher jobLauncher) {
//        JobLaunchingGateway jobLaunchingGateway = new JobLaunchingGateway(jobLauncher);
//        jobLaunchingGateway.setRequiresReply(false);
//        return jobLaunchingGateway;
//    }

//@Bean
//public IntegrationFlow fileReadingFlow(@Value("${fpml.messages.input}") File directory) {
//    return IntegrationFlows
//            .from(s -> s.file(directory).patternFilter("*.xml"),
//                    e -> e.poller(Pollers.fixedDelay(20000)))
//    .....................
//    .get();
//}
}
