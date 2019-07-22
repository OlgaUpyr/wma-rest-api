package com.wma.domains

import com.fasterxml.jackson.annotation.JsonManagedReference
import groovy.transform.Canonical

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.OneToMany
import javax.persistence.Table

@javax.persistence.Entity
@Table(schema = 'public', name = 'results')
@Canonical
class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'result_id')
    long id

    @Column(name = 'type_id')
    int typeId

    @Column(name = 'participant_id')
    long participantId

    @Column(name = 'final_result')
    BigDecimal finalResult

    @Column(name = 'wma')
    BigDecimal wma

    @JsonManagedReference('attempts')
    @OneToMany(mappedBy = 'result', cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Attempt> attempts
}