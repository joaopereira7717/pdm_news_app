package com.example.myapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun AsyncImage(
    image: String,
    description: String = "I was lazy...",
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.FillWidth
) {
    val isLoading = remember { mutableStateOf<Boolean>(true)}

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .listener(
                onError = { _, _ ->
                    isLoading.value = false
                },
                onSuccess = { _, _  ->
                    isLoading.value = false
                },
            )
            .build())

    if (isLoading.value == true) {
        Box(modifier = modifier
            .background(
                Color.Red,
                shape = RoundedCornerShape(10.dp)
            )
        )
    }
    Row(
        modifier = Modifier.background(
            Color.Gray,
            shape = RoundedCornerShape(10.dp)
        )
    ) {
        Image(
            modifier = modifier,
            painter = painter,
            contentScale = contentScale,
            contentDescription = description,
        )
    }

}