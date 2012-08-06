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
package org.ruleskeeper.app.sender;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.MetricProtoBuf.Metric.UnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerInfoSender extends AbstractDataSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoSender.class);
	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	private static final String MEASURE_PREFIX = "system";

	public ServerInfoSender() {
	}

	@Override
	public void retrieveAndSend(Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate) throws SigarException, IllegalArgumentException, IOException,
	    InterruptedException, ExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("Server Info: START");

		double upTime = sigar.getUptime().getUptime();
		LOGGER.debug("System Up Time: {}", upTime);
		Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_PREFIX).setTechnicalKey(MEASURE_PREFIX + ".uptime").setShortName("System Up Time")
		    .setUnit(UnitType.SECONDS).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + upTime).build();
		send(m, serverUrl);

		LoggerFactory.getLogger(LOGGER_INFO).info("Server Info: END");
	}

}
