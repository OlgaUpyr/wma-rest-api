package com.wma.jsons

import com.fasterxml.jackson.annotation.JsonFormat
import com.wma.domains.AgeCategory
import com.wma.domains.Sportsman
import org.hibernate.validator.constraints.NotEmpty

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class SportsmanJson {
    long id

    @NotEmpty
    String email

    @NotEmpty
    String firstName

    @NotEmpty
    String lastName

    @NotEmpty
    String firstNameEng

    @NotEmpty
    String lastNameEng

    @NotEmpty
    String gender

    String address
    String phone

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate dob

    AgeCategoryJson category
    Long age
    boolean isAttached

    SportsmanJson() {}

    SportsmanJson(Sportsman sportsman, boolean isAttached, LocalDate now) {
        this.id = sportsman.id
        this.email = sportsman.email
        this.firstName = sportsman.firstName
        this.lastName = sportsman.lastName
        this.firstNameEng = sportsman.firstNameEng
        this.lastNameEng = sportsman.lastNameEng
        this.gender = sportsman.gender
        this.address = sportsman.address
        this.phone = sportsman.phone
        this.dob = sportsman.dob
        this.category = new AgeCategoryJson(sportsman.category)
        this.age = ChronoUnit.YEARS.between(sportsman.dob, now)
        this.isAttached = isAttached
    }
}
