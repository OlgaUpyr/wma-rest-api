package com.wma.repositories

import com.wma.domains.AgeCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository('ageCategoryRepository')
interface AgeCategoryRepository extends JpaRepository<AgeCategory, Integer> {

    AgeCategory findById(int id)

    @Query(nativeQuery = true,
            value = '''SELECT c.* FROM age_categories c 
                    WHERE (:age BETWEEN c.age_from AND c.age_to) AND c.gender = :gender''')
    AgeCategory findCategoryByAgeAndGender(@Param('age') int age, @Param('gender') Boolean gender)
}

