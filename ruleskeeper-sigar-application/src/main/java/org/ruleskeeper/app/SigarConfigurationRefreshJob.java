package org.ruleskeeper.app;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;

public class SigarConfigurationRefreshJob implements Job {

	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("checking if configuration has changed");
		boolean confChanged = Boolean.FALSE;

		// read the configuration from file system
		// if the configuration changed, update application consequently

		if (confChanged) {
			LoggerFactory.getLogger(LOGGER_INFO).info("configuration changes detected, update application consequently");

			RulesKeeperSigarScheduler.INSTANCE.setRefreshCronExpression("0 0/1 * * * ?");
			RulesKeeperSigarScheduler.INSTANCE.schedule();
		} else {
			LoggerFactory.getLogger(LOGGER_INFO).info("no configuration change detected");
		}
	}
}
