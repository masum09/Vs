package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DeviceTelemetryManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Crimson Vitals", appName)
    }

    @Test
    fun `verify offline audit report generation`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val telemetryManager = DeviceTelemetryManager(context)
        val report = telemetryManager.generateAuditReport()

        assertNotNull(report)
        assertTrue(report.contains("CRIMSON VITALS // HARDWARE AUDIT DOSSIER"))
        assertTrue(report.contains("100% Offline"))
        assertTrue(report.contains("OPERATING SYSTEM"))
        assertTrue(report.contains("VOLATILE MEMORY"))
    }
}
