package com.wma.domains

import com.wma.jsons.AgeCategoryJson

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(schema = 'public', name = 'age_categories')
class AgeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = 'category_id')
    int id
    @Column(name = 'category')
    String categoryName
    @Column(name = 'gender')
    boolean gender
    @Column(name = 'age_from')
    String ageFrom
    @Column(name = 'age_to')
    String ageTo

    AgeCategory() {}

    AgeCategory(AgeCategoryJson json) {
        this.id = json.id
        this.categoryName = json.category
        this.gender = json.gender
        this.ageFrom = json.ageFrom
        this.ageTo = json.ageTo
    }
}
