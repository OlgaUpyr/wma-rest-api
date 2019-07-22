package com.wma.utils

import java.io.{ByteArrayOutputStream, OutputStreamWriter}

import com.opencsv.CSVWriter
import com.wma.domains.{Attempt, Participant, Result}
import com.wma.exceptions.{BadRequestException, NotFoundException}
import com.wma.jsons.CompetitionScoresByTeamsModel
import com.wma.repositories._
import com.wma.util.TimeUtil
import javax.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConversions._

trait RatingUtil {
  def getResultBySport(participantId: Long, sportId: Integer): Result
  def getParticipantByCompetitionAndSport(competitionId: Long, sportId: Integer): Seq[Participant]
  def writeTimeResult(participant: Participant, sportId: Integer, hours: Integer, minutes: Integer, seconds: Integer, miliseconds: Integer): Boolean
  def updateTimeResult(participant: Participant, sportId: Integer, attemptId: Long, hours: Integer, minutes: Integer, seconds: Integer, miliseconds: Integer): Boolean
  def writeDistanceResult(participant: Participant, sportId: Integer, result: java.math.BigDecimal, attempt: Int): Boolean
  def updateDistanceResult(participant: Participant, sportId: Integer, attemptId: Long, result: java.math.BigDecimal, attempt: Int): Boolean
  def getCompetitionReportByTeam(competition: CompetitionScoresByTeamsModel): Array[Byte]
}

