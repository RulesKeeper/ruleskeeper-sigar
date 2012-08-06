package org.ruleskeeper.app.sender;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.junit.Test;
import org.ruleskeeper.app.SigarConfigurationRefreshJob;
import org.ruleskeeper.app.SigarDataPickerJob;

public class FileSystemInfoSenderTest {

	@Test
	public void retrieveAndSend() throws IllegalArgumentException, SigarException, IOException, InterruptedException, ExecutionException {
		FileSystemInfoSender sender = new FileSystemInfoSender();

		Sigar sigar = new Sigar();
		SigarProxy sigarProxy = SigarProxyCache.newInstance(sigar);

		String serverUrl = SigarConfigurationRefreshJob.DEFAULT_SERVER_URL;
		String apiURL = serverUrl + SigarDataPickerJob.SAVE_MEASURE_VALUE_API_PATH;

		String hostName = "JUnitHostName";
		Date eventDate = new Date();

		sender.retrieveAndSend(sigar, sigarProxy, apiURL, hostName, eventDate);
	}
}
