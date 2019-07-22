package com.wma.jsons

import com.fasterxml.jackson.annotation.JsonFormat
import com.wma.domains.Competition
import org.hibernate.validator.constraints.NotEmpty

import java.time.LocalDate

class CompetitionJson {
    long id
    @NotEmpty(message = 'Name is invalid')
    String name
    @NotEmpty(message = 'Venue is invalid')
    String venue
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate startDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate endDate
    String competitionType
    String competitionClass
    int participantsLimit

    CompetitionJson() {}

    CompetitionJson(Competition competition) {
        this.id = competition.id
        this.name = competition.name
        this.venue = competition.venue
        this.startDate = competition.startDate
        this.endDate = competition.endDate
        this.competitionType = competition.competitionType
        this.competitionClass = competition.competitionClass
        this.participantsLimit = competition.participantsLimit
    }
}
