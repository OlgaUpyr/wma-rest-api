package com.wma.controllers

import java.io.ByteArrayInputStream

import com.wma.domains.{Competition, Team, WMAParameters}
import com.wma.exceptions.NotFoundException
import com.wma.jsons.{AttemptJson, CompetitionScoresByTeamsModel, ParticipantJson, ParticipantsListJson, ResultJson, SportsmanJson, SportsmanRow, SportsmanScoreModel, WMAFactorJson, WMAFactorsListJson}
import com.wma.repositories.{AttemptRepository, CompetitionRepository, ParticipantRepository, SportsmanRepository, TypeOfSportRepository, WMAFactorsRepository}
import com.wma.util.TimeUtil
import com.wma.utils.{CompetitionUtil, RatingUtil}
import javax.persistence.EntityManager
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestBody, RequestParam, RestController}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.validation.Valid
import play.api.libs.json.{JsValue, Json}
import org.apache.commons.io.IOUtils

import scala.collection.JavaConversions._


@RestController
class RatingsController(sportsmanRepository: SportsmanRepository,
                        typeOfSportRepository: TypeOfSportRepository,
                        participantRepository: ParticipantRepository,
                        competitionRepository: CompetitionRepository,
                        attemptRepository: AttemptRepository,
                        WMAFactorsRepository: WMAFactorsRepository,
                        competitionUtil: CompetitionUtil,
                        ratingUtil: RatingUtil,
                        timeUtil: TimeUtil,
                        entityManager: EntityManager) {

  @GetMapping(Array("/api/admin/results"))
  def getResultsBySport(implicit request: HttpServletRequest, response: HttpServletResponse,
                        @RequestParam(name = "competition", required = true) competitionId: Long,
                        @RequestParam(name = "sport", required = true) sportId: Int
                       ): ParticipantsListJson = {
    ratingUtil.getParticipantByCompetitionAndSport(competitionId, sportId) match {
      case null => throw NotFoundException()
      case participants => new ParticipantsListJson(
        participants.map(p => {
          val result = ratingUtil.getResultBySport(p.getId, sportId)
          val sport = typeOfSportRepository.findById(result.getTypeId)
          new ParticipantJson(p, new ResultJson(result, result.getAttempts
            .map(r => new AttemptJson(r, sport.getIsRunning)), sport)
          )
        })
      )
    }
  }

  @GetMapping(Array("/api/admin/wma"))
  def getWMAFactors(implicit request: HttpServletRequest, response: HttpServletResponse): WMAFactorsListJson = {
    val factors = WMAFactorsRepository.findAll().map(p => new WMAFactorJson(p, typeOfSportRepository.findById(p.getId)))
    new WMAFactorsListJson(factors)
  }

  @PostMapping(Array("/api/admin/wma"))
  def writeWMAFactors(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @Valid @RequestBody factors: WMAFactorJson): JsValue = {
    val wmaFactors = WMAFactorsRepository.findById(factors.getType.getId)
    wmaFactors.setM35(factors.getM35)
    wmaFactors.setM40(factors.getM40)
    wmaFactors.setM45(factors.getM45)
    wmaFactors.setM50(factors.getM50)
    wmaFactors.setM55(factors.getM55)
    wmaFactors.setM60(factors.getM60)
    wmaFactors.setM65(factors.getM65)
    wmaFactors.setM70(factors.getM70)
    wmaFactors.setM75(factors.getM75)
    wmaFactors.setM80(factors.getM80)
    wmaFactors.setM85(factors.getM85)
    wmaFactors.setM90(factors.getM90)
    wmaFactors.setM95(factors.getM95)
    wmaFactors.setM100(factors.getM100)
    wmaFactors.setW35(factors.getW35)
    wmaFactors.setW40(factors.getW40)
    wmaFactors.setW45(factors.getM45)
    wmaFactors.setW50(factors.getW50)
    wmaFactors.setW55(factors.getW55)
    wmaFactors.setW60(factors.getW60)
    wmaFactors.setW65(factors.getW65)
    wmaFactors.setW70(factors.getW70)
    wmaFactors.setW75(factors.getW75)
    wmaFactors.setW80(factors.getW80)
    wmaFactors.setW85(factors.getW85)
    wmaFactors.setW90(factors.getW90)
    wmaFactors.setW95(factors.getW95)
    wmaFactors.setW100(factors.getW100)
    WMAFactorsRepository.save(wmaFactors)

    Json.obj("status" -> true.toString)
  }

  @PostMapping(Array("/api/admin/result/time"))
  def writeTimeResult(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @RequestParam(name = "participant", required = true) participantId: Long,
                      @RequestParam(name = "sport", required = true) sportId: Int,
                      @RequestParam(name = "hh", required = true) hours: Int,
                      @RequestParam(name = "mm", required = true) minutes: Int,
                      @RequestParam(name = "ss", required = true) seconds: Int,
                      @RequestParam(name = "ms", required = true) miliseconds: Int
                     ): JsValue = {
    Option(participantRepository.findOne(participantId)) match {
      case Some(participant) =>
        val status = ratingUtil.writeTimeResult(participant, sportId, hours, minutes, seconds, miliseconds)
        Json.obj("status" -> status.toString)
      case _ => throw NotFoundException("Participant was not found")
    }
  }

  @PostMapping(Array("/api/admin/result/time/update"))
  def updateTimeResult(implicit request: HttpServletRequest, response: HttpServletResponse,
                       @RequestParam(name = "participant", required = true) participantId: Long,
                       @RequestParam(name = "sport", required = true) sportId: Int,
                       @RequestParam(name = "attempt", required = true) attemptId: Long,
                       @RequestParam(name = "hh", required = true) hours: Int,
                       @RequestParam(name = "mm", required = true) minutes: Int,
                       @RequestParam(name = "ss", required = true) seconds: Int,
                       @RequestParam(name = "ms", required = true) miliseconds: Int
                      ): JsValue = {
    Option(participantRepository.findOne(participantId)) match {
      case Some(participant) =>
        val status = ratingUtil.updateTimeResult(participant, sportId, attemptId, hours, minutes, seconds, miliseconds)
        Json.obj("status" -> status.toString)
      case _ => throw NotFoundException("Participant was not found")
    }
  }

  @PostMapping(Array("/api/admin/result/distance"))
  def writeDistanceResult(implicit request: HttpServletRequest, response: HttpServletResponse,
                      @RequestParam(name = "participant", required = true) participantId: Long,
                      @RequestParam(name = "sport", required = true) sportId: Int,
                      @RequestParam(name = "result", required = true) result: java.math.BigDecimal,
                      @RequestParam(name = "attempt", required = true) attempt: Int
                     ): JsValue = {
    Option(participantRepository.findOne(participantId)) match {
      case Some(participant) =>
        val status = ratingUtil.writeDistanceResult(participant, sportId, result, attempt)
        Json.obj("status" -> status.toString)
      case _ => throw NotFoundException("Participant was not found")
    }
  }

  @PostMapping(Array("/api/admin/result/distance/update"))
  def updateDistanceResult(implicit request: HttpServletRequest, response: HttpServletResponse,
                           @RequestParam(name = "participant", required = true) participantId: Long,
                           @RequestParam(name = "sport", required = true) sportId: Int,
                           @RequestParam(name = "attemptId", required = true) attemptId: Long,
                           @RequestParam(name = "result", required = true) result: java.math.BigDecimal,
                           @RequestParam(name = "attempt", required = true) attempt: Int
                         ): JsValue = {
    Option(participantRepository.findOne(participantId)) match {
      case Some(participant) =>
        val status = ratingUtil.updateDistanceResult(participant, sportId, attemptId, result, attempt)
        Json.obj("status" -> status.toString)
      case _ => throw NotFoundException("Participant was not found")
    }
  }

  @GetMapping(Array("/api/admin/report/by-team"))
  def getTeamsScoreReport(implicit request: HttpServletRequest, response: HttpServletResponse,
                          @RequestParam(name = "competition", required = true) competitionId: Long
                         ): Unit = {
    competitionRepository.findById(competitionId) match {
      case null => throw NotFoundException("No competition")
      case competition => generateReport(response, competition)
    }
  }

  def generateReport(response: HttpServletResponse, competition: Competition): Unit = {
    val csvFileName = s"TEAMS_SCORE_REPORT-${timeUtil.now}.csv"
    response.addHeader("Content-Type", "application/octet-stream; charset=utf-8")
    response.addHeader("Content-Disposition", "attachment; filename=\""+csvFileName+"\"")
    IOUtils.copy(new ByteArrayInputStream(ratingUtil.getCompetitionReportByTeam(createTeamsScoreReport(competition))), response.getOutputStream)
    response.flushBuffer()
  }

  def createTeamsScoreReport(competition: Competition): CompetitionScoresByTeamsModel = {
    val query =
      s"""
         |SELECT DISTINCT (t.*) FROM teams t JOIN participants p ON p.team_id = t.team_id
         |WHERE p.competition_id=${competition.getId}
       """.stripMargin
    val teams = entityManager.createNativeQuery(query, classOf[Team]).getResultList.map(_.asInstanceOf[Team])

    teams match {
      case null => throw NotFoundException("No teams by competition")
      case list =>
        val sportsmenListModel = list.map(team => {
          val query2 =
            s"""
               |SELECT r.result_id as result_id, s.sportsman_id as id, s.first_name as name, s.last_name as surname, c.category as category, s.dob as dob,
               |r.final_result as result, r.wma as wma, ts.type as sport, p.personal_score as score
               |FROM participants as p
               |JOIN sportsmen as s ON s.sportsman_id = p.sportsman_id
               |JOIN results as r ON p.participant_id = r.participant_id
               |JOIN types_of_sport as ts ON ts.type_id = r.type_id
               |JOIN age_categories as c ON s.category_id = c.category_id
               |WHERE p.team_id=${team.getId}
               |ORDER BY r.wma DESC
           """.stripMargin
          val sportsmenListJson = entityManager.createNativeQuery(query2, classOf[SportsmanRow])
            .getResultList.map(_.asInstanceOf[SportsmanRow])

          val title = Seq("Місце", "Прізвище та ім'я", "Команда", "Категорія", "День народження", "Результат",
            "Рейтинг WMA", "Вид", "Особистий залік")
          val formattedSportsmen = sportsmenListJson
            .zipWithIndex.map { case (s, i) => java.util.Arrays.asList(
            (i + 1).toString,
            s.getName + " " + s.getSurname,
            team.getName,
            s.getCategory,
            s.getDob.toString,
            if (s.getResult != null) s.getResult.toString else "-",
            if (s.getWma != null) s.getWma.toString else "-",
            s.getSport,
            if (s.getScore) "+" else "-"
          )
          }
          val totalScore = sportsmenListJson
            .filter(p => !p.getScore && p.getWma != null)
            .foldLeft(List[SportsmanRow]()) {
              case (acc, item) if acc.count(i => i.getId == item.getId) == 2 => acc
              case (acc, item) => item :: acc
            }
            .map(el => BigDecimal(el.getWma)).sum
          new SportsmanScoreModel(team.getName, title, formattedSportsmen, totalScore.bigDecimal)
        })

        sportsmenListModel
          .sortBy(m => m.getTotalPoint)(Ordering[java.math.BigDecimal].reverse)
          .zipWithIndex.foreach { case (e, i) => e.setTeamPlace((i+1).toString) }

        new CompetitionScoresByTeamsModel(competition.getName, competition.getVenue, competition.getStartDate,
          competition.getEndDate, sportsmenListModel)
    }
  }
}