@Component
@Transactional
class RatingUtilImpl(competitionRepository: CompetitionRepository,
                     participantRepository: ParticipantRepository,
                     resultRepository: ResultRepository,
                     typeOfSportRepository: TypeOfSportRepository,
                     attemptRepository: AttemptRepository,
                     timeUtil: TimeUtil, entityManager: EntityManager) extends RatingUtil {

  override def getResultBySport(participantId: Long, sportId: Integer): Result = {
    resultRepository.findResultByParticipantAndSport(participantId, sportId)
  }

  override def getParticipantByCompetitionAndSport(competitionId: Long, sportId: Integer): Seq[Participant] = {
    val competition = competitionRepository.findById(competitionId)
    if (competition == null) {
      throw NotFoundException()
    }

    val sport = typeOfSportRepository.findById(sportId)
    if (sport == null) {
      throw NotFoundException()
    }

    participantRepository.findParticipantByCompetitionAndSport(competitionId, sportId)
  }

  def convertTimeResult(hours: Int, minutes: Int, seconds: Int, miliseconds: Int): BigDecimal = {
    hours*3600 + minutes*60 + seconds + miliseconds*0.001
  }

  def getFinalResult(participant: Participant, sportId: Integer, actualResult: BigDecimal): BigDecimal = {
    val ageCategory = participant.getSportsman.getCategory.getCategoryName

    val query =
      s"""
         |SELECT $ageCategory FROM wma_parameters WHERE type_id=$sportId
       """.stripMargin
    val wmaFactor = entityManager.createNativeQuery(query).getResultList.map(_.asInstanceOf[java.math.BigDecimal]).head

    actualResult*wmaFactor
  }

  def calculateWMA(participant: Participant, sportId: Integer, finalResult: BigDecimal): BigDecimal = {
    val recordByGender = participant.getSportsman.getGender match {
      case "Чоловіча" => "record_male"
      case "Жіноча" => "record_female"
      case _ => BadRequestException()
    }
    val query =
      s"""
         |SELECT $recordByGender FROM types_of_sport WHERE type_id=$sportId
       """.stripMargin

    val record = entityManager.createNativeQuery(query).getResultList.map(_.asInstanceOf[java.math.BigDecimal]).head

    if(typeOfSportRepository.findById(sportId).getIsRunning) {
      BigDecimal(record)/finalResult*100
    } else {
      finalResult/BigDecimal(record)*100
    }
  }

  override def writeTimeResult(participant: Participant, sportId: Integer, hours: Integer, minutes: Integer, seconds: Integer, miliseconds: Integer): Boolean = {
    val sport = typeOfSportRepository.findById(sportId)
    if (sport == null) {
      throw NotFoundException()
    }

    val actualResult = convertTimeResult(hours, minutes, seconds, miliseconds).bigDecimal

    val existingResult = resultRepository.findResultByParticipantAndSport(participant.getId, sportId)

    val attempt = new Attempt()
    attempt.setResult(existingResult)
    attempt.setAttempt(true)
    attempt.setActualResult(actualResult)
    attemptRepository.save(attempt)

    val finalResult = getFinalResult(participant, sportId, actualResult).bigDecimal
    val wma = calculateWMA(participant, sportId, finalResult).bigDecimal

    existingResult.setFinalResult(finalResult)
    existingResult.setWma(wma)
    resultRepository.save(existingResult)

    true
  }

  override def updateTimeResult(participant: Participant, sportId: Integer, attemptId: Long, hours: Integer, minutes: Integer, seconds: Integer, miliseconds: Integer): Boolean = {
    val actualResult = convertTimeResult(hours, minutes, seconds, miliseconds).bigDecimal
    val finalResult = getFinalResult(participant, sportId, actualResult).bigDecimal
    val wma = calculateWMA(participant, sportId, finalResult).bigDecimal

    val existingResult = resultRepository.findResultByParticipantAndSport(participant.getId, sportId)
    existingResult.setFinalResult(finalResult)
    existingResult.setWma(wma)
    resultRepository.save(existingResult)

    val attempt = attemptRepository.findOne(attemptId)
    attempt.setActualResult(convertTimeResult(hours, minutes, seconds, miliseconds).bigDecimal)
    attemptRepository.save(attempt)

    true
  }

  override def writeDistanceResult(participant: Participant, sportId: Integer, actualResult: java.math.BigDecimal, attemptValue: Int): Boolean = {
    val sport = typeOfSportRepository.findById(sportId)
    if (sport == null) {
      throw NotFoundException()
    }

    val attempt = new Attempt()
    attempt.setResult(resultRepository.findResultByParticipantAndSport(participant.getId, sportId))

    attemptValue match {
      case -1 => attempt.setAttempt(false)
      case 0 => attempt.setAttempt(null)
      case 1 => attempt.setAttempt(true)
    }

    attempt.setActualResult(actualResult)
    attemptRepository.save(attempt)

    val existingResult = resultRepository.findResultByParticipantAndSport(participant.getId, sportId)
    if(attemptValue > 0) {
      val maxResult = existingResult.getAttempts match {
        case attempts if attempts.isEmpty => null
        case attempts => attempts.filter(e => e.getAttempt == true) match {
          case filteredAttempts if filteredAttempts.isEmpty => null
          case filteredAttempts => filteredAttempts.maxBy(r => r.getActualResult)
        }
      }
      if(maxResult == null || actualResult.compareTo(maxResult.getActualResult) > 0) {
        val finalResult = getFinalResult(participant, sportId, actualResult).bigDecimal
        val wma = calculateWMA(participant, sportId, finalResult).bigDecimal

        existingResult.setFinalResult(finalResult)
        existingResult.setWma(wma)
        resultRepository.save(existingResult)
      }
    }

    true
  }

  override def updateDistanceResult(participant: Participant, sportId: Integer, attemptId: Long, actualResult: java.math.BigDecimal, attemptValue: Int): Boolean = {
    val sport = typeOfSportRepository.findById(sportId)
    if (sport == null) {
      throw NotFoundException()
    }

    val attempt = attemptRepository.findOne(attemptId)
    attempt.setResult(resultRepository.findResultByParticipantAndSport(participant.getId, sportId))

    attemptValue match {
      case -1 => attempt.setAttempt(false)
      case 0 => attempt.setAttempt(null)
      case 1 => attempt.setAttempt(true)
    }

    attempt.setActualResult(actualResult)
    attemptRepository.save(attempt)

    val existingResult = resultRepository.findResultByParticipantAndSport(participant.getId, sportId)
    val maxResult = existingResult.getAttempts match {
      case attempts if attempts.isEmpty => null
      case attempts => attempts.filter(e => e.getAttempt == true) match {
        case filteredAttempts if filteredAttempts.isEmpty => null
        case filteredAttempts => filteredAttempts.maxBy(r => r.getActualResult)
      }
    }

    val (finalResult, wma) = maxResult match {
      case null => (null, null)
      case _ if attemptValue > 0 && actualResult.compareTo(maxResult.getActualResult) > 0 =>
        val res = getFinalResult(participant, sportId, actualResult).bigDecimal
        (res, calculateWMA(participant, sportId, res).bigDecimal)
      case _ =>
        val res = getFinalResult(participant, sportId, maxResult.getActualResult).bigDecimal
        (res, calculateWMA(participant, sportId, res).bigDecimal)
    }

    existingResult.setFinalResult(finalResult)
    existingResult.setWma(wma)
    resultRepository.save(existingResult)

    true
  }

  override def getCompetitionReportByTeam(competition: CompetitionScoresByTeamsModel): Array[Byte] = {
    val output = new ByteArrayOutputStream()
    val outputWriter = new OutputStreamWriter(output)
    val csvWriter = new CSVWriter(outputWriter)

    csvWriter.writeNext(Array(competition.getCompetitionName))
    csvWriter.writeNext(Array("Місце проведення:", competition.getCompetitionVenue))
    csvWriter.writeNext(Array("Тривалість змагань:", competition.getStartDate.toString,"-",competition.getEndDate.toString))
    csvWriter.writeNext(Array())

    competition.getSportsmen.foreach(s => {
      csvWriter.writeNext(Array(s.getTeamName))
      csvWriter.writeNext(Array())
      var itemsArray = new Array[String](s.getTitle.size)
      csvWriter.writeNext(s.getTitle.toArray(itemsArray))
      s.getData.foreach(r => {
        val p: Seq[String] = r
        csvWriter.writeNext(p.toArray)
      })
      csvWriter.writeNext(Array())
      csvWriter.writeNext(Array("Загальний бал:", s.getTotalPoint.toString))
      csvWriter.writeNext(Array("Місце:", s.getTeamPlace))
      csvWriter.writeNext(Array())
    })

    csvWriter.close()
    outputWriter.close()

    val res = output.toByteArray
    output.close()
    res
  }
}
