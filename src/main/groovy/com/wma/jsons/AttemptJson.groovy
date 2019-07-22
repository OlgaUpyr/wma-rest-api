package com.wma.jsons

import com.wma.domains.Attempt

class AttemptJson {
    long id
    BigDecimal actualResult
    int resultHours
    int resultMinutes
    int resultSeconds
    int resultMiliseconds
    int attempt

    AttemptJson() {}

    AttemptJson(Attempt attempt, Boolean isRunning) {
        this.id = attempt.id
        if(!isRunning) {
            this.actualResult = attempt.actualResult
        } else {
            this.resultHours = (attempt.actualResult / 3600).toInteger()
            this.resultMinutes = ((attempt.actualResult - this.resultHours*3600) / 60).toInteger()
            this.resultSeconds = (attempt.actualResult - this.resultHours*3600 - this.resultMinutes*60).toInteger()
            this.resultMiliseconds = ((attempt.actualResult - attempt.actualResult.toInteger())*1000).toInteger()
        }

        if(attempt.attempt == null) {
            this.attempt = 0
        } else if (!attempt.attempt) {
            this.attempt = -1
        } else if (attempt.attempt) {
            this.attempt = 1
        }
    }
}
