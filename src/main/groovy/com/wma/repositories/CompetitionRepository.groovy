package com.wma.repositories

import com.wma.domains.Competition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

import java.time.LocalDate

@Repository('competitionRepository')
interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Competition findById(long id)

//    @Query(nativeQuery = true,
//            value = '''SELECT c.* FROM competitions c
//                    WHERE NOT (c.start_date > :to OR c.end_date < :from)''')
//    List<Competition> findAllCompetitionsByDateRange(@Param('from') LocalDate from, @Param('to') LocalDate to)

    @Query(nativeQuery = true,
            value = '''SELECT c.* FROM competitions c''')
    List<Competition> findAllCompetitionsByDateRange()
}

