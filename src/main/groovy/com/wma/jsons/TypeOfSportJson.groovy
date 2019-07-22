package com.wma.jsons

import com.wma.domains.TypeOfSport

class TypeOfSportJson {
    int id
    String typeName
    BigDecimal recordMale
    BigDecimal recordFemale
    Boolean isJumping
    Boolean isRunning
    Boolean isThrowing
    Boolean isOutdoor
    Boolean isIndoor
    Boolean isNonstadia

    TypeOfSportJson() {}

    TypeOfSportJson(TypeOfSport type) {
        this.id = type.id
        this.typeName = type.typeName
        this.isJumping = type.isJumping
        this.isRunning = type.isRunning
        this.isThrowing = type.isThrowing
        this.isOutdoor = type.isOutdoor
        this.isIndoor = type.isIndoor
        this.isNonstadia = type.isNonstadia

        if(type.recordMale) {
            this.recordMale = type.recordMale
        }

        if(type.recordFemale) {
            this.recordFemale = type.recordFemale
        }
    }
}
