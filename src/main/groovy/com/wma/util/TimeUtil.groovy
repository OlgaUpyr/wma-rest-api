package com.wma.util

import org.springframework.stereotype.Component

import java.time.LocalDate

interface TimeUtil {

    LocalDate now()
}

@Component
class TimeUtilImpl implements TimeUtil {

    @Override
    LocalDate now() {
        LocalDate.now()
    }
}

