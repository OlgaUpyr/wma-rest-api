package com.wma.domains

import groovy.transform.Canonical
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@javax.persistence.Entity
@Table(schema = 'public', name = 'attempts')
@Canonical
class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'attempt_id')
    long id

    @ManyToOne
    @JoinColumn(name = 'result_id')
    Result result

    @Column(name = 'actual_result')
    BigDecimal actualResult

    @Column(name = 'attempt')
    Boolean attempt
}
