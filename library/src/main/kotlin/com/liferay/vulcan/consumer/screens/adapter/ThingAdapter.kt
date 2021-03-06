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

package com.liferay.vulcan.consumer.screens.adapter

import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import android.view.ViewGroup
import com.liferay.vulcan.consumer.R
import com.liferay.vulcan.consumer.delegates.convert
import com.liferay.vulcan.consumer.extensions.inflate
import com.liferay.vulcan.consumer.fetch
import com.liferay.vulcan.consumer.model.Collection
import com.liferay.vulcan.consumer.model.Thing
import com.liferay.vulcan.consumer.screens.views.BaseView
import com.liferay.vulcan.consumer.screens.views.Scenario
import okhttp3.HttpUrl

class ThingAdapter(collection: Collection, val listener: Listener) :
	Adapter<ThingViewHolder>(), ThingViewHolder.Listener {

	override fun onLayoutRow(view: BaseView?, thing: Thing, scenario: Scenario) =
		listener.onLayoutRow(view, thing, scenario)

	override fun onClickedRow(view: View, thing: Thing): View.OnClickListener? = listener.onClickedRow(view, thing)

	val totalItems = collection.totalItems
	val members = collection.members?.toMutableList() ?: mutableListOf()

	val nextPage = collection.pages?.next

	override fun onBindViewHolder(holder: ThingViewHolder?, position: Int) {
		if (members.size > position) {
			holder?.thing = members[position]
		} else {
			nextPage.let {
				HttpUrl.parse(nextPage)?.let {
					fetch(it) {
						it.fold(
							success = {
								convert<Collection>(it)?.let {
									val moreMembers = it.members
									merge(members, moreMembers)
									notifyDataSetChanged()
								}
							},
							failure = {}
						)
					}
				}

			}
		}
	}

	override fun getItemViewType(position: Int): Int {
		return position
	}

	private fun merge(members: MutableList<Thing>, moreMembers: List<Thing>?) {
		moreMembers?.apply { members.addAll(this) }
	}

	override fun getItemCount(): Int = totalItems ?: 0

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ThingViewHolder? {
		return parent?.inflate(R.layout.thing_viewholder_default)?.let {
			ThingViewHolder(it, this)
		}
	}

	interface Listener {
		fun onClickedRow(view: View, thing: Thing): View.OnClickListener?
		fun onLayoutRow(view: BaseView?, thing: Thing, scenario: Scenario): Int?
	}
}
