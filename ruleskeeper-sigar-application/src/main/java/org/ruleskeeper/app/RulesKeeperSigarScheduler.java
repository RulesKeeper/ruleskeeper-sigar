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

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum RulesKeeperSigarScheduler {
	INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(RulesKeeperSigarScheduler.class);

	public static final String SIGAR_OBJECT_QUARTZ_PARAM = "SIGAR_OBJECT";
	public static final String SIGAR_PROXY_OBJECT_QUARTZ_PARAM = "SIGAR_PROXY_OBJECT";
	public static final String SERVER_URL_QUARTZ_PARAM = "SERVER_URL";

	private static final String CONF_CHECKER_REFRESH_CRON_EXPRESSION = "0 0/2 * * * ?";

	private String refreshCronExpression = null;
	private String serverUrl = null;

	private String defaultWarnThresholdCPU = null;
	private String defaultErrorThresholdCPU = null;
	private String defaultWarnThresholdMemory = null;
	private String defaultErrorThresholdMemory = null;
	private String defaultWarnThresholdFileSystem = null;
	private String defaultErrorThresholdFileSystem = null;

	private Boolean gatheringCPUActive = Boolean.TRUE;
	private Boolean gatheringMemoryActive = Boolean.TRUE;
	private Boolean gatheringFileSystemActive = Boolean.TRUE;

	private Scheduler scheduler = null;

	private static AtomicInteger waitTimeForServerAvailability = new AtomicInteger(0);
	// Wait max for 600 seconds : 10 minutes
	private static final int MAX_WAIT = 600000;

	RulesKeeperSigarScheduler() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			LoggerFactory.getLogger(RulesKeeperSigarScheduler.class).error(e.getMessage());
		}
	}

	private Scheduler getScheduler() {
		return scheduler;
	}

	public void increaseWaitTime() {
		if (waitTimeForServerAvailability.get() < MAX_WAIT) {
			waitTimeForServerAvailability.set(waitTimeForServerAvailability.get() + RulesKeeperSigarConstants.MS_TO_SECOND_MULTIPLE);
		}
	}

	public void resetWaitTime() {
		waitTimeForServerAvailability.set(0);
	}

	public int getWaitTimeForServerAvailability() {
		return waitTimeForServerAvailability.get();
	}

	private static JobDataMap translate(Sigar sigar, SigarProxy sigarProxy, String serverUrl) {
		JobDataMap dataMap = new JobDataMap();
		if (sigar != null) {
			dataMap.put(SIGAR_OBJECT_QUARTZ_PARAM, sigar);
			dataMap.put(SIGAR_PROXY_OBJECT_QUARTZ_PARAM, sigarProxy);
			dataMap.put(SERVER_URL_QUARTZ_PARAM, serverUrl);
		}
		return dataMap;
	}

	public void printScheduledJobs() throws SchedulerException {
		String[] jobGroups;
		String[] jobsInGroup;
		int i;
		int j;

		jobGroups = getScheduler().getJobGroupNames();
		for (i = 0; i < jobGroups.length; i++) {
			LOGGER.debug("Group: " + jobGroups[i] + " contains the following jobs");
			jobsInGroup = getScheduler().getJobNames(jobGroups[i]);

			for (j = 0; j < jobsInGroup.length; j++) {
				LOGGER.debug("- " + jobsInGroup[j]);
			}
		}
		LOGGER.debug("END");
	}

	public void scheduleConfigurationRefreshJob() {
		try {
			JobDetail scheduledJob = new JobDetail("ConfRefreshScheduled", "RK_SIGAR_CONF", SigarConfigurationRefreshJob.class);
			CronTrigger scheduledTrigger = new CronTrigger("ConfRefreshScheduled", "Scheduled", CONF_CHECKER_REFRESH_CRON_EXPRESSION);
			getScheduler().scheduleJob(scheduledJob, scheduledTrigger);

			JobDetail nowJob = new JobDetail("ConfRefreshNow", "RK_SIGAR_CONF", SigarConfigurationRefreshJob.class);
			SimpleTrigger nowTrigger = new SimpleTrigger("ConfRefreshNow", "NOW", new Date(), null, 0, 0L);
			getScheduler().scheduleJob(nowJob, nowTrigger);
		} catch (SchedulerException e) {
			LOGGER.error(e.getMessage());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void scheduleDataPickerJob() {
		try {
			Sigar sigar = new Sigar();
			SigarProxy sigarProxy = SigarProxyCache.newInstance(sigar);

			getScheduler().deleteJob("SystemDataScheduled", "RK_SIGAR");
			getScheduler().deleteJob("SystemDataNow", "RK_SIGAR");

			JobDataMap dataMap = translate(sigar, sigarProxy, getServerUrl());

			JobDetail scheduledJob = new JobDetail("SystemDataScheduled", "RK_SIGAR", SigarDataPickerJob.class);
			scheduledJob.setJobDataMap(dataMap);
			CronTrigger scheduledTrigger = new CronTrigger("SystemDataScheduled", "Scheduled", getRefreshCronExpression());
			getScheduler().scheduleJob(scheduledJob, scheduledTrigger);

			JobDetail nowJob = new JobDetail("SystemDataNow", "RK_SIGAR", SigarDataPickerJob.class);
			nowJob.setJobDataMap(dataMap);
			SimpleTrigger nowTrigger = new SimpleTrigger("SystemDataNow", "NOW", new Date(), null, 0, 0L);
			getScheduler().scheduleJob(nowJob, nowTrigger);

		} catch (SchedulerException e) {
			LOGGER.error(e.getMessage());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public void start() throws SchedulerException {
		// start the Scheduler
		if (getScheduler() != null) {
			getScheduler().start();
			LOGGER.info("Scheduler started");
		} else {
			LOGGER.info("Scheduler is null, can't be started.");
		}
	}

	public String getRefreshCronExpression() {
		return refreshCronExpression;
	}

	public void setRefreshCronExpression(String refreshCronExpression) {
		this.refreshCronExpression = refreshCronExpression;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getDefaultWarnThresholdCPU() {
		return defaultWarnThresholdCPU;
	}

	public String getDefaultErrorThresholdCPU() {
		return defaultErrorThresholdCPU;
	}

	public void setDefaultErrorThresholdCPU(String defaultErrorThresholdCPU) {
		this.defaultErrorThresholdCPU = defaultErrorThresholdCPU;
	}

	public void setDefaultWarnThresholdCPU(String defaultWarnThresholdCPU) {
		this.defaultWarnThresholdCPU = defaultWarnThresholdCPU;
	}

	public String getDefaultWarnThresholdMemory() {
		return defaultWarnThresholdMemory;
	}

	public void setDefaultWarnThresholdMemory(String defaultWarnThresholdMemory) {
		this.defaultWarnThresholdMemory = defaultWarnThresholdMemory;
	}

	public String getDefaultErrorThresholdMemory() {
		return defaultErrorThresholdMemory;
	}

	public void setDefaultErrorThresholdMemory(String defaultErrorThresholdMemory) {
		this.defaultErrorThresholdMemory = defaultErrorThresholdMemory;
	}

	public String getDefaultWarnThresholdFileSystem() {
		return defaultWarnThresholdFileSystem;
	}

	public void setDefaultWarnThresholdFileSystem(String defaultWarnThresholdFileSystem) {
		this.defaultWarnThresholdFileSystem = defaultWarnThresholdFileSystem;
	}

	public String getDefaultErrorThresholdFileSystem() {
		return defaultErrorThresholdFileSystem;
	}

	public void setDefaultErrorThresholdFileSystem(String defaultErrorThresholdFileSystem) {
		this.defaultErrorThresholdFileSystem = defaultErrorThresholdFileSystem;
	}

	public Boolean getGatheringCPUActive() {
		return gatheringCPUActive;
	}

	public void setGatheringCPUActive(Boolean gatheringCPUActive) {
		this.gatheringCPUActive = gatheringCPUActive;
	}

	public Boolean getGatheringMemoryActive() {
		return gatheringMemoryActive;
	}

	public void setGatheringMemoryActive(Boolean gatheringMemoryActive) {
		this.gatheringMemoryActive = gatheringMemoryActive;
	}

	public Boolean getGatheringFileSystemActive() {
		return gatheringFileSystemActive;
	}

	public void setGatheringFileSystemActive(Boolean gatheringFileSystemActive) {
		this.gatheringFileSystemActive = gatheringFileSystemActive;
	}

}
