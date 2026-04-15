package com.guardian.track.util

import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.sqrt

class AccelerometerFilterTest {

    @Test
    fun `LowPassFilter initializes correctly on first call`() = runTest {
        val filter = LowPassFilter(alpha = 0.5f)

        val result = filter.filter(10f, 20f, 30f)

        assertEquals(10f, result.x, 0.001f)
        assertEquals(20f, result.y, 0.001f)
        assertEquals(30f, result.z, 0.001f)
        assertEquals(sqrt(10f * 10f + 20f * 20f + 30f * 30f), result.magnitude, 0.001f)
    }

    @Test
    fun `LowPassFilter applies smoothing on subsequent calls`() = runTest {
        val filter = LowPassFilter(alpha = 0.5f)

        filter.filter(0f, 0f, 0f)
        val result = filter.filter(10f, 20f, 30f)

        assertEquals(5f, result.x, 0.001f)
        assertEquals(10f, result.y, 0.001f)
        assertEquals(15f, result.z, 0.001f)
    }

    @Test
    fun `LowPassFilter reset clears state`() = runTest {
        val filter = LowPassFilter(alpha = 0.5f)

        filter.filter(10f, 20f, 30f)
        filter.reset()
        val result = filter.filter(5f, 10f, 15f)

        assertEquals(5f, result.x, 0.001f)
        assertEquals(10f, result.y, 0.001f)
        assertEquals(15f, result.z, 0.001f)
    }

    @Test
    fun `AccelerometerFilter processSensorData returns valid result`() = runTest {
        val result = AccelerometerFilter.processSensorData(0f, 0f, 9.81f, 1000000L)

        assertNotNull(result)
        assertTrue(result.rawMagnitude > 0)
        assertFalse(result.isNoise)
    }

    @Test
    fun `AccelerometerFilter detects noise when magnitude is low`() = runTest {
        val result = AccelerometerFilter.processSensorData(0.1f, 0.1f, 0.1f, 2000000L)

        assertTrue(result.isNoise)
    }

    @Test
    fun `AccelerometerFilter reset clears internal state`() = runTest {
        AccelerometerFilter.processSensorData(10f, 20f, 30f, 1000000L)
        AccelerometerFilter.reset()
        val result = AccelerometerFilter.processSensorData(0f, 0f, 0f, 2000000L)

        assertEquals(0f, result.gravity[0], 0.1f)
        assertEquals(0f, result.gravity[1], 0.1f)
    }

    @Test
    fun `AccelerometerResult equals works correctly`() = runTest {
        val result1 = AccelerometerFilter.AccelerometerResult(
            rawMagnitude = 10f,
            filteredMagnitude = 9f,
            linearMagnitude = 1f,
            highPassMagnitude = 0.5f,
            isNoise = false,
            gravity = floatArrayOf(1f, 2f, 3f)
        )
        val result2 = AccelerometerFilter.AccelerometerResult(
            rawMagnitude = 10f,
            filteredMagnitude = 9f,
            linearMagnitude = 1f,
            highPassMagnitude = 0.5f,
            isNoise = false,
            gravity = floatArrayOf(1f, 2f, 3f)
        )

        assertEquals(result1, result2)
    }

    @Test
    fun `AccelerometerResult hashCode is consistent`() = runTest {
        val result = AccelerometerFilter.AccelerometerResult(
            rawMagnitude = 10f,
            filteredMagnitude = 9f,
            linearMagnitude = 1f,
            highPassMagnitude = 0.5f,
            isNoise = false,
            gravity = floatArrayOf(1f, 2f, 3f)
        )

        assertEquals(result.hashCode(), result.hashCode())
    }

    @Test
    fun `LowPassFilter magnitude calculation is correct`() = runTest {
        val filter = LowPassFilter(alpha = 1f)

        val result = filter.filter(3f, 4f, 0f)

        assertEquals(5f, result.magnitude, 0.001f)
    }

    @Test
    fun `AccelerometerFilter handles high acceleration values`() = runTest {
        val result = AccelerometerFilter.processSensorData(100f, -50f, 75f, 1000000L)

        assertEquals(100f, result.rawMagnitude, 0.001f)
        assertTrue(result.rawMagnitude > result.filteredMagnitude)
    }
}

class FlowTurbineTest {
    @Test
    fun `turbine test can collect flow values`() = runTest {
        val flow = kotlinx.coroutines.flow.flowOf(1, 2, 3)

        flow.test {
            assertEquals(1, awaitItem())
            assertEquals(2, awaitItem())
            assertEquals(3, awaitItem())
            awaitComplete()
        }
    }
}