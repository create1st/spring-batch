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

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;

import java.util.function.Predicate;

/**
 * {@link ItemStreamReader} adapter to filter out undesired items.
 *
 * @param <T>
 */
public class FilterItemReaderAdapter<T> implements ItemReader<T>, ItemStream {

    private final ItemStreamReader<T> reader;

    private final Predicate<T> filterCondition;

    /**
     * @param reader          the {@link ItemStreamReader} implementation to delegate to
     * @param filterCondition Condition predicate.
     */
    public FilterItemReaderAdapter(final ItemStreamReader<T> reader, final Predicate<T> filterCondition) {
        this.reader = reader;
        this.filterCondition = filterCondition;
    }

    @Override
    public T read() throws Exception {
        T item;
        do {
            item = reader.read();
        } while (item != null && !filterCondition.test(item));
        return item;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        reader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        reader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        reader.close();
    }
}
