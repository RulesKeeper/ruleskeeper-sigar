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

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartSigar {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartSigar.class);

	public StartSigar() {
	}

	public void scheduleConfigurationRefreshJob() {
		RulesKeeperSigarScheduler.INSTANCE.scheduleConfigurationRefreshJob();
	}

	public void scheduleDataPickerJob() {
		RulesKeeperSigarScheduler.INSTANCE.scheduleDataPickerJob();
	}

	public void start() {
		try {
			RulesKeeperSigarScheduler.INSTANCE.start();
		} catch (SchedulerException e) {
			LOGGER.error(e.getMessage());
		}
	}

	public static void main(String[] args) {
		StartSigar p = new StartSigar();
		p.scheduleConfigurationRefreshJob();
		p.start();
		p.scheduleDataPickerJob();
	}

}
