package com.example.notetalk.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.notetalk.R

val colors = listOf(
    Color(0xFFEA4335), // Red
    Color(0xFFFBBC05), // Yellow
    Color(0xFF34A853), // Green
    Color(0xFF4285F4)  // Blue
)

@Composable
fun ProfileIcon(
    imagePainter: Painter,
    ringColors: List<Color>,
    size: Dp = 60.dp,
    strokeWidth: Dp = 4.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val innerRadius = (size - strokeWidth).toPx()/2
            val outerRadius = size.toPx()/2
            val sweepAngle = 360f/ringColors.size
            var startAngle = -90f

            for(color in ringColors) {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx()),
                    topLeft = center - Offset(outerRadius, outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2)
                )
                startAngle += sweepAngle
            }
        }
        Image(
            painter = imagePainter,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(size - 2 * strokeWidth) // Adjust image size to fit inside the ring
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun ProfilePreview() {
    ProfileIcon(
        imagePainter = painterResource(id = R.drawable.user1),
        ringColors = colors
    )
}