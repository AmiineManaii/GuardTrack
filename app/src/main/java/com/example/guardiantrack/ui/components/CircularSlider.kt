package com.example.guardiantrack.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guardiantrack.ui.theme.GtMagicCyan
import com.example.guardiantrack.ui.theme.GtMagicPurple
import com.example.guardiantrack.ui.theme.GtTextPrimary
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 5f..30f,
    size: Dp = 200.dp,
    strokeWidth: Float = 40f,
    activeColor: Color = GtMagicCyan,
    inactiveColor: Color = Color.DarkGray.copy(alpha = 0.3f)
) {
    var angle by remember {
        val initialAngle = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * 360f
        mutableStateOf(initialAngle)
    }

    // Sync angle when value changes externally
    LaunchedEffect(value) {
        angle = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)) * 360f
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val touchX = change.position.x - (size.toPx() / 2)
                        val touchY = change.position.y - (size.toPx() / 2)
                        var newAngle = Math.toDegrees(atan2(touchY.toDouble(), touchX.toDouble())).toFloat()
                        
                        // Convert to 0-360 range
                        newAngle = (newAngle + 450f) % 360f
                        
                        angle = newAngle
                        val newValue = valueRange.start + (newAngle / 360f) * (valueRange.endInclusive - valueRange.start)
                        onValueChange(newValue)
                    }
                }
        ) {
            val canvasSize = this.size
            val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
            val radius = (canvasSize.minDimension / 2) - (strokeWidth / 2) - 20f

            // Decorative ticks (Radio style)
            for (i in 0 until 360 step 15) {
                val tickAngleRad = Math.toRadians((i - 90f).toDouble())
                val startTickX = center.x + (radius + strokeWidth / 2 + 10f) * cos(tickAngleRad).toFloat()
                val startTickY = center.y + (radius + strokeWidth / 2 + 10f) * sin(tickAngleRad).toFloat()
                val endTickX = center.x + (radius + strokeWidth / 2 + 25f) * cos(tickAngleRad).toFloat()
                val endTickY = center.y + (radius + strokeWidth / 2 + 25f) * sin(tickAngleRad).toFloat()
                
                drawLine(
                    color = if (i <= angle) activeColor.copy(alpha = 0.5f) else inactiveColor,
                    start = Offset(startTickX, startTickY),
                    end = Offset(endTickX, endTickY),
                    strokeWidth = 2f
                )
            }

            // Inactive track
            drawArc(
                color = inactiveColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Active track with magic gradient
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to GtMagicPurple,
                    1.0f to GtMagicCyan,
                    center = center
                ),
                startAngle = -90f,
                sweepAngle = angle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Thumb (The radio knob)
            val thumbAngleRad = Math.toRadians((angle - 90f).toDouble())
            val thumbX = center.x + radius * cos(thumbAngleRad).toFloat()
            val thumbY = center.y + radius * sin(thumbAngleRad).toFloat()

            drawCircle(
                color = Color.White,
                radius = strokeWidth / 1.5f,
                center = Offset(thumbX, thumbY)
            )
            
            drawCircle(
                color = activeColor,
                radius = strokeWidth / 3f,
                center = Offset(thumbX, thumbY)
            )

            // Text in the center
            val text = "%.1f".format(value)
            drawContext.canvas.nativeCanvas.apply {
                val paint = Paint().apply {
                    color = GtTextPrimary.toArgb()
                    textSize = 40.sp.toPx()
                    textAlign = Paint.Align.CENTER
                    isFakeBoldText = true
                }
                drawText(
                    text,
                    center.x,
                    center.y + (paint.textSize / 3),
                    paint
                )
                
                paint.textSize = 14.sp.toPx()
                paint.isFakeBoldText = false
                drawText(
                    "m/s²",
                    center.x,
                    center.y + (paint.textSize * 2),
                    paint
                )
            }
        }
    }
}
