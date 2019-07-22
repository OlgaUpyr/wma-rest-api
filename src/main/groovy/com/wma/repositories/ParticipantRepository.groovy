package com.wma.repositories

import com.wma.domains.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository('participantRepository')
interface ParticipantRepository extends JpaRepository<Participant, Long> {
    @Query(nativeQuery = true,
            value = '''SELECT p.* FROM participants p 
                    WHERE p.competition_id=:competition''')
    List<Participant> findAllParticipantsByCompetition(
            @Param('competition') Long competition)

    @Query(nativeQuery = true,
            value = '''SELECT p.* FROM participants p 
                    WHERE p.competition_id=:competition AND p.team_id=:team AND p.sportsman_id=:sportsman''')
    Participant findParticipantByCompetitionAndTeamAndSportsman(
            @Param('competition') Long competition, @Param('team') Long team, @Param('sportsman') Long sportsman)

    @Query(nativeQuery = true,
            value = '''SELECT p.* FROM participants p 
                    WHERE p.competition_id=:competition AND p.sportsman_id=:sportsman''')
    Participant findParticipantByCompetitionAndSportsman(@Param('competition') Long competition, @Param('sportsman') Long sportsman)

    @Query(nativeQuery = true,
            value = '''SELECT p.team_id FROM participants p 
                    WHERE p.competition_id=:competition''')
    List<BigInteger> findTeamsAttachedToCompetition(@Param('competition') Long competition)

    @Query(nativeQuery = true,
            value = '''SELECT p.sportsman_id FROM participants p 
                    WHERE p.competition_id=:competition AND p.team_id=:team''')
    List<BigInteger> findSportsmenAttachedToCompetitionAndTeam(
            @Param('competition') Long competition, @Param('team') Long team)

    @Query(nativeQuery = true,
            value = '''SELECT p.* FROM participants p 
                    JOIN results r ON p.participant_id = r.participant_id
                    WHERE p.competition_id=:competition AND r.type_id=:sport''')
    List<Participant> findParticipantByCompetitionAndSport(@Param('competition') Long competition, @Param('sport') int sport)
}

