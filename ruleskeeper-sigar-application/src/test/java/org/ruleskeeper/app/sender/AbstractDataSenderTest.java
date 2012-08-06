package org.ruleskeeper.app.sender;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.junit.Test;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.MetricProtoBuf.Metric.UnitType;
import org.ruleskeeper.app.SigarConfigurationRefreshJob;
import org.ruleskeeper.app.SigarDataPickerJob;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;

public class AbstractDataSenderTest {

	private static Handler httpHandler = new Handler();

	public static class Handler extends AsyncCompletionHandler<Response> {
		@Override
		public Response onCompleted(Response response) {
			System.out.println("onCompleted");
			return response;
		}

		@Override
		public void onThrowable(Throwable t) {
			System.out.println("onThrowable");
			t.printStackTrace();
		}
	}

	@Test
	public void sendMetricToServerTest() throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
		AbstractDataSender a = new AbstractDataSender() {

			@Override
			public void retrieveAndSend(Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate) throws SigarException, IOException, InterruptedException {
				System.out.println("retrieveAndSend");
			}
		};

		String serverUrl = SigarConfigurationRefreshJob.DEFAULT_SERVER_URL;
		String apiURL = serverUrl + SigarDataPickerJob.SAVE_MEASURE_VALUE_API_PATH;

		Double value = Math.random();
		Date eventDate = new Date();
		Metric m = Metric.newBuilder().setDataProvider("JUnitTest").setHierarchyKey("junit").setTechnicalKey("junit.measure1").setShortName("JUnit Measure 1")
		    .setUnit(UnitType.COUNTER).setEventDate(String.valueOf(eventDate.getTime())).setValue(String.valueOf(value)).build();

		a.sendMetricToServer(m, apiURL, httpHandler);
	}
}
