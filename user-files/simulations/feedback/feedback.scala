package feedback

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import feedback.utilities.common_functions.print_error_processor
import feedback.requests.requests._

class Feedback extends Simulation {

  val environment = System.getProperty("URL")
  val users = Integer.getInteger("users")
  val ramp_up = Integer.getInteger("ramp_up")
  val duration = Integer.getInteger("duration")

  val webProtocol = http
    .baseURL(environment)
    .disableCaching
    .extraInfoExtractor(extraInfo => print_error_processor(extraInfo))

  val users_from_csv = csv("data/people.csv").circular
  val search_terms_from_csv = csv("data/searchterms.csv").circular
  val search_terms2_from_csv = csv("data/searchterms_shuffled.csv").circular
  val reviewer_from_csv = csv("data/reviewer.csv").circular
  val reviewee_from_csv = csv("data/reviewee.csv").circular

  def feedback: ScenarioBuilder = {
    scenario("feedback")
      .feed(users_from_csv)
      .feed(search_terms_from_csv)
      .feed(search_terms2_from_csv)
      .feed(reviewer_from_csv)
      .feed(reviewee_from_csv)
      .during(duration, exitASAP = true) {
        tryMax(10) {
          exec(addCookie(Cookie("FeedbackTesterMakeMe", "${email}")
            .withDomain("perf1.qpf.epam.com")
            .withPath("/")))
          .exec(SignIn)
          .exec(Incoming)
          .exec(Find_last_summary)
          .exec(History)
          .exec(session => {
            session.set("search_term", session("searchTerm").as[String].substring(0, 3))
          })
          .exec(Rewiewers_get)
          .exec(session => {
            session.set("search_term", session("searchTerm").as[String].substring(0, 4))
          })
          .exec(Rewiewers_get)
          .exec(session => {
            session.set("search_term", session("searchTerm").as[String].substring(0, 5))
          })
          .exec(Rewiewers_get)
          .exec(Rewiewers)
          .exec(Incoming_contains_id)
          .exec(Change_due_date)
          .exec(Incoming_contains_id)
          .exec(Delete)
          .exec(Incoming_not_contains_id)
          .exec(Outgoing_options)
          .exec(Outgoing)
          .exec(Outgoing_history)
          .exec(session => {
            session.set("search_term", session("searchTerm2").as[String].substring(0, 3))
          })
          .exec(Rewiewers_get)
          .exec(session => {
            session.set("search_term", session("searchTerm2").as[String].substring(0, 4))
          })
          .exec(Rewiewers_get)
          .exec(session => {
            session.set("search_term", session("searchTerm2").as[String].substring(0, 5))
          })
          .exec(Rewiewers_get)
          .exec(Rewiewers_outgoing)
          .exec(Outgoing_containst_id)
          .exec(Open_review)
          .exec(Previously_provided_reviews)
          .exec(Review_from_previous_summaries)
          .repeat(5)(
            exec(Save_as_draft)
            .exec(Open_outgoing_review)
          )
          .exec(Send_feedback)
          .exec(Open_outgoing_review)
          .exec(Share_feedback)
        }.exitHereIfFailed
      }
  }

  setUp(feedback.inject(rampUsers(users) over(ramp_up seconds)).protocols(webProtocol))
}
