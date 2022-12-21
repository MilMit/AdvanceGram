/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2020 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2020 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.github.shadowsocks.plugin

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import org.telegram.messenger.ApplicationLoader
import org.telegram.messenger.LocaleController
import org.telegram.messenger.R

@RequiresApi(Build.VERSION_CODES.KITKAT) class PluginList : ArrayList<Plugin>() {
    init {
        add(NoPlugin)
        addAll(ApplicationLoader.applicationContext.packageManager.queryIntentContentProviders(
                Intent(PluginContract.ACTION_NATIVE_PLUGIN), PackageManager.GET_META_DATA)
                .map { NativePlugin(it) })
    }

    val lookup = mutableMapOf<String, Plugin>().apply {
        for (plugin in this@PluginList) {
            fun check(old: Plugin?) = check(old == null || old === plugin) { LocaleController.formatString("SSPluginConflictingName",R.string.SSPluginConflictingName,plugin.id) }
            check(put(plugin.id, plugin))
        }
    }

    val lookupNames get() = lookup.values.map {
        if (it.label.isNotBlank() && it.id.isNotBlank()) {
            "${it.label} (${it.id})"
        } else if (it.label.isNotBlank()) {
            it.label
        } else if (it.id.isNotBlank()) {
            it.id
        } else it.packageName
    }.map { it.takeIf { it.isNotBlank() } ?: LocaleController.getString("Disable", R.string.Disable) }.toTypedArray()
}
