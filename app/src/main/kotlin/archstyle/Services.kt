package archstyle
import java.util.*
import kotlin.math.roundToInt
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import archstyle.unauthgithubController
import archstyle.unauthgithubController
import com.google.gson.Gson
data class GitData(
    val authorizations_url: String,
    val code_search_url: String,
    val commit_search_url: String,
    val current_user_authorizations_html_url: String,
    val current_user_repositories_url: String,
    val current_user_url: String,
    val emails_url: String,
    val emojis_url: String,
    val events_url: String,
    val feeds_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val hub_url: String,
    val issue_search_url: String,
    val issues_url: String,
    val keys_url: String,
    val label_search_url: String,
    val notifications_url: String,
    val organization_repositories_url: String,
    val organization_teams_url: String,
    val organization_url: String,
    val public_gists_url: String,
    val rate_limit_url: String,
    val repository_search_url: String,
    val repository_url: String,
    val starred_gists_url: String,
    val starred_url: String,
    val topic_search_url: String,
    val user_organizations_url: String,
    val user_repositories_url: String,
    val user_search_url: String,
    val user_url: String
)

data class Population(
    val data: List<Data>,
    val source: List<Source>
)

data class Data(
    val ID_Nation: String,
    val ID_Year: Int,
    val Nation: String,
    val Population: Int,
    val Slug_Nation: String,
    val Year: String
)

data class Source(
    val annotations: Annotations,
    val measures: List<String>,
    val name: String,
    val substitutions: List<Any>
)

data class Annotations(
    val dataset_link: String,
    val dataset_name: String,
    val source_description: String,
    val source_name: String,
    val subtopic: String,
    val table_id: String,
    val topic: String
)

class UnAuthGithubService {
   suspend fun getgithubInfo():Population
     {
    val client = HttpClient(CIO)
    val httpResponse: HttpResponse = client.get("https://datausa.io/api/data?drilldowns=Nation&measures=Population")
    val stringBody: String = httpResponse.readText()
    val population:Population = Gson().fromJson(stringBody, Population::class.java)
    client.close()
    return population
    }

}
