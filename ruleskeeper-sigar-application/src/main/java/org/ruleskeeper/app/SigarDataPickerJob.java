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

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.MetricProtoBuf.Metric.UnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class SigarDataPickerJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SigarDataPickerJob.class);
	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	private static final String MEASURE_CPU_PREFIX = "system.cpu.";

	private static final String SAVE_MEASURE_VALUE_API_PATH = "/api/metrics/save";

	private static final Double PERCENT_MULTIPLE = 100.0D;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info("refreshing System Info: START");

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Sigar sigar = (Sigar) dataMap.get(StartSigar.SIGAR_OBJECT_QUARTZ_PARAM);
		String serverUrl = (String) dataMap.get(StartSigar.SERVER_URL_QUARTZ_PARAM);
		String serverUrlAPI = serverUrl + SAVE_MEASURE_VALUE_API_PATH;

		try {
			String hostName = sigar.getNetInfo().getHostName();
			getNetInfo(sigar);

			getCPUInfo(sigar, serverUrlAPI, hostName);

		} catch (SigarException e) {
			LOGGER.error("Not able to gather system data: %s", e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Not able to publish data: %s", e.getMessage());
		}

		LoggerFactory.getLogger(LOGGER_INFO).info("refreshing System Info: END");
	}

	private void getNetInfo(Sigar sigar) throws SigarException {
		LOGGER.debug(sigar.getNetInfo().getHostName());
		LOGGER.debug(sigar.getNetInfo().getDefaultGateway());
		LOGGER.debug(sigar.getNetInfo().getDomainName());
		LOGGER.debug(sigar.getNetInfo().getPrimaryDns());
		LOGGER.debug(sigar.getNetInfo().getSecondaryDns());
	}

	private void getCPUInfo(Sigar sigar, String serverUrl, String hostName) throws SigarException, IOException {
		LoggerFactory.getLogger(LOGGER_INFO).info("refreshing CPU Info: START");
		org.hyperic.sigar.CpuInfo[] infos = sigar.getCpuInfoList();
		org.hyperic.sigar.CpuInfo info = infos[0];

		sendCpuInfo(serverUrl, hostName, info);
		sendTotalCPU(sigar, serverUrl, hostName);
		sendDetailedCPU(sigar, serverUrl, hostName);
		LoggerFactory.getLogger(LOGGER_INFO).info("refreshing CPU Info: END");
	}

	private void sendDetailedCPU(Sigar sigar, String serverUrl, String hostName) throws SigarException, IOException {
		CpuPerc[] cpus = sigar.getCpuPercList();
		for (int i = 0; i < cpus.length; i++) {
			String key = MEASURE_CPU_PREFIX + i;
			LOGGER.debug(key);
			LOGGER.debug("" + cpus[i]);

			String value = formatAsPercent(cpus[i].getUser());
			Metric m = Metric.newBuilder().setDataProvider(hostName).setKey(key + ".user").setUnit(UnitType.COUNTER).setValue(value).build();
			sendMetricData(m, serverUrl);

			value = formatAsPercent(cpus[i].getSys());
			m = Metric.newBuilder().setDataProvider(hostName).setKey(key + ".system").setUnit(UnitType.COUNTER).setValue(value).build();
			sendMetricData(m, serverUrl);

			value = formatAsPercent(cpus[i].getNice());
			m = Metric.newBuilder().setDataProvider(hostName).setKey(key + ".nice").setUnit(UnitType.COUNTER).setValue(value).build();
			sendMetricData(m, serverUrl);

			value = formatAsPercent(cpus[i].getWait());
			m = Metric.newBuilder().setDataProvider(hostName).setKey(key + ".wait").setUnit(UnitType.COUNTER).setValue(value).build();
			sendMetricData(m, serverUrl);

			value = formatAsPercent(cpus[i].getIdle());
			m = Metric.newBuilder().setDataProvider(hostName).setKey(key + ".idle").setUnit(UnitType.COUNTER).setValue(value).build();
			sendMetricData(m, serverUrl);
		}
	}

	private void sendTotalCPU(Sigar sigar, String serverUrl, String hostName) throws SigarException, IOException {
		CpuPerc cpuAll = sigar.getCpuPerc();
		String value = formatAsPercent(cpuAll.getUser());
		Metric m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "total.user").setUnit(UnitType.PERCENT).setValue(value).build();
		sendMetricData(m, serverUrl);

		value = formatAsPercent(cpuAll.getSys());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "total.system").setUnit(UnitType.PERCENT).setValue(value).build();
		sendMetricData(m, serverUrl);

		value = formatAsPercent(cpuAll.getNice());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "total.nice").setUnit(UnitType.PERCENT).setValue(value).build();
		sendMetricData(m, serverUrl);

		value = formatAsPercent(cpuAll.getWait());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "total.wait").setUnit(UnitType.PERCENT).setValue(value).build();
		sendMetricData(m, serverUrl);

		value = formatAsPercent(cpuAll.getIdle());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "total.idle").setUnit(UnitType.PERCENT).setValue(value).build();
		sendMetricData(m, serverUrl);
	}

	private void sendCpuInfo(String serverUrl, String hostName, org.hyperic.sigar.CpuInfo info) throws IOException {
		LOGGER.debug("Vendor: " + info.getVendor());
		Metric m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "vendor").setUnit(UnitType.STRING).setValue(info.getVendor()).build();
		sendMetricData(m, serverUrl);

		LOGGER.debug("Model: " + info.getModel());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "model").setUnit(UnitType.STRING).setValue(info.getModel()).build();
		sendMetricData(m, serverUrl);

		LOGGER.debug("Mhz: " + info.getMhz());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "frequency").setUnit(UnitType.STRING).setValue("" + info.getMhz()).build();
		sendMetricData(m, serverUrl);

		LOGGER.debug("Total Cores: " + info.getTotalCores());
		m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "cores").setUnit(UnitType.COUNTER).setValue("" + info.getTotalCores()).build();
		sendMetricData(m, serverUrl);

		if ((info.getTotalCores() != info.getTotalSockets()) || (info.getCoresPerSocket() > info.getTotalCores())) {
			LOGGER.debug("Physical CPUs: " + info.getTotalSockets());
			m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "physicals").setUnit(UnitType.COUNTER).setValue("" + info.getTotalSockets()).build();
			sendMetricData(m, serverUrl);

			LOGGER.debug("Cores per CPU: " + info.getCoresPerSocket());
			m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "corespercpu").setUnit(UnitType.COUNTER).setValue("" + info.getCoresPerSocket()).build();
			sendMetricData(m, serverUrl);
		}

		long cacheSize = info.getCacheSize();
		if (cacheSize != Sigar.FIELD_NOTIMPL) {
			LOGGER.debug("Cache Size: " + cacheSize);
			m = Metric.newBuilder().setDataProvider(hostName).setKey(MEASURE_CPU_PREFIX + "cachesize").setUnit(UnitType.COUNTER).setValue("" + info.getCacheSize()).build();
			sendMetricData(m, serverUrl);
		}
	}

	public String formatAsPercent(double val) {
		String p = String.valueOf(val * PERCENT_MULTIPLE);

		int ix = p.indexOf('.') + 1;
		String percent = p.substring(0, ix) + p.substring(ix, ix + 1);

		return percent;
	}

	private static Handler httpHandler = new Handler();

	private void sendMetricData(Metric m, String serverUrl) throws IOException {
		// LOGGER.info(m.toString());
		byte[] data = m.toByteArray();
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

		String dataAsString = new String(data, "UTF-8");
		asyncHttpClient.preparePost(serverUrl).addParameter("data", dataAsString).execute(httpHandler);
	}

	public static class Handler extends AsyncCompletionHandler<Response> {
		@Override
		public Response onCompleted(Response response) throws Exception {
			LOGGER.info("" + response.getStatusCode());
			return response;
		}

		@Override
		public void onThrowable(Throwable t) {
			LOGGER.error(t.getMessage());
		}
	}
}