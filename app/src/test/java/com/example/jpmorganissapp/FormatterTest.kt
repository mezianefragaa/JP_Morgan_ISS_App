package com.example.jpmorganissapp

import com.example.jpmorganissapp.util.MeasurementSystem
import com.example.jpmorganissapp.util.formatDistance
import org.junit.Test

class FormatterTest {

    @Test
    fun formatDistanceTest(){
       val formattedLessThan1000Metric =  formatDistance(dist = 1000.0, precision = 2, MeasurementSystem.METRIC)
       val expectedLessThan1000Metric = "1000 m"
        assert(formattedLessThan1000Metric == expectedLessThan1000Metric)

        val formattedMoreThan1000Metric =  formatDistance(2000.0, precision = 2, MeasurementSystem.METRIC)
        val expectedMoreThan1000Metric = "2.00 km"
        assert(formattedMoreThan1000Metric == expectedMoreThan1000Metric)

        val formattedLessThan1000Imperial =  formatDistance(dist = 1000.0, precision = 2, MeasurementSystem.IMPERIAL)
        val expectedLessThan1000Imperial = "0.62 mi"
        assert(formattedLessThan1000Imperial == expectedLessThan1000Imperial)

        val formattedMoreThan1000Imperial =  formatDistance(2000.0, precision = 2, MeasurementSystem.IMPERIAL)
        val expectedMoreThan1000Imperial = "1.24 mi"
        assert(formattedMoreThan1000Imperial == expectedMoreThan1000Imperial)

    }
}