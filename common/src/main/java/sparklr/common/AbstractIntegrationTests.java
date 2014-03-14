/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package sparklr.common;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sparklr.common.AbstractIntegrationTests.TestConfiguration;

@SpringApplicationConfiguration(classes=TestConfiguration.class, inheritLocations=true)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@IntegrationTest
public abstract class AbstractIntegrationTests implements PortHolder {

	@Rule
	public HttpTestUtils serverRunning = HttpTestUtils.standard().setPortHolder(this);
	
	@Rule
	public OAuth2ContextSetup context = OAuth2ContextSetup.standard(serverRunning);
	
	@Autowired
	private EmbeddedWebApplicationContext server;
	
	@Override
	public int getPort() {
		return server==null ? 8080 : server.getEmbeddedServletContainer().getPort();
	}
	
	@Configuration
	@PropertySource(value="classpath:test.properties", ignoreResourceNotFound=true)
	protected static class TestConfiguration {
		
	}

}