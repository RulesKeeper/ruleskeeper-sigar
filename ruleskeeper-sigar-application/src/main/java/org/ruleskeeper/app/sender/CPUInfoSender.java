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

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.MetricProtoBuf.Metric.UnitType;
import org.ruleskeeper.app.RulesKeeperSigarScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPUInfoSender extends AbstractDataSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(CPUInfoSender.class);
	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	private static final String MEASURE_CPU_PREFIX = "system.cpu.";
	private static final String INFO_SUFFIX = "info";
	private static final String TOTAL_SUFFIX = "total";

	public CPUInfoSender() {
	}

	@Override
	public void retrieveAndSend(Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate) throws SigarException, IllegalArgumentException, IOException,
	    InterruptedException, ExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("CPU Info: START");
		org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
		org.hyperic.sigar.CpuInfo info = infos[0];

		sendCpuInfo(serverUrl, hostName, info, eventDate);
		sendTotalCPU(sigar, serverUrl, hostName, eventDate);
		sendDetailedCPU(sigar, serverUrl, hostName, eventDate);
		LoggerFactory.getLogger(LOGGER_INFO).info("CPU Info: END");
	}

	private void sendDetailedCPU(Sigar sigar, String serverUrl, String hostName, Date eventDate) throws SigarException, IOException, InterruptedException, IllegalArgumentException,
	    ExecutionException {
		CpuPerc[] cpus = sigar.getCpuPercList();
		for (int i = 0; i < cpus.length; i++) {
			String key = MEASURE_CPU_PREFIX + i;
			LOGGER.debug(key);
			LOGGER.debug("" + cpus[i]);

			double user = cpus[i].getUser();
			String value = formatAsPercent(user);
			LOGGER.debug(value);
			Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(key).setTechnicalKey(key + ".user").setShortName("CPU " + i + " Usage User")
			    .setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			double system = cpus[i].getSys();
			value = formatAsPercent(system);
			LOGGER.debug(value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(key).setTechnicalKey(key + ".system").setShortName("CPU " + i + " Usage System").setUnit(UnitType.PERCENT)
			    .setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = formatAsPercent(user + system);
			LOGGER.debug(value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(key).setTechnicalKey(key + ".used").setShortName("CPU " + i + " Used").setUnit(UnitType.PERCENT)
			    .setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = formatAsPercent(cpus[i].getNice());
			LOGGER.debug(value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(key).setTechnicalKey(key + ".nice").setShortName("CPU " + i + " Usage Nice").setUnit(UnitType.PERCENT)
			    .setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = formatAsPercent(cpus[i].getWait());
			LOGGER.debug(value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(key).setTechnicalKey(key + ".wait").setShortName("CPU " + i + " Usage Wait").setUnit(UnitType.PERCENT)
			    .setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = formatAsPercent(cpus[i].getIdle());
			LOGGER.debug(value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(key).setTechnicalKey(key + ".idle").setShortName("CPU " + i + " Usage Idle").setUnit(UnitType.PERCENT)
			    .setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);
		}
	}

	private void sendTotalCPU(Sigar sigar, String serverUrl, String hostName, Date eventDate) throws SigarException, IOException, InterruptedException, IllegalArgumentException,
	    ExecutionException {
		CpuPerc cpuAll = sigar.getCpuPerc();

		double user = cpuAll.getUser();
		String value = formatAsPercent(user);
		LOGGER.debug(value);
		Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + TOTAL_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "total.user")
		    .setShortName("CPU Total Usage User").setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
		send(m, serverUrl);

		double system = cpuAll.getSys();
		value = formatAsPercent(system);
		LOGGER.debug(value);
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + TOTAL_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "total.system")
		    .setShortName("CPU Total Usage System").setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
		send(m, serverUrl);

		value = formatAsPercent(user + system);
		LOGGER.debug(value);
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + TOTAL_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "total.used")
		    .setShortName("CPU Total Used").setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value)
		    .setDefaultWarningThreshold(String.valueOf(RulesKeeperSigarScheduler.INSTANCE.getDefaultWarnThresholdCPU()))
		    .setDefaultErrorThreshold(String.valueOf(RulesKeeperSigarScheduler.INSTANCE.getDefaultErrorThresholdCPU())).build();
		send(m, serverUrl);

		value = formatAsPercent(cpuAll.getNice());
		LOGGER.debug(value);
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + TOTAL_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "total.nice")
		    .setShortName("CPU Total Usage Nice").setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
		send(m, serverUrl);

		value = formatAsPercent(cpuAll.getWait());
		LOGGER.debug(value);
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + TOTAL_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "total.wait")
		    .setShortName("CPU Total Usage Wait").setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
		send(m, serverUrl);

		value = formatAsPercent(cpuAll.getIdle());
		LOGGER.debug(value);
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + TOTAL_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "total.idle")
		    .setShortName("CPU Total Usage Idle").setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
		send(m, serverUrl);
	}

	private void sendCpuInfo(String serverUrl, String hostName, org.hyperic.sigar.CpuInfo info, Date eventDate) throws IOException, InterruptedException, IllegalArgumentException,
	    ExecutionException {
		LOGGER.debug("Vendor: {}", info.getVendor());
		Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "vendor")
		    .setShortName("Vendor").setUnit(UnitType.STRING).setEventDate(String.valueOf(eventDate.getTime())).setValue(info.getVendor()).build();
		send(m, serverUrl);

		LOGGER.debug("Model: {}", info.getModel());
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "model").setShortName("Model")
		    .setUnit(UnitType.STRING).setEventDate(String.valueOf(eventDate.getTime())).setValue(info.getModel()).build();
		send(m, serverUrl);

		LOGGER.debug("Mhz: {}", info.getMhz());
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "frequency").setShortName("Frequency")
		    .setUnit(UnitType.STRING).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + info.getMhz()).build();
		send(m, serverUrl);

		LOGGER.debug("Total Cores: {}", info.getTotalCores());
		m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "cores").setShortName("# Cores")
		    .setUnit(UnitType.COUNTER).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + info.getTotalCores()).build();
		send(m, serverUrl);

		if ((info.getTotalCores() != info.getTotalSockets()) || (info.getCoresPerSocket() > info.getTotalCores())) {
			LOGGER.debug("Physical CPUs: {}", info.getTotalSockets());
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "physicals")
			    .setShortName("# Physicals").setUnit(UnitType.COUNTER).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + info.getTotalSockets()).build();
			send(m, serverUrl);

			LOGGER.debug("Cores per CPU: {}", info.getCoresPerSocket());
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "corespercpu")
			    .setShortName("# Cores per CPU").setUnit(UnitType.COUNTER).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + info.getCoresPerSocket()).build();
			send(m, serverUrl);
		}

		long cacheSize = info.getCacheSize();
		if (cacheSize != Sigar.FIELD_NOTIMPL) {
			LOGGER.debug("Cache Size: {}", cacheSize);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_CPU_PREFIX + INFO_SUFFIX).setTechnicalKey(MEASURE_CPU_PREFIX + "cachesize")
			    .setShortName("Cache Size").setUnit(UnitType.COUNTER).setEventDate(String.valueOf(eventDate.getTime())).setValue("" + info.getCacheSize()).build();
			send(m, serverUrl);
		}
	}

}
