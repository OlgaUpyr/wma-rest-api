package com.wma.domains

import com.fasterxml.jackson.annotation.JsonFormat
import groovy.transform.Canonical
import org.hibernate.validator.constraints.NotEmpty
import com.wma.jsons.CompetitionJson
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.Table
import java.time.LocalDate


@javax.persistence.Entity
@Table(schema = 'public', name = 'competitions')
@Canonical
class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'competition_id')
    long id

    @Column(name = 'name')
    @NotEmpty(message = '*Please provide competition name')
    String name

    @Column(name = 'venue')
    @NotEmpty(message = '*Please provide competition venue')
    String venue

    @Column(name = 'start_date', columnDefinition = 'date')
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate startDate

    @Column(name = 'end_date', columnDefinition = 'date')
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate endDate

    @Column(name = 'competition_type')
    String competitionType

    @Column(name = 'competition_class')
    String competitionClass

    @Column(name = 'participants_limit')
    int participantsLimit

    Competition() {}

    Competition(CompetitionJson json) {
        this.name = json.name
        this.venue = json.venue
        this.startDate = json.startDate
        this.endDate = json.endDate
        this.competitionType = json.competitionType
        this.competitionClass = json.competitionClass
        this.participantsLimit = json.participantsLimit
    }
}