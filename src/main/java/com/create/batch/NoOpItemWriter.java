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

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;
import org.springframework.batch.item.ItemStreamWriter;

import java.util.List;

/**
 * Dummy {@link ItemStreamWriter}
 *
 * @param <T> Item type
 */
public class NoOpItemWriter<T> extends AbstractItemStreamItemWriter<T> {

    @Override
    public void write(List<? extends T> items) throws Exception {
        // Do nothing
    }
}
