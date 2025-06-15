package com.example.beamupwatchface

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceHolder
import android.view.animation.DecelerateInterpolator
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import java.time.ZonedDateTime

data class DigitState(
    val hour1: Int,
    val hour2: Int,
    val minute1: Int,
    val minute2: Int
)

class BeamUpCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
) : Renderer.CanvasRenderer(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    CanvasType.HARDWARE,
    16L // 60fps for smooth animations
) {

    // Paint objects for rendering
    private val digitPaint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        isAntiAlias = true
    }

    private val beamPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 2f
        isAntiAlias = true
    }

    // Digit state tracking
    private var currentDigitState = DigitState(0, 0, 0, 0)
    private var previousDigitState = DigitState(0, 0, 0, 0)
    
    // Animation state
    private val digitAnimators = mutableMapOf<Int, ValueAnimator>()
    private val beamAnimators = mutableMapOf<Int, ValueAnimator>()
    private val digitOffsets = mutableMapOf<Int, Float>()
    private val beamHeights = mutableMapOf<Int, Float>()
    
    // Animation parameters
    private val animationDuration = 400L
    private val beamAnimationDuration = 600L
    
    // Digit positions (will be calculated based on screen size)
    private var digitWidth = 0f
    private var digitHeight = 0f
    private var watchCenterX = 0f
    private var watchCenterY = 0f

    override fun render(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        // Clear background
        canvas.drawColor(Color.BLACK)
        
        // Calculate dimensions if not set
        if (digitWidth == 0f) {
            calculateDimensions(bounds)
        }
        
        // Get current time
        val hour = zonedDateTime.hour
        val minute = zonedDateTime.minute
        
        // Create new digit state
        val newDigitState = DigitState(
            hour1 = if (hour == 0 || hour == 12) 1 else (hour % 12) / 10,
            hour2 = if (hour == 0 || hour == 12) 2 else (hour % 12) % 10,
            minute1 = minute / 10,
            minute2 = minute % 10
        )
        
        // Check for digit changes and trigger animations
        checkForDigitChanges(newDigitState)
        
        // Update current state
        currentDigitState = newDigitState
        
        // Draw digits with animations
        drawDigits(canvas, bounds)
        
        // Draw beam effects
        drawBeams(canvas, bounds)
    }

    private fun calculateDimensions(bounds: Rect) {
        watchCenterX = bounds.exactCenterX()
        watchCenterY = bounds.exactCenterY()
        
        // Calculate optimal text size based on screen size
        val baseSize = bounds.width() * 0.15f
        digitPaint.textSize = baseSize
        
        // Measure digit dimensions
        val testRect = Rect()
        digitPaint.getTextBounds("00", 0, 2, testRect)
        digitWidth = testRect.width().toFloat() * 0.6f
        digitHeight = testRect.height().toFloat()
    }

    private fun checkForDigitChanges(newState: DigitState) {
        val positions = listOf(0, 1, 2, 3) // hour1, hour2, minute1, minute2
        val oldDigits = listOf(currentDigitState.hour1, currentDigitState.hour2, 
                              currentDigitState.minute1, currentDigitState.minute2)
        val newDigits = listOf(newState.hour1, newState.hour2, 
                              newState.minute1, newState.minute2)
        
        positions.forEach { pos ->
            if (oldDigits[pos] != newDigits[pos]) {
                animateDigitChange(pos)
                animateBeam(pos)
            }
        }
    }

    private fun animateDigitChange(position: Int) {
        // Cancel existing animation if running
        digitAnimators[position]?.cancel()
        
        // Create new digit animation
        val animator = ValueAnimator.ofFloat(-digitHeight, 0f).apply {
            duration = animationDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                digitOffsets[position] = animation.animatedValue as Float
                invalidate()
            }
        }
        
        digitAnimators[position] = animator
        animator.start()
    }

    private fun animateBeam(position: Int) {
        // Cancel existing beam animation if running
        beamAnimators[position]?.cancel()
        
        // Create beam extension and retraction animation
        val animator = ValueAnimator.ofFloat(0f, digitHeight * 2f, 0f).apply {
            duration = beamAnimationDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                beamHeights[position] = animation.animatedValue as Float
                invalidate()
            }
        }
        
        beamAnimators[position] = animator
        animator.start()
    }

    private fun drawDigits(canvas: Canvas, bounds: Rect) {
        val digits = listOf(
            if (currentDigitState.hour1 == 0) " " else currentDigitState.hour1.toString(),
            currentDigitState.hour2.toString(),
            currentDigitState.minute1.toString(),
            currentDigitState.minute2.toString()
        )
        
        val positions = listOf(
            watchCenterX - digitWidth * 1.5f,  // hour1
            watchCenterX - digitWidth * 0.5f,  // hour2  
            watchCenterX + digitWidth * 0.5f,  // minute1
            watchCenterX + digitWidth * 1.5f   // minute2
        )
        
        digits.forEachIndexed { index, digit ->
            val x = positions[index]
            val y = watchCenterY + (digitOffsets[index] ?: 0f)
            
            canvas.drawText(digit, x, y, digitPaint)
        }
        
        // Draw colon separator
        val colonY = watchCenterY - digitHeight * 0.3f
        canvas.drawCircle(watchCenterX, colonY, 4f, digitPaint)
        canvas.drawCircle(watchCenterX, colonY + digitHeight * 0.3f, 4f, digitPaint)
    }

    private fun drawBeams(canvas: Canvas, bounds: Rect) {
        val positions = listOf(
            watchCenterX - digitWidth * 1.5f,  // hour1
            watchCenterX - digitWidth * 0.5f,  // hour2  
            watchCenterX + digitWidth * 0.5f,  // minute1
            watchCenterX + digitWidth * 1.5f   // minute2
        )
        
        positions.forEachIndexed { index, x ->
            val beamHeight = beamHeights[index] ?: 0f
            if (beamHeight > 0f) {
                // Draw vertical beam extending up from digit position
                val startY = watchCenterY - digitHeight * 0.5f
                val endY = startY - beamHeight
                
                // Main beam line
                canvas.drawLine(x, startY, x, endY, beamPaint)
                
                // Add some beam particles for effect
                for (i in 0 until 5) {
                    val particleY = startY - (beamHeight * (i + 1) / 6f)
                    val particleSize = (5f - i) * (beamHeight / (digitHeight * 2f))
                    canvas.drawCircle(x, particleY, particleSize, beamPaint)
                }
            }
        }
    }

    override fun renderHighlightLayer(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        // No highlight layer needed for this watch face
    }
}