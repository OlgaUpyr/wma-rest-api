package com.wma.repositories

import com.wma.domains.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import scala.Int

@Repository('resultRepository')
interface ResultRepository extends JpaRepository<Result, Long> {
    @Query(nativeQuery = true,
            value = '''SELECT r.* FROM results r WHERE r.participant_id=:participant AND r.type_id=:sport''')
    Result findResultByParticipantAndSport(@Param('participant') Long participant, @Param('sport') int sport)

    @Query(nativeQuery = true, value = '''SELECT r.* FROM results r WHERE r.participant_id=:participant''')
    List<Result> findResultByParticipant(@Param('participant') Long participant)
}
