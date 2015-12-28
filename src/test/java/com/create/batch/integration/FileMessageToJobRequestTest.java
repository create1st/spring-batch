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

package com.create.batch.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.Job;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.messaging.Message;

import java.io.File;
import java.time.Clock;
import java.time.Instant;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileMessageToJobRequestTest {
    public static final String PARAMETER = "PARAMETER";
    public static final long TIMESTAMP = 123456L;
    public static final String PATH = "PATH";

    private FileMessageToJobRequest transformer;

    @Mock
    private Job job;
    @Mock
    private Clock clock;

    @Before
    public void setup() {
        transformer = new FileMessageToJobRequest(job, clock, PARAMETER);
    }

    @Test
    public void testMessageFileToJobLaunchRequestConversion() {
        // given
        final Message<File> message = mock(Message.class);
        final File file = mock(File.class);
        when(file.getAbsolutePath()).thenReturn(PATH);
        when(message.getPayload()).thenReturn(file);
        final Instant timestamp = Instant.ofEpochSecond(TIMESTAMP);
        when(clock.instant()).thenReturn(timestamp);

        // when
        final JobLaunchRequest jobLaunchRequest = transformer.toRequest(message);

        // then
        assertThat(jobLaunchRequest.getJob(), is(job));
        assertThat(jobLaunchRequest.getJobParameters().getLong(FileMessageToJobRequest.TIMESTAMP_PARAMETER), equalTo(TIMESTAMP));
        assertThat(jobLaunchRequest.getJobParameters().getString(PARAMETER), equalTo(PATH));
    }

}