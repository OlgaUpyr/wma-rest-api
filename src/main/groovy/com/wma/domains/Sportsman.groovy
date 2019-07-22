package com.wma.domains

import com.fasterxml.jackson.annotation.JsonFormat
import com.wma.jsons.SportsmanJson
import groovy.transform.Canonical
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.GenerationType
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import java.time.LocalDate


@javax.persistence.Entity
@Table(schema = 'public', name = 'sportsmen')
@Canonical
class Sportsman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'sportsman_id')
    long id

    @Column(name = 'first_name')
    @NotEmpty(message = '*Please provide first name')
    String firstName

    @Column(name = 'last_name')
    @NotEmpty(message = '*Please provide last name')
    String lastName

    @Column(name = 'first_name_eng')
    @NotEmpty(message = '*Please provide first name - english version')
    String firstNameEng

    @Column(name = 'last_name_eng')
    @NotEmpty(message = '*Please provide last name - english version')
    String lastNameEng

    @Column(name = 'dob', columnDefinition = 'date')
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'M/d/yyyy')
    LocalDate dob

    @Column(name = 'gender')
    @NotEmpty(message = '*Please provide gender')
    String gender

    @Column(name = 'phone')
    String phone

    @Column(name = 'address')
    String address

    @Column(name = 'email')
    @Email(message = '*Invalid Email Address')
    @NotEmpty(message = '*Please provide an email')
    String email

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = 'category_id')
    AgeCategory category

    Sportsman() {}

    Sportsman(SportsmanJson json) {
        setFirstName(json.firstName)
        setLastName(json.lastName)
        setFirstNameEng(json.firstNameEng)
        setLastNameEng(json.lastNameEng)
        setDob(json.dob)
        setGender(json.gender)
        setPhone(json.phone)
        setAddress(json.address)
        setEmail(json.email)
    }
}