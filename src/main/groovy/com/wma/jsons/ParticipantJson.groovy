package com.wma.jsons

import com.wma.domains.Competition
import com.wma.domains.Participant
import com.wma.domains.Team

import java.time.LocalDate

class ParticipantJson {
    long id
    boolean personalScore
    SportsmanJson sportsman
    Team team
    Competition competition
    List<ResultJson> results
    ResultJson result

    ParticipantJson() {}

    ParticipantJson(Participant participant, List<ResultJson> results) {
        this.id = participant.id
        this.personalScore = participant.personalScore
        this.sportsman = new SportsmanJson(participant.sportsman, false,  LocalDate.now())
        this.team = participant.team
        this.competition = participant.competition

        if(results) {
            this.results = results
        }
    }

    ParticipantJson(Participant participant, ResultJson result) {
        this.id = participant.id
        this.personalScore = participant.personalScore
        this.sportsman = new SportsmanJson(participant.sportsman, false,  LocalDate.now())
        this.team = participant.team
        this.competition = participant.competition
        this.result = result
    }
}
