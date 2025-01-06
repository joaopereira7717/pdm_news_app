package com.example.newsapp.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.components.HorizontalScrollable
import com.example.newsapp.components.NewsCard
import com.example.newsapp.components.Pill
import com.example.newsapp.components.PillData
import com.example.newsapp.network.requestHandler
import com.example.newsapp.network.retrofit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLEncoder

data class Article(
    val author: String,
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val url: String,
)

data class News (
    val articles: List<Article>,
    val totalResults: Int,
)

interface NewsApi {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int
    ): Response<News>
}

val authApi = retrofit.create(NewsApi::class.java)

@Composable
fun HomeScreen(context: MainActivity, navigation: NavHostController) {
    val isLoading = remember { mutableStateOf(true) }
    val data = remember { mutableStateListOf<Article>() }
    val selectedPill = remember { mutableStateOf<String>("Football") }
    val pills = listOf(
        PillData("Gaming", R.drawable.ic_gaming),
        PillData("Boxing", R.drawable.ic_boxing),
        PillData("Cars", R.drawable.ic_cars),
        PillData("Tech", R.drawable.ic_tech),
        PillData("Cyber Security", R.drawable.ic_cybersec),
    )

    LaunchedEffect(selectedPill.value) {
        isLoading.value = true
        requestHandler(
            apiCall = {
                authApi.getNews(
                    query = selectedPill.value,
                    fromDate = "25-12-2024", //25th December 2024
                    sortBy = "publishedAt",
                    apiKey = "4fb34e57e64e4814a9e985ac5cba796f",
                    pageSize = 15
                )
            },
            onSuccess = { response ->
                data.clear()

                var articles = response.articles

                //verify if the article is removed and remove it from the list
                for (article in articles) {
                    if (article.title == "[Removed]") {
                        articles = articles.filter { it != article }
                    }
                }

                data.addAll(articles)
            },
            onError = { error ->
                println("DEBUG ERROR > $error")
            },
            isLoading = { loading ->
                isLoading.value = loading
            }
        )
    }

    val onPillClick: (String) -> Unit = { pill ->
        selectedPill.value = pill
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.mipmap.img),
                contentDescription = "App logo",
                modifier = Modifier
                    .width(90.dp)
                    .padding(vertical = 10.dp),
                contentScale = ContentScale.Fit,
            )
        }
        HorizontalScrollable(
            modifier = Modifier.padding(bottom = 10.dp),
            content = {
                pills.forEach { pill ->
                    Pill(
                        text = pill.text,
                        onClick = { onPillClick(pill.text) },
                        isSelected = pill.text == selectedPill.value,
                        icon = pill.icon
                    )
                }
            },
        )

        if (data.size == 0 && !isLoading.value) {
            NoDataListState()
        }

        if (data.size == 0 && isLoading.value) {
            LoadingListState()
        }

        LazyColumn(
            content = {
                items(data.size) { index ->
                    NewsCard(
                        title = data[index].title ?: "",
                        author = data[index].author ?: "",
                        image = data[index].urlToImage ?: "",
                        date = data[index].publishedAt ?: "",
                        onClick = {
                            val url: String = URLEncoder.encode(data[index].url)
                            val title: String = URLEncoder.encode(data[index].title)
                            navigation.navigate("article/$url/$title")
                        }
                    )
                }
            }
        )
    }
}


@Composable
fun LoadingListState() {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No data found",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun NoDataListState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No data found",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(10.dp)
        )
    }
}