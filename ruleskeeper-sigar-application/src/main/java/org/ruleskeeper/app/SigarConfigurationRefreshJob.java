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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SigarConfigurationRefreshJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SigarConfigurationRefreshJob.class);

	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	protected static final String CONF_PROPERTIES = "/conf/ruleskeeper-sigar.properties";

	private static final String DEFAULT_REFRESH_CRON_EXPRESSION = "0 0/1 * * * ?";
	public static final String DEFAULT_SERVER_URL = "http://127.0.0.1:9000";

	private static final String DEFAULT_CPU_WARN_THRESHOLD = "80";
	private static final String DEFAULT_CPU_ERROR_THRESHOLD = "90";
	private static final String DEFAULT_MEMORY_WARN_THRESHOLD = "80";
	private static final String DEFAULT_MEMORY_ERROR_THRESHOLD = "90";
	private static final String DEFAULT_FS_WARN_THRESHOLD = "80";
	private static final String DEFAULT_FS_ERROR_THRESHOLD = "90";

	private static final String DEFAULT_GATHERING_CPU_ACTIVE = "Y";
	private static final String DEFAULT_GATHERING_MEMORY_ACTIVE = "Y";
	private static final String DEFAULT_GATHERING_FS_ACTIVE = "Y";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("checking if configuration has changed");
		boolean confChanged = Boolean.FALSE;

		// priority:
		//
		// 1. read the configuration from file system
		// 2. read the configuration from RulesKeeper Server
		//
		// if the configuration changed, update application consequently

		Properties configuration = null;
		try {
			configuration = getConfiguration();
			confChanged = Boolean.TRUE;

			String refreshCronExpression = configuration.getProperty("ruleskeeper.sigar.refresh.cron", DEFAULT_REFRESH_CRON_EXPRESSION);
			String serverUrl = configuration.getProperty("ruleskeeper.server.url", DEFAULT_SERVER_URL);
			String defaultWarnThresholdCPU = configuration.getProperty("ruleskeeper.sigar.cpu.defaultWarnThreshold", DEFAULT_CPU_WARN_THRESHOLD);
			String defaultErrorThresholdCPU = configuration.getProperty("ruleskeeper.sigar.cpu.defaultErrorThreshold", DEFAULT_CPU_ERROR_THRESHOLD);
			String defaultWarnThresholdMemory = configuration.getProperty("ruleskeeper.sigar.memory.defaultWarnThreshold", DEFAULT_MEMORY_WARN_THRESHOLD);
			String defaultErrorThresholdMemory = configuration.getProperty("ruleskeeper.sigar.memory.defaultErrorThreshold", DEFAULT_MEMORY_ERROR_THRESHOLD);
			String defaultWarnThresholdFileSystem = configuration.getProperty("ruleskeeper.sigar.fs.defaultWarnThreshold", DEFAULT_FS_WARN_THRESHOLD);
			String defaultErrorThresholdFileSystem = configuration.getProperty("ruleskeeper.sigar.fs.defaultErrorThreshold", DEFAULT_FS_ERROR_THRESHOLD);

			String gatheringCPUActive = configuration.getProperty("ruleskeeper.sigar.gathering.cpu.active", DEFAULT_GATHERING_CPU_ACTIVE);
			String gatheringMemoryActive = configuration.getProperty("ruleskeeper.sigar.gathering.memory.active", DEFAULT_GATHERING_MEMORY_ACTIVE);
			String gatheringFileSystemActive = configuration.getProperty("ruleskeeper.sigar.gathering.fs.active", DEFAULT_GATHERING_FS_ACTIVE);

			RulesKeeperSigarScheduler.INSTANCE.setRefreshCronExpression(refreshCronExpression);
			RulesKeeperSigarScheduler.INSTANCE.setServerUrl(serverUrl);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultWarnThresholdCPU(defaultWarnThresholdCPU);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultErrorThresholdCPU(defaultErrorThresholdCPU);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultWarnThresholdMemory(defaultWarnThresholdMemory);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultErrorThresholdMemory(defaultErrorThresholdMemory);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultWarnThresholdFileSystem(defaultWarnThresholdFileSystem);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultErrorThresholdFileSystem(defaultErrorThresholdFileSystem);

			if ("Y".equalsIgnoreCase(gatheringCPUActive)) {
				RulesKeeperSigarScheduler.INSTANCE.setGatheringCPUActive(Boolean.TRUE);
			} else {
				RulesKeeperSigarScheduler.INSTANCE.setGatheringCPUActive(Boolean.FALSE);
			}
			if ("Y".equalsIgnoreCase(gatheringMemoryActive)) {
				RulesKeeperSigarScheduler.INSTANCE.setGatheringMemoryActive(Boolean.TRUE);
			} else {
				RulesKeeperSigarScheduler.INSTANCE.setGatheringMemoryActive(Boolean.FALSE);
			}
			if ("Y".equalsIgnoreCase(gatheringFileSystemActive)) {
				RulesKeeperSigarScheduler.INSTANCE.setGatheringFileSystemActive(Boolean.TRUE);
			} else {
				RulesKeeperSigarScheduler.INSTANCE.setGatheringFileSystemActive(Boolean.FALSE);
			}
		} catch (IOException e) {
			LOGGER.error("unable to load conf file '" + CONF_PROPERTIES + "': applying default configuration", e);

			RulesKeeperSigarScheduler.INSTANCE.setRefreshCronExpression(DEFAULT_REFRESH_CRON_EXPRESSION);
			RulesKeeperSigarScheduler.INSTANCE.setServerUrl(DEFAULT_SERVER_URL);

			RulesKeeperSigarScheduler.INSTANCE.setDefaultWarnThresholdCPU(DEFAULT_CPU_WARN_THRESHOLD);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultErrorThresholdCPU(DEFAULT_CPU_ERROR_THRESHOLD);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultWarnThresholdMemory(DEFAULT_MEMORY_WARN_THRESHOLD);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultErrorThresholdMemory(DEFAULT_MEMORY_ERROR_THRESHOLD);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultWarnThresholdFileSystem(DEFAULT_FS_WARN_THRESHOLD);
			RulesKeeperSigarScheduler.INSTANCE.setDefaultErrorThresholdFileSystem(DEFAULT_FS_ERROR_THRESHOLD);

			RulesKeeperSigarScheduler.INSTANCE.setGatheringCPUActive(Boolean.TRUE);
			RulesKeeperSigarScheduler.INSTANCE.setGatheringMemoryActive(Boolean.TRUE);
			RulesKeeperSigarScheduler.INSTANCE.setGatheringFileSystemActive(Boolean.TRUE);
		}

		if (confChanged) {
			LoggerFactory.getLogger(LOGGER_INFO).info("configuration changes detected, update application consequently");
			LoggerFactory.getLogger(LOGGER_INFO).info("data gathering cron expression: {}", RulesKeeperSigarScheduler.INSTANCE.getRefreshCronExpression());
			LoggerFactory.getLogger(LOGGER_INFO).info("data are pushed to: {}", RulesKeeperSigarScheduler.INSTANCE.getServerUrl());
			RulesKeeperSigarScheduler.INSTANCE.scheduleDataPickerJob();
		} else {
			LoggerFactory.getLogger(LOGGER_INFO).info("no configuration change detected");
		}
	}

	private static Properties getConfiguration() throws IOException {
		Properties properties = new Properties();
		InputStream is = StartSigar.class.getResourceAsStream(CONF_PROPERTIES);
		if (is == null) {
			LoggerFactory.getLogger(LOGGER_INFO).error("conf file '{}' not found in classpath => loading default configuration", CONF_PROPERTIES);
		} else {
			properties.load(is);
		}
		return properties;
	}
}
