package feedback.requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import java.time.LocalDate

object requests {

  val SignIn = exec(http("GET_/user/me")
    .get("/api/user/me")
    .check(status.is(200))
    .check(regex("authorities").find.exists)
    .check(jsonPath("$.personId").find.saveAs("userId"))
    .check(jsonPath("$.email").find.exists))
    .pause(1)

  val Incoming = exec(http("GET_/reviews/incoming")
    .get("/api/reviews/incoming/${userId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Find_last_summary = exec(http("GET_/reviews/incoming/.../find-last-summary")
    .get("/api/reviews/incoming/${userId}/find-last-summary")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val History = exec(http("GET_/reviews/incoming/.../history")
    .get("/api/reviews/incoming/${userId}/history?page=0&size=20")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Rewiewers_get = exec(http("GET_/people/.../reviewers")
    .get("/api/people/${userId}/reviewers")
    .queryParam("name", "${search_term}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Rewiewers = exec(http("POST_/api/reviews")
    .post("/api/reviews")
    .header("Content-Type", "application/json;charset=UTF-8")
    .body(StringBody("""{ "revieweeId": "${userId}", "reviewerId": "${reviewerId}" }""")).asJSON
    .check(status.is(201))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(jsonPath("$.data").find.saveAs("requestedReviewId")))
    .pause(1)

  val Incoming_contains_id = exec(http("GET_/reviews/incoming")
    .get("/api/reviews/incoming/${userId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(regex("${requestedReviewId}").find.exists))
    .pause(1)

  val Change_due_date = exec(session => {
      val dueDate = LocalDate.now().plusDays(5).toString()
      session.set("dueDate", dueDate)
    })
    .exec(http("PATCH_/api/reviews/.../change-due-date")
    .patch("/api/reviews/${requestedReviewId}/change-due-date")
    .header("Content-Type", "application/json;charset=UTF-8")
    .body(StringBody("""{ "id": "${requestedReviewId}", "dueDate": "${dueDate}" }""")).asJSON
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Delete = exec(http("DELETE_/api/reviews/")
    .delete("/api/reviews/${requestedReviewId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Incoming_not_contains_id = exec(http("GET_/reviews/incoming")
    .get("/api/reviews/incoming/${userId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(regex("${requestedReviewId}").find.notExists))
    .pause(1)

  val Outgoing_options = exec(http("OPT_/reviews/outgoing")
    .options("/api/reviews/outgoing/${userId}")
    .check(status.is(200)))
    .pause(1)

  val Outgoing = exec(http("GET_/reviews/outgoing")
    .get("/api/reviews/outgoing/${userId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Outgoing_history = exec(http("GET_/reviews/outgoing/.../history")
    .get("/api/reviews/outgoing/${userId}/history?page=0&size=20")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Rewiewers_outgoing = exec(http("POST_/api/reviews")
    .post("/api/reviews")
    .header("Content-Type", "application/json;charset=UTF-8")
    .body(StringBody("""{ "revieweeId": "${revieweeId}", "reviewerId": "${userId}" }""")).asJSON
    .check(status.is(201))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(jsonPath("$.data").find.saveAs("outgoingReviewId")))
    .pause(1)

  val Outgoing_containst_id = exec(http("GET_/reviews/outgoing")
    .get("/api/reviews/outgoing/${userId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(regex("${outgoingReviewId}").find.exists))
    .pause(1)

  val Open_review = exec(http("GET_/reviews/_-_open_review")
    .get("/api/reviews/${outgoingReviewId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(jsonPath("$..questionId").findAll.saveAs("questionIds"))
    .check(regex("\"id\":\"${outgoingReviewId}\"").find.exists)
    .check(regex("\"reviewer\":\\{\"id\":\"${userId}\"").find.exists)
    .check(regex("\"reviewee\":\\{\"id\":\"${revieweeId}\"").find.exists))
    .pause(1)

  val Previously_provided_reviews = exec(http("GET_/reviews/.../previously-provided-reviews")
    .get("/api/reviews/${outgoingReviewId}/previously-provided-reviews")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Review_from_previous_summaries = exec(http("GET_/reviews/.../reviews-from-previous-summaries")
    .get("/api/reviews/${outgoingReviewId}/reviews-from-previous-summaries?page=0&size=5")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)

  val Save_as_draft = exec(session => {
    val questions = session("questionIds").as[Seq[Any]]
    var filledQuestionaire = new StringBuilder()
    var isFirst = true
    val answerOptions = Array[String]("A", "B", "C", "D", "E")
    val randomGenerator = new scala.util.Random()
    for (i <- questions) {
      if (!isFirst) filledQuestionaire.append(", \n")
      val randomIndex = randomGenerator.nextInt(5)
      val answer = answerOptions(randomIndex)
      filledQuestionaire.append("{\"questionId\": \"").append(i).append("\", \"answer\": \"").append(answer).append("\"}")
      isFirst = false
    }
    session.set("filledQuestionaire", filledQuestionaire)
  }).exec(session => {
    val randomGenerator = new scala.util.Random()
    session.set("randomFeedbackText", randomGenerator.alphanumeric.take(300).mkString)
    })
    .exec(http("PATCH_/reviews/_-_save_as_draft")
      .patch("/api/reviews/${outgoingReviewId}")
      .header("Content-Type", "application/json;charset=UTF-8")
      .body(StringBody("""{ "answers": [${filledQuestionaire}], "saveAsDraft":true, "textualSummary":"<p>performance testing: ${randomFeedbackText}</p>", "loading":true }""")).asJSON
      .check(status.is(200))
      .check(regex("\"errors\":\\[\\]").find.exists)
      .check(regex("\"id\":\"${outgoingReviewId}\"").find.exists)
      .check(regex("\"reviewer\":\\{\"id\":\"${userId}\"").find.exists)
      .check(regex("\"reviewee\":\\{\"id\":\"${revieweeId}\"").find.exists))
    .pause(1)

  val Open_outgoing_review = exec(http("GET_/reviews/")
    .get("/api/reviews/${outgoingReviewId}")
    .check(status.is(200))
    .check(regex("\"errors\":\\[\\]").find.exists)
    .check(regex("\"id\":\"${outgoingReviewId}\"").find.exists)
    .check(regex("\"reviewer\":\\{\"id\":\"${userId}\"").find.exists)
    .check(regex("\"reviewee\":\\{\"id\":\"${revieweeId}\"").find.exists))
    .pause(1)

  val Send_feedback = exec(session => {
    val questions = session("questionIds").as[Seq[Any]]
    var filledQuestionaire = new StringBuilder()
    var isFirst = true
    val answerOptions = Array[String]("A", "B", "C", "D", "E")
    val randomGenerator = new scala.util.Random()
    for (i <- questions) {
      if (!isFirst) filledQuestionaire.append(", \n")
      val randomIndex = randomGenerator.nextInt(5)
      val answer = answerOptions(randomIndex)
      filledQuestionaire.append("{\"questionId\": \"").append(i).append("\", \"answer\": \"").append(answer).append("\"}")
      isFirst = false
    }
    session.set("filledQuestionaire", filledQuestionaire)
  }).exec(session => {
    val randomGenerator = new scala.util.Random()
    session.set("randomFeedbackText", randomGenerator.alphanumeric.take(300).mkString)
  })
    .exec(http("PATCH_/reviews/_-_send_feedback")
      .patch("/api/reviews/${outgoingReviewId}")
      .header("Content-Type", "application/json;charset=UTF-8")
      .body(StringBody("""{ "answers": [${filledQuestionaire}], "saveAsDraft":false, "textualSummary":"<p>performance testing: ${randomFeedbackText}</p>", "loading":true }""")).asJSON
      .check(status.is(200))
      .check(regex("\"errors\":\\[\\]").find.exists)
      .check(regex("\"id\":\"${outgoingReviewId}\"").find.exists)
      .check(regex("\"reviewer\":\\{\"id\":\"${userId}\"").find.exists)
      .check(regex("\"reviewee\":\\{\"id\":\"${revieweeId}\"").find.exists))
    .pause(1)

  val Share_feedback = exec(http("PATCH_/reviews/../share")
      .patch("/api/reviews/${outgoingReviewId}/share")
      .header("Content-Type", "application/json;charset=UTF-8")
      .check(status.is(200))
      .check(regex("\"errors\":\\[\\]").find.exists))
    .pause(1)
}
