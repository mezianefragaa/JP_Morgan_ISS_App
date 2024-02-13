package com.example.jpmorganissapp.util

private const val TO_FEET = 1 / 0.3048
private const val TO_YARD = 1 / 0.9144
private const val TO_MILE = 1 / 1609.344
/**
 * Given a distance in meters, format this distance to return a value which depends on the
 * measurement system.
 * Defaults to using 2 digits for decimal precision (for km and mi).
 */
fun formatDistance(
    dist: Double,
    precision: Int = 2,
    system: MeasurementSystem = MeasurementSystem.METRIC
): String {

    return when (system) {
       MeasurementSystem.METRIC -> {
            if (dist <= 1000) {
                "%.0f m".format(dist)
            } else {
                "%.${precision}f km".format(dist / 1000.0)
            }
        }
        MeasurementSystem.IMPERIAL -> {
            val yd = dist * TO_YARD
            if (yd <= 1000) {
                "%.0f yd".format(yd)
            } else {
                val mi = dist * TO_MILE
                "%.${precision}f mi".format(mi)
            }
        }
    }
}


enum class MeasurementSystem {
    METRIC, IMPERIAL
}