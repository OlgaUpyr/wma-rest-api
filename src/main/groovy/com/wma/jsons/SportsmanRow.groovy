package com.wma.jsons

import com.fasterxml.jackson.annotation.JsonFormat
import javax.persistence.Id
import java.time.LocalDate

@javax.persistence.Entity
class SportsmanRow {
    @Id
    long result_id
    long id
    String name
    String surname
    String category
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = 'MM.dd.yyyy')
    LocalDate dob
    BigDecimal result
    BigDecimal wma
    String sport
    boolean score
}
