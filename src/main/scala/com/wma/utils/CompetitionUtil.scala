package com.wma.utils

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util

import com.wma.domains.{AgeCategory, Competition, Participant, Result, Sportsman, Team, TypeOfSport}
import com.wma.exceptions.{AlreadyExistsException, BadRequestException, NotFoundException}
import com.wma.jsons.{CompetitionJson, SportsmanJson, TeamJson}
import com.wma.repositories.{AgeCategoryRepository, CompetitionRepository, ParticipantRepository, ResultRepository, SportsmanRepository, TeamRepository, TypeOfSportRepository}
import com.wma.util.TimeUtil
import javax.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConversions._

trait CompetitionUtil {
  def createCompetition(json: CompetitionJson): Competition
  def createTeam(json: TeamJson): Team
  def getAllTeams(): Seq[Team]
  def createSportsman(json: SportsmanJson): Sportsman
  def updateSportsman(json: SportsmanJson): Sportsman
  def getAllSportsmen(): Seq[Sportsman]
  def getAllSportsmenNotRegisteredOnCompetition(competitionId: Long): Seq[Sportsman]
  def getAllSportsmenRegisteredOnCompetition(competitionId: Long): Seq[Sportsman]
  def getAllTypesOfSport(): Seq[TypeOfSport]
  def getTypesOfSportForCompetition(competitionId: Long): Seq[TypeOfSport]
  def attachSportsmanToCompetition(competitionId: Long, teamId: Long, sportsmanId: Long): Boolean
  def updateParticipantInfo(participant: Participant, teamId: Long, personalScore: Boolean, types: Array[String]): Boolean
  // TODO
  def getCompetitionByDateRange(startDate: LocalDate, endDate: LocalDate): Seq[Competition]
  def detachSportsmanFromTeam(competitionId: Long, teamId: Long, sportsmanId: Long): Boolean
  def getAllAgeCategories(): Seq[AgeCategory]
}

