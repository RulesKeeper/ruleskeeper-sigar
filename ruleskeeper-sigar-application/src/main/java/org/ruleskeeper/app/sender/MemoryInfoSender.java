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

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.MetricProtoBuf.Metric.UnitType;
import org.ruleskeeper.app.RulesKeeperSigarScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryInfoSender extends AbstractDataSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryInfoSender.class);
	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	private static final String MEASURE_MEMORY_PREFIX = "system.memory";

	public MemoryInfoSender() {
	}

	@Override
	public void retrieveAndSend(Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate) throws SigarException, IOException, InterruptedException,
	    IllegalArgumentException, ExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("Memory Info: START");
		Mem mem = sigar.getMem();

		LOGGER.debug("Memory Total: {}", formatToMegaByte(mem.getTotal()));
		Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_MEMORY_PREFIX).setTechnicalKey(MEASURE_MEMORY_PREFIX + ".total").setShortName("Memory Total")
		    .setUnit(UnitType.MEGABYTES).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + formatToMegaByte(mem.getTotal())).build();
		send(m, serverUrl);

		LOGGER.debug("Memory Used: {}", formatToMegaByte(mem.getUsed()));
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_MEMORY_PREFIX).setTechnicalKey(MEASURE_MEMORY_PREFIX + ".used").setShortName("Memory Used")
		    .setUnit(UnitType.MEGABYTES).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + formatToMegaByte(mem.getUsed()))
		    .setDefaultWarningThreshold(String.valueOf(RulesKeeperSigarScheduler.INSTANCE.getDefaultWarnThresholdMemory()))
		    .setDefaultErrorThreshold(String.valueOf(RulesKeeperSigarScheduler.INSTANCE.getDefaultErrorThresholdMemory())).build();
		send(m, serverUrl);

		LOGGER.debug("Memory Free: {}", formatToMegaByte(mem.getFree()));
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_MEMORY_PREFIX).setTechnicalKey(MEASURE_MEMORY_PREFIX + ".free").setShortName("Memory Not Used")
		    .setUnit(UnitType.MEGABYTES).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + formatToMegaByte(mem.getFree())).build();
		send(m, serverUrl);

		LoggerFactory.getLogger(LOGGER_INFO).info("Memory Info: END");
	}
}
