/*
 * Copyright 2016 Sebastian Gil.
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

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "com.create.controller"
})
public class WebConfiguration {
    @Value("${spring.h2.console.path}")
    private String h2ConsoleContextPath;

    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings(h2ConsoleMappings());
        return registration;
    }

    private String h2ConsoleMappings() {
        return h2ConsoleContextPath + "/*";
    }
}
