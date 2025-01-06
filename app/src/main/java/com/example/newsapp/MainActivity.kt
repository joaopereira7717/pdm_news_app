package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.Views.Article
import com.example.newsapp.Views.ArticleScreen
import com.example.newsapp.Views.HomeScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding))  {
                        composable( route = "home"){
                            HomeScreen(
                                context = this@MainActivity,
                                navigation = navController
                            )
                        }
                        composable(route = "article/{url}/{title}"){
                            val url = it.arguments?.getString("url")
                            val title = it.arguments?.getString("title")

                            url?.let {
                                ArticleScreen(
                                    url = URLDecoder.decode(url, "UTF-8"),
                                    title = URLDecoder.decode(title, "UTF-8")  ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}