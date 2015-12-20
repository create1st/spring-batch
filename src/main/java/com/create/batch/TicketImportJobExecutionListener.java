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

import com.create.metrics.MetricProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import java.util.List;

/**
 * Ticket import job execution stage monitor.
 */
public class TicketImportJobExecutionListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(TicketImportJobExecutionListener.class);

    private final List<MetricProvider> metrics;

    public TicketImportJobExecutionListener(final List<MetricProvider> metrics) {
        this.metrics = metrics;
    }

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        log.debug("Ticket import job started.");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.debug("Ticket import job completed.");
            metrics.stream()
                    .map(metric -> String.format("%s = %s", metric.getMetricName(), metric.getMetricValue()))
                    .forEach(log::debug);
        }
    }
}
