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
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ruleskeeper.app.sender.CPUInfoSender;
import org.ruleskeeper.app.sender.FileSystemInfoSender;
import org.ruleskeeper.app.sender.MemoryInfoSender;
import org.ruleskeeper.app.sender.ServerInfoSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SigarDataPickerJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SigarDataPickerJob.class);
	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	public static final String SAVE_MEASURE_VALUE_API_PATH = "/api/metrics/save";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("System Data Gathering: START");

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Sigar sigar = (Sigar) dataMap.get(RulesKeeperSigarScheduler.SIGAR_OBJECT_QUARTZ_PARAM);
		SigarProxy sigarProxy = (SigarProxy) dataMap.get(RulesKeeperSigarScheduler.SIGAR_PROXY_OBJECT_QUARTZ_PARAM);
		String serverUrl = (String) dataMap.get(RulesKeeperSigarScheduler.SERVER_URL_QUARTZ_PARAM);
		String serverUrlAPI = serverUrl + SAVE_MEASURE_VALUE_API_PATH;

		try {
			String hostName = sigar.getNetInfo().getHostName();
			Date eventDate = new Date();
			getNetInfo(sigar);

			ServerInfoSender serverInfoSender = new ServerInfoSender();
			serverInfoSender.retrieveAndSend(sigar, sigarProxy, serverUrlAPI, hostName, eventDate);

			if (RulesKeeperSigarScheduler.INSTANCE.getGatheringCPUActive()) {
				CPUInfoSender cpuSender = new CPUInfoSender();
				cpuSender.retrieveAndSend(sigar, sigarProxy, serverUrlAPI, hostName, eventDate);
			}
			if (RulesKeeperSigarScheduler.INSTANCE.getGatheringMemoryActive()) {
				MemoryInfoSender memorySender = new MemoryInfoSender();
				memorySender.retrieveAndSend(sigar, sigarProxy, serverUrlAPI, hostName, eventDate);
			}
			if (RulesKeeperSigarScheduler.INSTANCE.getGatheringFileSystemActive()) {
				FileSystemInfoSender fsender = new FileSystemInfoSender();
				fsender.retrieveAndSend(sigar, sigarProxy, serverUrlAPI, hostName, eventDate);
			}

		} catch (SigarException e) {
			LOGGER.error("Not able to gather system data: {}", e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Not able to publish data: {}", e.getMessage());
		} catch (InterruptedException e) {
			LOGGER.error("Not able to publish data: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.error("Not able to publish data: {}", e.getMessage());
		} catch (ExecutionException e) {
			LOGGER.error("Not able to publish data: {}", e.getMessage());
		}

		LoggerFactory.getLogger(LOGGER_INFO).info("System Data Gathering: END");
	}

	private void getNetInfo(Sigar sigar) throws SigarException {
		LOGGER.debug(sigar.getNetInfo().getHostName());
		LOGGER.debug(sigar.getNetInfo().getDefaultGateway());
		LOGGER.debug(sigar.getNetInfo().getDomainName());
		LOGGER.debug(sigar.getNetInfo().getPrimaryDns());
		LOGGER.debug(sigar.getNetInfo().getSecondaryDns());
	}

}
