/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.vulcan.consumer.screens.views

import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.events.Event
import com.liferay.vulcan.consumer.screens.ThingScreenlet
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

/**
 * @author Alejandro Hernández
 */
interface BaseView : AnkoLogger {

	var screenlet: ThingScreenlet?

	fun <T> sendEvent(event: Event<T>): T? = screenlet?.onEventFor(event)

	var thing: Thing?

	fun onDestroy() {}

	fun showError(message: String?) {
		error { "Error loading the thing " + message }
	}

}