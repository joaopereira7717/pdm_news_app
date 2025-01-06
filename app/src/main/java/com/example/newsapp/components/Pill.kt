package com.example.newsapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.newsapp.R
import com.example.newsapp.ui.theme.Blue40
import com.example.newsapp.ui.theme.LightBlue80

data class PillData(
    val text: String,
    val icon: Int
)

@Composable
fun Pill(
    text: String,
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    icon: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (isSelected) LightBlue80.copy(.5f) else Blue40,
                shape = RectangleShape
            )
            .padding(10.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "$text Icon",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier =  Modifier.padding(horizontal = 3.dp))
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Medium,
        )
    }
}