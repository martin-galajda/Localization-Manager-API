package filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.http.Status
import play.api.mvc._
import play.Configuration
import scala.concurrent.{ExecutionContext, Future}

class CORSFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, config: Configuration) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    println(requestHeader)
    val accessControlAllowOrigin = config.getString("http.client.address")

    nextFilter(requestHeader).map { result =>
      System.err.println(result)
      result.withHeaders(
        //"Access-Control-Allow-Origin" -> "http://localhost:3000",
        //"Access-Control-Allow-Origin" -> "https://morning-taiga-56897.herokuapp.com",
        "Access-Control-Allow-Origin" -> accessControlAllowOrigin,
        "Access-Control-Allow-Methods" -> "GET, POST, OPTIONS, DELETE, PUT",
        "Access-Control-Allow-Headers" -> "Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With, X-XSRF-TOKEN",
        "Access-Control-Allow-Credentials" -> "true"
      )
    }
  }
}