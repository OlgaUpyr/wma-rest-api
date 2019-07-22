package com.wma.jsons

import com.wma.domains.Result
import com.wma.domains.TypeOfSport

class ResultJson {
    List<AttemptJson> attempts
    TypeOfSport sport
    BigDecimal finalResult
    BigDecimal wma

    ResultJson() {}

    ResultJson(Result result, List<AttemptJson> attempts, TypeOfSport sport) {
        this.sport = sport
        this.finalResult = result.finalResult
        this.wma = result.wma

        if(attempts != null) {
            this.attempts = attempts
        }
    }
}
