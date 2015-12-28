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

package com.create.application.configuration;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DestinationResolver;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
@EnableJms
@ComponentScan(basePackages = {
        "com.create.jms"
})
public class JmsConfiguration {
    @Autowired(required = false)
    private DestinationResolver destinationResolver;

    @Bean
    public BrokerService embeddedActiveMq(@Value("${spring.activemq.broker-url}") final String brokerUrl) throws Exception {
        final BrokerService broker = new BrokerService();
        broker.addConnector(brokerUrl);
        broker.start();
        return broker;
    }

    @Bean
    public Destination destination(@Value("${ticket.queue}") final String queueName) {
        return new ActiveMQQueue(queueName);
    }

    @Bean
    public JmsTemplate jmsTemplate(final ConnectionFactory connectionFactory,
                                   final JmsProperties properties,
                                   final Destination destination) {
        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPubSubDomain(properties.isPubSubDomain());
        jmsTemplate.setDefaultDestination(destination);
        if (destinationResolver != null) {
            jmsTemplate.setDestinationResolver(destinationResolver);
        }
        return jmsTemplate;
    }


}
