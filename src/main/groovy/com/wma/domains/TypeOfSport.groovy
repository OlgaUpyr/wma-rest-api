package com.wma.domains

import com.wma.jsons.TypeOfSportJson

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = 'public', name = 'types_of_sport')
class TypeOfSport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'type_id')
    int id
    @Column(name = 'type')
    String typeName
    @Column(name = 'record_male')
    BigDecimal recordMale
    @Column(name = 'record_female')
    BigDecimal recordFemale
    @Column(name = 'is_jumping')
    Boolean isJumping
    @Column(name = 'is_running')
    Boolean isRunning
    @Column(name = 'is_throwing')
    Boolean isThrowing
    @Column(name = 'is_outdoor')
    Boolean isOutdoor
    @Column(name = 'is_indoor')
    Boolean isIndoor
    @Column(name = 'is_nonstadia')
    Boolean isNonstadia

    TypeOfSport() {}

    TypeOfSport(TypeOfSportJson json) {
        this.typeName = json.typeName
        this.recordMale = json.recordMale
        this.recordFemale = json.recordFemale
        this.isJumping = json.isJumping
        this.isRunning = json.isRunning
        this.isThrowing = json.isThrowing
        this.isOutdoor = json.isOutdoor
        this.isIndoor = json.isIndoor
        this.isNonstadia = json.isNonstadia
    }
}