@Component
@Transactional
class CompetitionUtilImpl(competitionRepository: CompetitionRepository,
                          teamRepository: TeamRepository,
                          sportsmanRepository: SportsmanRepository,
                          ageCategoryRepository: AgeCategoryRepository,
                          participantRepository: ParticipantRepository,
                          typeOfSportRepository: TypeOfSportRepository,
                          resultRepository: ResultRepository,
                          timeUtil: TimeUtil, entityManager: EntityManager) extends CompetitionUtil {

  override def createCompetition(json: CompetitionJson): Competition = {
    val competition = new Competition(json)
    competitionRepository.save(competition)
    competition
  }

  override def getCompetitionByDateRange(startDate: LocalDate, endDate: LocalDate): Seq[Competition] = {
    competitionRepository.findAllCompetitionsByDateRange() // TODO
  }

  override def createTeam(json: TeamJson): Team = {
    val team = new Team(json)
    teamRepository.save(team)
    team
  }

  override def getAllTeams(): Seq[Team] = {
    teamRepository.findAll()
  }

  override def createSportsman(json: SportsmanJson): Sportsman = {
    if (!Seq("чоловіча", "жіноча").contains(json.getGender.toLowerCase)) {
      throw BadRequestException("Value for field 'gender' is invalid")
    }

    if(sportsmanRepository.findByEmailIgnoreCase(json.getEmail) != null) {
      throw AlreadyExistsException()
    }

    val sportsman = new Sportsman(json)
    val age = ChronoUnit.YEARS.between(sportsman.getDob, timeUtil.now()).toInt
    val gender = sportsman.getGender.toLowerCase() == "чоловіча"

    val ageCategory = ageCategoryRepository.findCategoryByAgeAndGender(age, gender)
    if (ageCategory == null) {
      throw NotFoundException()
    }
    sportsman.setCategory(ageCategory)
    sportsmanRepository.save(sportsman)
    sportsman
  }

  override def updateSportsman(json: SportsmanJson): Sportsman = {
    if (!Seq("чоловіча", "жіноча").contains(json.getGender.toLowerCase)) {
      throw BadRequestException("Value for field 'gender' is invalid")
    }

    if(sportsmanRepository.findAnotherSportsmenByEmail(json.getId, json.getEmail) != null) {
      throw AlreadyExistsException()
    }

    val sportsman = sportsmanRepository.findById(json.getId)
    sportsman.setFirstName(json.getFirstName)
    sportsman.setFirstNameEng(json.getFirstNameEng)
    sportsman.setLastName(json.getLastName)
    sportsman.setLastNameEng(json.getLastNameEng)
    sportsman.setEmail(json.getEmail)
    sportsman.setAddress(json.getAddress)
    sportsman.setPhone(json.getPhone)
    sportsmanRepository.save(sportsman)
    sportsman
  }

  override def updateParticipantInfo(participant: Participant, teamId: Long, personalScore: Boolean,
                                     types: Array[String]): Boolean = {
    val team = teamRepository.findById(teamId)
    if (team == null) {
      throw NotFoundException()
    }

    entityManager.createNativeQuery("DELETE FROM results WHERE participant_id=:participant")
      .setParameter("participant", participant.getId)
      .executeUpdate()

    val newTags = new util.ArrayList[TypeOfSport]()
    types.map(id => newTags.add(typeOfSportRepository.findById(java.lang.Integer.valueOf(id))))

    types.foreach(id => {
      val res = new Result()
      res.setParticipantId(participant.getId)
      res.setTypeId(java.lang.Integer.valueOf(id))
      resultRepository.save(res)
    })

    participant.setTeam(team)
    participant.setPersonalScore(personalScore)
    participantRepository.save(participant)

    true
  }

  override def attachSportsmanToCompetition(competitionId: Long, teamId: Long, sportsmanId: Long): Boolean = {
    val competition = competitionRepository.findById(competitionId)
    if (competition == null) {
      throw NotFoundException()
    }

    val team = teamRepository.findById(teamId)
    if (team == null) {
      throw NotFoundException()
    }

    val participant = participantRepository.findParticipantByCompetitionAndTeamAndSportsman(competitionId, teamId, sportsmanId)
    if (participant != null) {
      throw AlreadyExistsException()
    }

    val newParticipant = new Participant()
    newParticipant.setCompetition(competition)
    newParticipant.setTeam(team)
    newParticipant.setSportsman(sportsmanRepository.findById(sportsmanId))
    newParticipant.setPersonalScore(false)
    participantRepository.save(newParticipant)

    true
  }

  override def detachSportsmanFromTeam(competitionId: Long, teamId: Long, sportsmanId: Long): Boolean = {
    val participant = participantRepository.findParticipantByCompetitionAndTeamAndSportsman(competitionId, teamId, sportsmanId)
    participantRepository.delete(participant)
    true
  }

  override def getAllSportsmen(): Seq[Sportsman] = {
    sportsmanRepository.findAll()
  }

  override def getAllSportsmenNotRegisteredOnCompetition(competitionId: Long): Seq[Sportsman] = {
    sportsmanRepository.findSportsmenNotRegisteredOnCompetition(competitionId)
  }

  override def getAllSportsmenRegisteredOnCompetition(competitionId: Long): Seq[Sportsman] = {
    sportsmanRepository.findSportsmenByCompetition(competitionId)
  }

  override def getAllAgeCategories(): Seq[AgeCategory] = {
    ageCategoryRepository.findAll()
  }

  override def getAllTypesOfSport(): Seq[TypeOfSport] = {
    typeOfSportRepository.findAll()
  }

  override def getTypesOfSportForCompetition(competitionId: Long): Seq[TypeOfSport] = {
    val competition = competitionRepository.findById(competitionId)
    if (competition == null) {
      throw NotFoundException()
    }

    competition.getCompetitionType match {
      case "outdoor" => typeOfSportRepository.findOutdoorSportTypes()
      case "indoor" => typeOfSportRepository.findIndoorSportTypes()
      case "nonstadia" => typeOfSportRepository.findNonstadiaSportTypes()
    }
  }
}
