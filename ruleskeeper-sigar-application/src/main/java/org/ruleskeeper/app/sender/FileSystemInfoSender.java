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

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.ruleskeeper.MetricProtoBuf.Metric;
import org.ruleskeeper.MetricProtoBuf.Metric.UnitType;
import org.ruleskeeper.app.RulesKeeperSigarScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: complete with info retrieved in Iostat.java NetInfo
 */
public class FileSystemInfoSender extends AbstractDataSender {

	private static final String SYS_TYPE_CDROM = "cdrom";
	private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemInfoSender.class);
	private static final String LOGGER_INFO = "org.ruleskeeper.INFO";

	private static final String SENDER_NAME = "File System Info";

	private static final String MEASURE_FILESYSTEM_PREFIX = "system.filesystem";

	public FileSystemInfoSender() {
	}

	@Override
	public void retrieveAndSend(Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate) throws SigarException, IllegalArgumentException, IOException,
	    InterruptedException, ExecutionException {
		LoggerFactory.getLogger(LOGGER_INFO).info(SENDER_NAME + ": START");

		FileSystem[] fslist = sigarProxy.getFileSystemList();
		int countFS = 0;
		if (fslist != null) {
			for (int i = 0; i < fslist.length; i++) {
				boolean isRealFileSystem = retrieveAndSendFSDetails(fslist[i], i, sigar, sigarProxy, serverUrl, hostName, eventDate);
				if (isRealFileSystem) {
					countFS++;
				}
			}
		}
		String value = String.valueOf(countFS);
		LOGGER.debug("# FileSystem: {}", value);
		Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX).setTechnicalKey("count").setShortName("# FileSystem")
		    .setUnit(UnitType.COUNTER).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
		send(m, serverUrl);

		LoggerFactory.getLogger(LOGGER_INFO).info(SENDER_NAME + ": END");
	}

	private boolean retrieveAndSendFSDetails(FileSystem fs, int fsId, Sigar sigar, SigarProxy sigarProxy, String serverUrl, String hostName, Date eventDate)
	    throws IllegalArgumentException, IOException, InterruptedException, ExecutionException {
		boolean isRealFileSystem = Boolean.FALSE;

		long used, avail, total;
		used = avail = total = 0;

		try {
			FileSystemUsage usage;
			if (fs instanceof NfsFileSystem) {
				NfsFileSystem nfs = (NfsFileSystem) fs;
				if (!nfs.ping()) {
					LOGGER.warn(nfs.getUnreachableMessage());
					return isRealFileSystem;
				}
			}

			if (!SYS_TYPE_CDROM.equalsIgnoreCase(fs.getSysTypeName())) {
				usage = sigar.getFileSystemUsage(fs.getDirName());
				used = usage.getTotal() - usage.getFree();
				avail = usage.getAvail();
				total = usage.getTotal();
			}
		} catch (SigarException e) {
			// e.g. on win32 D:\ fails with "Device not ready"
			// if there is no cd in the drive.
			used = avail = total = 0;
		}

		if (!SYS_TYPE_CDROM.equalsIgnoreCase(fs.getSysTypeName())) {

			String value = fs.getDevName();
			LOGGER.debug("File System Name: {}", value);
			Metric m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("name").setShortName("File System Name")
			    .setUnit(UnitType.STRING).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = fs.getDirName();
			LOGGER.debug("Mounted On: {}", value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("mountPath").setShortName("Mounted On")
			    .setUnit(UnitType.STRING).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = fs.getSysTypeName() + "/" + fs.getTypeName();
			LOGGER.debug("Mounted On: {}", value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("type").setShortName("FS Type")
			    .setUnit(UnitType.STRING).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = String.valueOf(formatToMegaByte(total));
			LOGGER.debug("FS Space Total: {}", value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("total").setShortName("FS Space Total")
			    .setUnit(UnitType.MEGABYTES).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			value = String.valueOf(formatToMegaByte(used));
			LOGGER.debug("FS Space Used: {}", value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("used").setShortName("FS Space Used")
			    .setUnit(UnitType.MEGABYTES).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);

			Double usedP = Double.valueOf(used) / Double.valueOf(total);
			value = String.valueOf(formatAsPercent(usedP));
			LOGGER.debug("FS Space Used %: {}", value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("usedPerCent").setShortName("FS Space Used %")
			    .setUnit(UnitType.PERCENT).setEventDate(String.valueOf(eventDate.getTime())).setValue(value)
			    .setDefaultWarningThreshold(String.valueOf(RulesKeeperSigarScheduler.INSTANCE.getDefaultWarnThresholdFileSystem()))
			    .setDefaultErrorThreshold(String.valueOf(RulesKeeperSigarScheduler.INSTANCE.getDefaultErrorThresholdFileSystem())).build();
			send(m, serverUrl);

			value = String.valueOf(formatToMegaByte(avail));
			LOGGER.debug("FS Space Available: {}", value);
			m = Metric.newBuilder().setDataProvider(hostName).setHierarchyKey(MEASURE_FILESYSTEM_PREFIX + "." + fsId).setTechnicalKey("available").setShortName("FS Space Available")
			    .setUnit(UnitType.MEGABYTES).setEventDate(String.valueOf(eventDate.getTime())).setValue(value).build();
			send(m, serverUrl);
			isRealFileSystem = Boolean.TRUE;
		}
		return isRealFileSystem;
	}
}
