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
import org.springframework.batch.item.ItemProcessor;

import java.util.function.Predicate;

/**
 * Go-through processor which as a side effect is doing metric update in case when provided condition is fulfilled.
 */
public class MetricItemProcessor<T> implements ItemProcessor<T, T> {

    private final Predicate<T> predicate;

    private final MetricUpdater<T> metricUpdater;

    public MetricItemProcessor(final Predicate<T> predicate, final MetricUpdater<T> metricUpdater) {
        this.predicate = predicate;
        this.metricUpdater = metricUpdater;
    }

    @Override
    public T process(final T item) throws Exception {
        if (predicate.test(item)) {
            metricUpdater.updateWith(item);
        }
        return item;
    }
}