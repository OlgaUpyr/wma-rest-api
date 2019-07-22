package com.wma.jsons

import com.wma.domains.TypeOfSport
import com.wma.domains.WMAParameters

class WMAFactorJson {
    TypeOfSport type
    BigDecimal m35
    BigDecimal m40
    BigDecimal m45
    BigDecimal m50
    BigDecimal m55
    BigDecimal m60
    BigDecimal m65
    BigDecimal m70
    BigDecimal m75
    BigDecimal m80
    BigDecimal m85
    BigDecimal m90
    BigDecimal m95
    BigDecimal m100
    BigDecimal w35
    BigDecimal w40
    BigDecimal w45
    BigDecimal w50
    BigDecimal w55
    BigDecimal w60
    BigDecimal w65
    BigDecimal w70
    BigDecimal w75
    BigDecimal w80
    BigDecimal w85
    BigDecimal w90
    BigDecimal w95
    BigDecimal w100

    WMAFactorJson() {}

    WMAFactorJson(WMAParameters wma, TypeOfSport type) {
        this.type = type
        this.m35 = wma.m35
        this.m40 = wma.m40
        this.m45 = wma.m45
        this.m50 = wma.m50
        this.m55 = wma.m55
        this.m60 = wma.m60
        this.m65 = wma.m65
        this.m70 = wma.m70
        this.m75 = wma.m75
        this.m80 = wma.m80
        this.m85 = wma.m85
        this.m90 = wma.m90
        this.m95 = wma.m95
        this.m100 = wma.m100
        this.w35 = wma.w35
        this.w40 = wma.w40
        this.w45 = wma.w45
        this.w50 = wma.w50
        this.w55 = wma.w55
        this.w60 = wma.w60
        this.w65 = wma.w65
        this.w70 = wma.w70
        this.w75 = wma.w75
        this.w80 = wma.w80
        this.w85 = wma.w85
        this.w90 = wma.w90
        this.w95 = wma.w95
        this.w100 = wma.w100
    }
}
