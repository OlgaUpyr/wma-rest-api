package com.wma.domains

import com.wma.jsons.WMAFactorJson
import groovy.transform.Canonical
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table


@javax.persistence.Entity
@Table(schema = 'public', name = 'wma_parameters')
@Canonical
class WMAParameters {
    @Id
    @Column(name = 'type_id')
    int id

    @Column(name = 'm35')
    BigDecimal m35

    @Column(name = 'm40')
    BigDecimal m40

    @Column(name = 'm45')
    BigDecimal m45

    @Column(name = 'm50')
    BigDecimal m50

    @Column(name = 'm55')
    BigDecimal m55

    @Column(name = 'm60')
    BigDecimal m60

    @Column(name = 'm65')
    BigDecimal m65

    @Column(name = 'm70')
    BigDecimal m70

    @Column(name = 'm75')
    BigDecimal m75

    @Column(name = 'm80')
    BigDecimal m80

    @Column(name = 'm85')
    BigDecimal m85

    @Column(name = 'm90')
    BigDecimal m90

    @Column(name = 'm95')
    BigDecimal m95

    @Column(name = 'm100')
    BigDecimal m100

    @Column(name = 'w35')
    BigDecimal w35

    @Column(name = 'w40')
    BigDecimal w40

    @Column(name = 'w45')
    BigDecimal w45

    @Column(name = 'w50')
    BigDecimal w50

    @Column(name = 'w55')
    BigDecimal w55

    @Column(name = 'w60')
    BigDecimal w60

    @Column(name = 'w65')
    BigDecimal w65

    @Column(name = 'w70')
    BigDecimal w70

    @Column(name = 'w75')
    BigDecimal w75

    @Column(name = 'w80')
    BigDecimal w80

    @Column(name = 'w85')
    BigDecimal w85

    @Column(name = 'w90')
    BigDecimal w90

    @Column(name = 'w95')
    BigDecimal w95

    @Column(name = 'w100')
    BigDecimal w100

    WMAParameters() {}

    WMAParameters(WMAFactorJson wma) {
        this.id = wma.type.id
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