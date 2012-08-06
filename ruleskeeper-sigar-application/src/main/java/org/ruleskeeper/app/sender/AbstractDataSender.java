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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.app.RulesKeeperSigarConstants;
import org.ruleskeeper.app.RulesKeeperSigarScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public abstract class AbstractDataSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataSender.class);

	private static final int BYTE_MULTIPLE = 1024;

	public static final List<Integer> SUCESSS_STATUS_CODE = new ArrayList<Integer>();

	static {
		SUCESSS_STATUS_CODE.add(200);
		SUCESSS_STATUS_CODE.add(201);
		SUCESSS_STATUS_CODE.add(202);
		SUCESSS_STATUS_CODE.add(203);
		SUCESSS_STATUS_CODE.add(204);
		SUCESSS_STATUS_CODE.add(205);
		SUCESSS_STATUS_CODE.add(206);
		SUCESSS_STATUS_CODE.add(207);
		SUCESSS_STATUS_CODE.add(210);

		SUCESSS_STATUS_CODE.add(300);
		SUCESSS_STATUS_CODE.add(301);
		SUCESSS_STATUS_CODE.add(302);
		SUCESSS_STATUS_CODE.add(303);
		SUCESSS_STATUS_CODE.add(304);
		SUCESSS_STATUS_CODE.add(305);
		SUCESSS_STATUS_CODE.add(306);
		SUCESSS_STATUS_CODE.add(307);
		SUCESSS_STATUS_CODE.add(310);
	}

	private static Handler httpHandler = new Handler();

	public abstract void retrieveAndSend(Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate) throws SigarException, IllegalArgumentException,
	    IOException, InterruptedException, ExecutionException;

	public String formatAsPercent(double val) {
		String p = String.valueOf(val * RulesKeeperSigarConstants.PERCENT_MULTIPLE);

		int ix = p.indexOf('.') + 1;
		String percent = p.substring(0, ix) + p.substring(ix, ix + 1);

		return percent;
	}

	protected static Long formatToMegaByte(long value) {
		return Long.valueOf(value / BYTE_MULTIPLE / BYTE_MULTIPLE);
	}

	public void send(Metric m, String serverUrl) throws IOException, InterruptedException, IllegalArgumentException, ExecutionException {
		if (RulesKeeperSigarScheduler.INSTANCE.getWaitTimeForServerAvailability() > 0) {
			LOGGER.info("RulesKeeper Server not available, waiting " + RulesKeeperSigarScheduler.INSTANCE.getWaitTimeForServerAvailability()
			    / RulesKeeperSigarConstants.MS_TO_SECOND_MULTIPLE + " seconds, before trying to send data.");
			Thread.sleep(RulesKeeperSigarScheduler.INSTANCE.getWaitTimeForServerAvailability());
		}
		sendMetricToServer(m, serverUrl, httpHandler);
	}

	public void sendMetricToServer(Metric m, String serverUrl, AsyncCompletionHandler<Response> handler) throws IllegalArgumentException, IOException, InterruptedException,
	    ExecutionException {
		ByteString data = m.toByteString();
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		String dataAsString = data.toStringUtf8();
		Response r = asyncHttpClient.preparePost(serverUrl).addParameter("data", dataAsString).execute(handler).get();
		if (SUCESSS_STATUS_CODE.contains(r.getStatusCode())) {
			LOGGER.info("Metric sent to RulesKeeper Server: {}", r.getStatusCode());
		} else {
			LOGGER.info("Not able to send Metric to RulesKeeper Server: {}", r.getStatusCode());
		}
		asyncHttpClient.close();
	}

	public static class Handler extends AsyncCompletionHandler<Response> {
		@Override
		public Response onCompleted(Response response) {
			LOGGER.debug("" + response.getStatusCode());
			if (RulesKeeperSigarScheduler.INSTANCE.getWaitTimeForServerAvailability() > 0) {
				LOGGER.info("RulesKeeper Server is now available.");
				RulesKeeperSigarScheduler.INSTANCE.resetWaitTime();
			}
			return response;
		}

		@Override
		public void onThrowable(Throwable t) {
			LOGGER.error(t.getMessage());
			RulesKeeperSigarScheduler.INSTANCE.increaseWaitTime();
		}
	}

}
