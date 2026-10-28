package com.jara.lavacampus.ui.theme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
val Typography = Typography(
 displaySmall=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=42.sp,lineHeight=46.sp,fontWeight=FontWeight.ExtraBold,letterSpacing=(-1.5).sp),
 headlineLarge=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=34.sp,lineHeight=39.sp,fontWeight=FontWeight.Bold,letterSpacing=(-.8).sp),
 headlineMedium=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=28.sp,lineHeight=34.sp,fontWeight=FontWeight.Bold,letterSpacing=(-.5).sp),
 headlineSmall=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=24.sp,lineHeight=30.sp,fontWeight=FontWeight.Bold),
 titleLarge=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=21.sp,lineHeight=28.sp,fontWeight=FontWeight.SemiBold),
 titleMedium=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=16.sp,lineHeight=23.sp,fontWeight=FontWeight.SemiBold),
 bodyLarge=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=16.sp,lineHeight=24.sp),
 bodyMedium=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=14.sp,lineHeight=21.sp),
 bodySmall=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=12.sp,lineHeight=18.sp),
 labelLarge=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=14.sp,lineHeight=20.sp,fontWeight=FontWeight.Bold),
 labelMedium=TextStyle(fontFamily=FontFamily.SansSerif,fontSize=12.sp,lineHeight=17.sp,fontWeight=FontWeight.Medium,letterSpacing=.5.sp))
