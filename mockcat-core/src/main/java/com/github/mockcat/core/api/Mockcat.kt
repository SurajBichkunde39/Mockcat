package com.github.mockcat.core.api

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.getSystemService
import com.github.mockcat.core.MockcatActivity
import com.github.mockcat.core.R

object Mockcat {
    private const val SHORTCUT_ID = "mockcatShortcutId"

    fun getLaunchIntent(context: Context): Intent {
        return Intent(context, MockcatActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * This is temporary call for `createShortcut` here, mostly only for testing.
     * Todo: Move this call to init logic of core-lib once all the setup is done.
     * Also Make this internal, this should not be part of public API.
     */
    fun createShortcut(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            return
        }

        val shortcutManager = context.getSystemService<ShortcutManager>() ?: return
        if (shortcutManager.dynamicShortcuts.any { it.id == SHORTCUT_ID }) {
            return
        }
        val shortcut =
            ShortcutInfo.Builder(context, SHORTCUT_ID)
                .setShortLabel(context.getString(R.string.mockcat_shortcut_label))
                .setLongLabel(context.getString(R.string.mockcat_shortcut_label))
                .setIcon(Icon.createWithResource(context, R.drawable.mockcat_ic_launcher))
                .setIntent(getLaunchIntent(context).setAction(Intent.ACTION_VIEW))
                .build()

        try {
            shortcutManager.addDynamicShortcuts(listOf(shortcut))
        } catch (e: IllegalArgumentException) {
            // Logger.warn("ShortcutManager addDynamicShortcuts failed ", e)
        } catch (e: IllegalStateException) {
            // Logger.warn("ShortcutManager addDynamicShortcuts failed ", e)
        }
    }
}
