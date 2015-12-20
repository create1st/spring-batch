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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketJobExecutionListenerTest {

    @Mock
    private MetricProvider metricProvider = mock(MetricProvider.class);

    private TicketImportJobExecutionListener listener;

    @Before
    public void setup() {
        listener = new TicketImportJobExecutionListener(Arrays.asList(metricProvider));
    }

    @Test
    public void testBeforeJob() throws Exception {
        // given
        final JobExecution jobExecution = mock(JobExecution.class);

        // when
        listener.beforeJob(jobExecution);

        // then
        verifyNoMoreInteractions(metricProvider);
    }

    @Test
    public void testAfterJobCompleted() throws Exception {
        // given
        final JobExecution jobExecution = mock(JobExecution.class);
        when(jobExecution.getStatus()).thenReturn(BatchStatus.COMPLETED);

        // when
        listener.afterJob(jobExecution);

        // then
        verify(metricProvider).getMetricName();
        verify(metricProvider).getMetricValue();
    }

    @Test
    public void testAfterJobNotCompleted() throws Exception {
        // given
        final JobExecution jobExecution = mock(JobExecution.class);
        when(jobExecution.getStatus()).thenReturn(BatchStatus.STARTED);

        // when
        listener.afterJob(jobExecution);

        // then
        verifyNoMoreInteractions(metricProvider);
    }
}