package com.wma.controllers

import java.time.LocalDate

import com.wma.domains.TypeOfSport
import com.wma.exceptions.NotFoundException
import com.wma.jsons.{AttemptJson, CompetitionJson, CompetitionsListJson, ParticipantJson, ParticipantsListJson, ResultJson, SportsmanJson, SportsmenListJson, TeamJson, TeamsListJson, TypeOfSportJson, TypesOfSportListJson}
import com.wma.repositories.{CompetitionRepository, ParticipantRepository, ResultRepository, SportsmanRepository, TypeOfSportRepository}
import com.wma.util.TimeUtil
import com.wma.utils.CompetitionUtil
import javax.persistence.EntityManager
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestParam, RestController}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.transaction.Transactional
import javax.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import play.api.libs.json.{JsValue, Json}

import scala.collection.JavaConversions._


@RestController
class CompetitionsController(sportsmanRepository: SportsmanRepository,
                             typeOfSportRepository: TypeOfSportRepository,
                             participantRepository: ParticipantRepository,
                             competitionRepository: CompetitionRepository,
                             resultRepository: ResultRepository,
                             competitionUtil: CompetitionUtil,
                             timeUtil: TimeUtil,
                             entityManager: EntityManager) {

  @PostMapping(Array("/api/admin/competition"))
  def createCompetition(implicit request: HttpServletRequest, response: HttpServletResponse,
                        @Valid @RequestBody newCompetition: CompetitionJson): CompetitionJson = {
    val competition = competitionUtil.createCompetition(newCompetition)
    new CompetitionJson(competition)
  }

  @GetMapping(Array("/api/admin/competition/info"))
  def getCompetition(implicit request: HttpServletRequest, response: HttpServletResponse,
                     @RequestParam(name = "competition", required = true) competitionId: Long): CompetitionJson = {
    val competition = competitionRepository.findById(competitionId)
    new CompetitionJson(competition)
  }

  @GetMapping(Array("/api/admin/competitions"))
  def getCompetitions(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @DateTimeFormat(pattern = "MM-dd-yyyy") @RequestParam(name = "from", required = false) startDate: LocalDate,
                      @DateTimeFormat(pattern = "MM-dd-yyyy") @RequestParam(name = "to", required = false) endDate: LocalDate
                     ): CompetitionsListJson = {
    val competitions = competitionUtil.getCompetitionByDateRange(startDate, endDate)
    new CompetitionsListJson(competitions.map(competition => new CompetitionJson(competition)))
  }

  @PostMapping(Array("/api/admin/team"))
  def createTeam(implicit request: HttpServletRequest, response: HttpServletResponse,
                 @Valid @RequestBody newTeam: TeamJson): TeamJson = {
    val team = competitionUtil.createTeam(newTeam)
    new TeamJson(team, false)
  }

  @GetMapping(Array("/api/admin/teams"))
  def getTeams(implicit request: HttpServletRequest, response: HttpServletResponse): TeamsListJson = {
    val teams = competitionUtil.getAllTeams()
    new TeamsListJson(teams.map(team => new TeamJson(team, false)))
  }

  @PostMapping(Array("/api/admin/sportsman"))
  def createSportsman(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @Valid @RequestBody newSportsman: SportsmanJson): SportsmanJson = {
    val sportsman = competitionUtil.createSportsman(newSportsman)
    new SportsmanJson(sportsman, false, timeUtil.now())
  }

  @PostMapping(Array("/api/admin/edit/sportsman"))
  def updateSportsman(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @Valid @RequestBody sportsman: SportsmanJson): SportsmanJson = {
    sportsmanRepository.findById(sportsman.getId) match {
      case null => throw NotFoundException("Sportsman was not found")
      case _ =>
        val editedSportsman = competitionUtil.updateSportsman(sportsman)
        new SportsmanJson(editedSportsman, false, timeUtil.now())
    }
  }

  @Transactional
  @PostMapping(Array("/api/admin/register/sportsman"))
  def registerSportsman(implicit request: HttpServletRequest, response: HttpServletResponse,
                        @RequestParam(name = "sportsman", required = true) sportsmanId: Long,
                        @RequestParam(name = "competition", required = true) competitionId: Long,
                        @RequestParam(name = "team", required = true) teamId: Long): JsValue = {
    Option(sportsmanRepository.findOne(sportsmanId)) match {
      case Some(sportsman) =>
        val status = competitionUtil.attachSportsmanToCompetition(competitionId, teamId, sportsmanId)
        Json.obj("status" -> status.toString)
      case _ => throw NotFoundException("Sportsman was not found")
    }
  }

  @PostMapping(Array("/api/admin/participant/edit"))
  def updateParticipant(implicit request: HttpServletRequest, response: HttpServletResponse,
                        @RequestParam(name = "participant", required = true) participantId: Long,
                        @RequestParam(name = "team", required = true) teamId: Long,
                        @RequestParam(name = "personal_score", required = false) personalScore: Boolean,
                        @RequestParam(name = "types", required = false, defaultValue = "") types: Array[String]
                       ): JsValue = {
    Option(participantRepository.findOne(participantId)) match {
      case Some(participant) =>
        val status = competitionUtil.updateParticipantInfo(participant, teamId, personalScore, types)
        Json.obj("status" -> status.toString)
      case _ => throw NotFoundException("Participant was not found")
    }
  }

  // TODO
  @PostMapping(Array("/api/admin/detach/sportsman"))
  def detachSportsman(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @RequestParam(name = "competition", required = true) competitionId: Long,
                      @RequestParam(name = "team", required = true) teamId: Long,
                      @RequestParam(name = "sportsman", required = true) sportsmanId: Long): JsValue = {
    val status = competitionUtil.detachSportsmanFromTeam(competitionId, teamId, sportsmanId)
    Json.obj("status" -> status.toString)
  }

  @GetMapping(Array("/api/admin/sportsmen"))
  def getSportsmen(implicit request: HttpServletRequest, response: HttpServletResponse,
                   @RequestParam(name = "competition", required = false) competitionId: java.lang.Long): SportsmenListJson = {
    val sportsmen = competitionId match {
      case null =>
        competitionUtil.getAllSportsmen()
      case _ =>
        competitionUtil.getAllSportsmenNotRegisteredOnCompetition(competitionId)
    }

    new SportsmenListJson(sportsmen.map(sportsman => new SportsmanJson(sportsman, false, timeUtil.now())))
  }

  @GetMapping(Array("/api/admin/participants"))
  def getParticipants(implicit request: HttpServletRequest, response: HttpServletResponse,
                   @RequestParam(name = "competition", required = true) competitionId: java.lang.Long): ParticipantsListJson = {
    val participants = participantRepository.findAllParticipantsByCompetition(competitionId)
    new ParticipantsListJson(participants
      .map(p => new ParticipantJson(p, resultRepository.findResultByParticipant(p.getId)
        .map(result => {
          val sport = typeOfSportRepository.findById(result.getTypeId)
          new ResultJson(result, result.getAttempts.map(a => new AttemptJson(a, sport.getIsRunning)), sport)
        })
      ))
    )
  }

//  @GetMapping(Array("/api/admin/age-categories"))
//  def getAllAgeCategories(implicit request: HttpServletRequest, response: HttpServletResponse): AgeCategoriesListJson = {
//    var categories = competitionUtil.getAllAgeCategories()
//    new AgeCategoriesListJson(categories.map(category => new AgeCategoryJson(category)))
//  }

  @GetMapping(Array("/api/admin/types-of-sport"))
  def getAllTypesOfSport(implicit request: HttpServletRequest, response: HttpServletResponse,
                         @RequestParam(name = "competition", required = false) competitionId: java.lang.Long
                        ): TypesOfSportListJson = {
    val types = competitionId match {
      case null => competitionUtil.getAllTypesOfSport()
      case _ => competitionUtil.getTypesOfSportForCompetition(competitionId)
    }

    new TypesOfSportListJson(types.map(sportType => new TypeOfSportJson(sportType)))
  }

  @PostMapping(Array("/api/admin/types-of-sport"))
  def saveNewTypeOfSport(implicit request: HttpServletRequest, response: HttpServletResponse,
                         @Valid @RequestBody json: TypeOfSportJson): TypesOfSportListJson = {
    val newSport = new TypeOfSport(json)
    typeOfSportRepository.save(newSport)

    new TypesOfSportListJson(competitionUtil.getAllTypesOfSport().map(sportType => new TypeOfSportJson(sportType)))
  }

  @PostMapping(Array("/api/admin/types-of-sport/edit"))
  def editNewTypeOfSport(implicit request: HttpServletRequest, response: HttpServletResponse,
                         @Valid @RequestBody json: TypeOfSportJson): TypesOfSportListJson = {
    typeOfSportRepository.findById(json.getId) match {
      case null => throw NotFoundException("Sport type was not found")
      case _ =>
        val newSport = new TypeOfSport(json)
        newSport.setId(json.getId)
        typeOfSportRepository.save(newSport)
    }

    new TypesOfSportListJson(competitionUtil.getAllTypesOfSport().map(sportType => new TypeOfSportJson(sportType)))
  }
}
