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

import com.create.metrics.MetricUpdater;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Predicate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetricItemProcessorTest {

    @Mock
    private Predicate<Object> predicate;

    @Mock
    private MetricUpdater<Object> metricUpdater;

    private MetricItemProcessor<Object> processor;

    @Before
    public void setup() {
        processor = new MetricItemProcessor(predicate, metricUpdater);
    }

    @Test
    public void testProcessWithoutMetricUpdate() throws Exception {
        // given
        final Object item = mock(Object.class);
        when(predicate.test(item)).thenReturn(false);

        // when
        final Object processedItem = processor.process(item);

        // then
        assertThat(processedItem, is(item));
        verify(metricUpdater, never()).updateWith(item);
    }

    @Test
    public void testProcessWithMetricUpdate() throws Exception {
        // given
        final Object item = mock(Object.class);
        when(predicate.test(item)).thenReturn(true);

        // when
        final Object processedItem = processor.process(item);

        // then
        assertThat(processedItem, is(item));
        verify(metricUpdater).updateWith(item);
    }
}