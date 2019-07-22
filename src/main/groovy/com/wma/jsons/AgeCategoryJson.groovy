package com.wma.jsons

import com.wma.domains.AgeCategory

class AgeCategoryJson {
    int id
    String category
    boolean gender
    String ageFrom
    String ageTo

    AgeCategoryJson() {}

    AgeCategoryJson(AgeCategory ageCategory) {
        this.id = ageCategory.id
        this.category = ageCategory.categoryName
        this.gender = ageCategory.gender
        this.ageFrom = ageCategory.ageFrom
        this.ageTo = ageCategory.ageTo
    }
}
