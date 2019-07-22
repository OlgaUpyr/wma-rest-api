package com.wma.repositories

import com.wma.domains.Sportsman
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository('sportsmanRepository')
interface SportsmanRepository extends JpaRepository<Sportsman, Long> {
    Sportsman findById(long id)

    Sportsman findByEmailIgnoreCase(String email)

    List<Sportsman> findAll()

    @Query(nativeQuery = true,
            value = '''SELECT s.* FROM sportsmen s WHERE s.email=:email AND s.sportsman_id!=:sportsman''')
    Sportsman findAnotherSportsmenByEmail(@Param('sportsman') Long sportsman, @Param('email') String email)

    @Query(nativeQuery = true,
            value = '''SELECT s.* FROM sportsmen s 
                    JOIN participants p ON s.sportsman_id=p.sportsman_id
                    WHERE p.competition_id=:competition''')
    List<Sportsman> findSportsmenByCompetition(@Param('competition') Long competition)

    @Query(nativeQuery = true,
            value = '''SELECT s.* FROM sportsmen s 
                    WHERE s.sportsman_id NOT IN 
                    (SELECT p.sportsman_id FROM participants p WHERE p.competition_id=:competition)''')
    List<Sportsman> findSportsmenNotRegisteredOnCompetition(@Param('competition') Long competition)
}

