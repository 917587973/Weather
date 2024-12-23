package com.avinash.weather.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.avinash.weather.R

val fonts = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins, FontWeight.Normal),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 15.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        fontSize = 40.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        fontSize = 18.sp,
        letterSpacing = 0.sp,
    ),
)