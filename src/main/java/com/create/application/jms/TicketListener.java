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

package com.create.application.jms;

import com.create.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TicketListener {
    private static final Logger log = LoggerFactory.getLogger(TicketListener.class);

    @JmsListener(destination = "jms/ticket" )
    public void onNewTicket(final Ticket ticket) {
        log.debug("onNewTicket : {}", ticket);
        //    public JobLaunchRequest toRequest(Message<File> message) {
//        JobParametersBuilder jobParametersBuilder =
//                new JobParametersBuilder();
//
//        jobParametersBuilder.addString(fileParameterName,
//                message.getPayload().getAbsolutePath());
//
//        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
//    }
    }
}
