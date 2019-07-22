package com.wma.jsons

import com.fasterxml.jackson.annotation.JsonFormat

import java.time.LocalDate

class CompetitionScoresByTeamsModel {
    String competitionName
    String competitionVenue
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate startDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate endDate
    List<SportsmanScoreModel> sportsmen

    CompetitionScoresByTeamsModel(String competitionName, String competitionVenue, LocalDate startDate, LocalDate endDate, List<SportsmanScoreModel> sportsmen) {
        this.competitionName = competitionName
        this.competitionVenue = competitionVenue
        this.startDate = startDate
        this.endDate = endDate
        this.sportsmen = sportsmen
    }
}
