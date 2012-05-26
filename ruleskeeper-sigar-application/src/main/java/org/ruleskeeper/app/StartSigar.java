/*
 * RulesKeeper, Monitoring System
 * Copyright (C) 2011-2012 RulesKeeper
 * mailto:contact AT ruleskeeper DOT org
 *
 * RulesKeeper is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * RulesKeeper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with RulesKeeper; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.ruleskeeper.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartSigar {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartSigar.class);

	private static final String CONF_PROPERTIES = "/conf/ruleskeeper-sigar.properties";
	protected static final String SIGAR_OBJECT_QUARTZ_PARAM = "SIGAR_OBJECT";
	protected static final String SERVER_URL_QUARTZ_PARAM = "SERVER_URL";

	private static final String DEFAULT_REFRESH_CRON_EXPRESSION = "0 0/1 * * * ?";
	private static final String DEFAULT_SERVER_URL = "http://127.0.0.1:9000";

	private String refreshCronExpression = null;
	private String serverUrl = null;

	public StartSigar(Properties configuration) {
		refreshCronExpression = configuration.getProperty("ruleskeeper.sigar.refresh.cron", DEFAULT_REFRESH_CRON_EXPRESSION);
		serverUrl = configuration.getProperty("ruleskeeper.server.url", DEFAULT_SERVER_URL);
		RulesKeeperSigarScheduler.INSTANCE.setRefreshCronExpression(refreshCronExpression);
		RulesKeeperSigarScheduler.INSTANCE.setServerUrl(serverUrl);
	}

	public void schedule() {
		RulesKeeperSigarScheduler.INSTANCE.schedule();
	}

	public void start() {
		try {
			RulesKeeperSigarScheduler.INSTANCE.start();
		} catch (SchedulerException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		Properties configuration = getConfiguration();
		StartSigar p = new StartSigar(configuration);
		p.schedule();
		p.start();
	}

	private static Properties getConfiguration() throws IOException {
		Properties properties = new Properties();
		InputStream is = StartSigar.class.getResourceAsStream(CONF_PROPERTIES);
		if (is == null) {
			LOGGER.error("conf file not found in classpath '" + CONF_PROPERTIES + "' => loading default configuration");
		} else {
			properties.load(is);
		}
		return properties;

	}

}
