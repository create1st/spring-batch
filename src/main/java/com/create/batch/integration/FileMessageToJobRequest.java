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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Clock;
import java.time.Instant;

/**
 * Spring integration {@link Transformer} converts {@link File} {@link Message} to {@link JobLaunchRequest}.
 */
@Component
public class FileMessageToJobRequest {
    private static final Logger log = LoggerFactory.getLogger(FileMessageToJobRequest.class);
    public static final String TIMESTAMP_PARAMETER = "timestamp";

    private final Job job;
    private final Clock clock;
    private final String jobParameter;

    @Autowired
    public FileMessageToJobRequest(final Job job, final Clock clock, @Value("${ticket.job.parameter}") final String jobParameter) {
        this.job = job;
        this.clock = clock;
        this.jobParameter = jobParameter;
    }

    @Transformer(inputChannel = "inboundFileChannel", outputChannel = "outboundJobRequestChannel")
    public JobLaunchRequest toRequest(final Message<File> message) {
        log.debug("toRequest : {}", message);
        final Instant timestamp = clock.instant();
        final JobParameters jobParameters =
                new JobParametersBuilder()
                        .addString(jobParameter, message.getPayload().getAbsolutePath())
                        .addLong(TIMESTAMP_PARAMETER, timestamp.getEpochSecond())
                        .toJobParameters();
        return new JobLaunchRequest(job, jobParameters);
    }
}
