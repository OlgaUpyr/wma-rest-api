package com.wma.domains

import com.wma.jsons.TeamJson
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.Table


@javax.persistence.Entity
@Table(schema = 'public', name = 'teams')
@Canonical
class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'team_id')
    long id

    @Column(name = 'name')
    @NotEmpty(message = '*Please provide competition name')
    String name

    @Column(name = 'country')
    String country

    @Column(name = 'region')
    @NotEmpty(message = '*Please provide team region')
    String region

    Team() {}

    Team(TeamJson json) {
        setName(json.name)
        setCountry(json.country)
        setRegion(json.region)
    }
}