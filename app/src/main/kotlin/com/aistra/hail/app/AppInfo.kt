package com.aistra.hail.app

import android.content.pm.ApplicationInfo
import com.aistra.hail.HailApp
import com.aistra.hail.utils.HPackages

class AppInfo(val packageName: String, var tagId: Int) {
    val applicationInfo: ApplicationInfo? get() = HPackages.getApplicationInfoOrNull(packageName)
    val name get() = applicationInfo?.loadLabel(HailApp.app.packageManager) ?: packageName
    val icon
        get() = applicationInfo?.loadIcon(HailApp.app.packageManager)
            ?: HailApp.app.packageManager.defaultActivityIcon

    var state: Int = getCurrentState()
    fun getCurrentState(): Int = when {
        applicationInfo == null -> STATE_UNKNOWN
        AppManager.isAppFrozen(packageName) -> STATE_FROZEN
        else -> STATE_UNFROZEN
    }

    var selected: Boolean = false
    fun isNowSelected(selectedList: List<AppInfo>): Boolean = this in selectedList

    fun setTag(id: Int) {
        tagId = id
        HailData.saveApps()
    }

    override fun equals(other: Any?): Boolean = other is AppInfo && other.packageName == packageName
    override fun hashCode(): Int = packageName.hashCode()

    companion object {
        const val STATE_UNKNOWN = 0
        const val STATE_UNFROZEN = 1
        const val STATE_FROZEN = 2
    }
}