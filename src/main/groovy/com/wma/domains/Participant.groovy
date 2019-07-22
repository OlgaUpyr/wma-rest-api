package com.wma.domains

import com.fasterxml.jackson.annotation.JsonManagedReference
import groovy.transform.Canonical
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.CascadeType
import javax.persistence.GenerationType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table


@javax.persistence.Entity
@Table(schema = 'public', name = 'participants')
@Canonical
class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'participant_id')
    long id

    @ManyToOne()
    @JoinColumn(name = 'competition_id')
    Competition competition

    @ManyToOne()
    @JoinColumn(name = 'team_id')
    Team team

    @ManyToOne()
    @JoinColumn(name = 'sportsman_id')
    Sportsman sportsman

    @Column(name = "personal_score")
    boolean personalScore
}