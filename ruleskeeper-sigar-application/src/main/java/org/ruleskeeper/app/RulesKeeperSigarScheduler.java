package org.ruleskeeper.app;

import java.text.ParseException;
import java.util.Date;

import org.hyperic.sigar.Sigar;
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

	protected static final String SIGAR_OBJECT_QUARTZ_PARAM = "SIGAR_OBJECT";
	protected static final String SERVER_URL_QUARTZ_PARAM = "SERVER_URL";

	private static final String CONF_CHECKER_REFRESH_CRON_EXPRESSION = "0 0/10 * * * ?";

	private String refreshCronExpression = null;
	private String serverUrl = null;

	private Scheduler scheduler = null;

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

	private static JobDataMap translate(Sigar sigar, String serverUrl) {
		JobDataMap dataMap = new JobDataMap();
		if (sigar != null) {
			dataMap.put(SIGAR_OBJECT_QUARTZ_PARAM, sigar);
			dataMap.put(SERVER_URL_QUARTZ_PARAM, serverUrl);
		}
		return dataMap;
	}

	public void schedule() {
		try {
			Sigar sigar = new Sigar();

			getScheduler().deleteJob("SystemData", "Scheduled");

			JobDetail job = new JobDetail("SystemData", "SIGAR", SigarDataPickerJob.class);
			JobDataMap dataMap = translate(sigar, getServerUrl());
			job.setJobDataMap(dataMap);
			CronTrigger cronTrigger = new CronTrigger("SystemDataTrigger", "Scheduled", getRefreshCronExpression());
			getScheduler().scheduleJob(job, cronTrigger);

			JobDetail confChecker = new JobDetail("ConfChecked", "SIGAR", SigarConfigurationRefreshJob.class);
			CronTrigger confCheckerTrigger = new CronTrigger("SystemDataTrigger", "Scheduled", CONF_CHECKER_REFRESH_CRON_EXPRESSION);
			getScheduler().scheduleJob(confChecker, confCheckerTrigger);

			JobDetail jobNow = new JobDetail("TriggerNow", "SIGAR", SigarDataPickerJob.class);
			jobNow.setJobDataMap(dataMap);
			SimpleTrigger nowTrigger = new SimpleTrigger("TriggerNow", "NOW", new Date(), null, 0, 0L);
			getScheduler().scheduleJob(jobNow, nowTrigger);

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

}
