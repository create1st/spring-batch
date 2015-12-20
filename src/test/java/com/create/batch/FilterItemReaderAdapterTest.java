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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;

import java.util.function.Predicate;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilterItemReaderAdapterTest {

    @Mock
    private Predicate<Object> filterCondition;

    @Mock
    private ItemStreamReader<Object> reader;

    private FilterItemReaderAdapter<Object> adapter;

    @Before
    public void setup() {
        adapter = new FilterItemReaderAdapter<>(reader, filterCondition);
    }

    @Test
    public void testReadNotfiltered() throws Exception {
        // given
        final Object value = mock(Object.class);
        when(reader.read()).thenReturn(value);
        when(filterCondition.test(value)).thenReturn(true);

        // when
        final Object readValue = this.adapter.read();

        // then
        assertThat(readValue, is(value));
        verify(reader).read();
    }

    @Test
    public void testReadFiltered() throws Exception {
        // given
        final Object value1 = mock(Object.class);
        final Object value2 = mock(Object.class);
        when(reader.read()).thenReturn(value1, value2);
        when(filterCondition.test(value1)).thenReturn(false);
        when(filterCondition.test(value2)).thenReturn(true);

        // when
        final Object readValue = this.adapter.read();

        // then
        assertThat(readValue, is(value2));
        verify(reader, times(2)).read();
    }

    @Test
    public void testReadNoMoreItems() throws Exception {
        // given
        when(reader.read()).thenReturn(null);

        // when
        final Object readValue = this.adapter.read();

        // then
        assertThat(readValue, nullValue());
        verify(reader).read();
    }

    @Test
    public void testOpen() throws Exception {
        // given
        final ExecutionContext executionContext = mock(ExecutionContext.class);

        // when
        this.adapter.open(executionContext);

        // then
        verify(reader).open(executionContext);
    }

    @Test
    public void testUpdate() throws Exception {
        // given
        final ExecutionContext executionContext = mock(ExecutionContext.class);

        // when
        this.adapter.update(executionContext);

        // then
        verify(reader).update(executionContext);
    }

    @Test
    public void testClose() throws Exception {
        // given

        // when
        this.adapter.close();

        // then
        verify(reader).close();
    }
}