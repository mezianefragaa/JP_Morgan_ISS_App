package com.example.jpmorganissapp

import com.example.jpmorganissapp.util.formatDistance
import org.junit.Test

class FormatterTest {

    @Test
    fun formatDistanceTest(){
       val formatted =  formatDistance(1000.0)
        assert(formatted == "1 km")

       // assert(2+2 ==4)
    }
}