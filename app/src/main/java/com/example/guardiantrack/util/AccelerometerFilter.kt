package com.example.guardiantrack.util

import kotlin.math.sqrt

class LowPassFilter(
    private val alpha: Float = 0.15f
) {
    private var isInitialized = false
    private var filteredX = 0f
    private var filteredY = 0f
    private var filteredZ = 0f

    fun filter(rawX: Float, rawY: Float, rawZ: Float): FilteredData {
        if (!isInitialized) {
            filteredX = rawX
            filteredY = rawY
            filteredZ = rawZ
            isInitialized = true
        } else {
            filteredX = alpha * rawX + (1 - alpha) * filteredX
            filteredY = alpha * rawY + (1 - alpha) * filteredY
            filteredZ = alpha * rawZ + (1 - alpha) * filteredZ
        }
        return FilteredData(filteredX, filteredY, filteredZ)
    }

    fun reset() {
        isInitialized = false
        filteredX = 0f
        filteredY = 0f
        filteredZ = 0f
    }

    data class FilteredData(
        val x: Float,
        val y: Float,
        val z: Float
    ) {
        val magnitude: Float
            get() = sqrt(x * x + y * y + z * z)
    }
}

object AccelerometerFilter {
    private const val LOW_PASS_ALPHA = 0.1f
    private const val HIGH_PASS_CUTOFF = 2.0f
    private const val NOISE_THRESHOLD = 0.5f

    private val lowPassFilter = LowPassFilter(alpha = LOW_PASS_ALPHA)
    private var gravity = FloatArray(3)
    private var lastTimestamp: Long = 0

    fun processSensorData(
        rawX: Float,
        rawY: Float,
        rawZ: Float,
        timestamp: Long
    ): AccelerometerResult {
        val dt = if (lastTimestamp > 0) (timestamp - lastTimestamp) / 1_000_000f else 0f
        lastTimestamp = timestamp

        val filtered = lowPassFilter.filter(rawX, rawY, rawZ)

        val linearAcceleration = FloatArray(3)
        linearAcceleration[0] = rawX - gravity[0]
        linearAcceleration[1] = rawY - gravity[1]
        linearAcceleration[2] = rawZ - gravity[2]

        gravity[0] += LOW_PASS_ALPHA * (rawX - gravity[0])
        gravity[1] += LOW_PASS_ALPHA * (rawY - gravity[1])
        gravity[2] += LOW_PASS_ALPHA * (rawZ - gravity[2])

        val linearMagnitude = sqrt(
            linearAcceleration[0] * linearAcceleration[0] +
            linearAcceleration[1] * linearAcceleration[1] +
            linearAcceleration[2] * linearAcceleration[2]
        )

        val isNoise = linearMagnitude < NOISE_THRESHOLD

        val highPassFiltered = if (dt > 0) {
            val rc = 1f / (2f * Math.PI.toFloat() * HIGH_PASS_CUTOFF)
            val highPassAlpha = dt / (dt + rc)
            FloatArray(3).apply {
                this[0] = highPassAlpha * (gravity.getOrNull(0)?.let { rawX - it } ?: 0f)
                this[1] = highPassAlpha * (gravity.getOrNull(1)?.let { rawY - it } ?: 0f)
                this[2] = highPassAlpha * (gravity.getOrNull(2)?.let { rawZ - it } ?: 0f)
            }
        } else {
            linearAcceleration
        }

        return AccelerometerResult(
            rawMagnitude = sqrt(rawX * rawX + rawY * rawY + rawZ * rawZ),
            filteredMagnitude = filtered.magnitude,
            linearMagnitude = linearMagnitude,
            highPassMagnitude = sqrt(highPassFiltered[0] * highPassFiltered[0] +
                                     highPassFiltered[1] * highPassFiltered[1] +
                                     highPassFiltered[2] * highPassFiltered[2]),
            isNoise = isNoise,
            gravity = gravity.copyOf()
        )
    }

    fun reset() {
        lowPassFilter.reset()
        gravity = FloatArray(3)
        lastTimestamp = 0
    }

    data class AccelerometerResult(
        val rawMagnitude: Float,
        val filteredMagnitude: Float,
        val linearMagnitude: Float,
        val highPassMagnitude: Float,
        val isNoise: Boolean,
        val gravity: FloatArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as AccelerometerResult
            if (rawMagnitude != other.rawMagnitude) return false
            if (filteredMagnitude != other.filteredMagnitude) return false
            if (linearMagnitude != other.linearMagnitude) return false
            if (highPassMagnitude != other.highPassMagnitude) return false
            if (isNoise != other.isNoise) return false
            return gravity.contentEquals(other.gravity)
        }

        override fun hashCode(): Int {
            var result = rawMagnitude.hashCode()
            result = 31 * result + filteredMagnitude.hashCode()
            result = 31 * result + linearMagnitude.hashCode()
            result = 31 * result + highPassMagnitude.hashCode()
            result = 31 * result + isNoise.hashCode()
            result = 31 * result + gravity.contentHashCode()
            return result
        }
    }
}