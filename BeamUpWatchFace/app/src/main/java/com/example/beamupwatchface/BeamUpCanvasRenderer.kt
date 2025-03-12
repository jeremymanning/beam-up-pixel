class BeamUpCanvasRenderer(
    context: Context,
    surfaceHolder: SurfaceHolder,
    currentUserStyleRepository: CurrentUserStyleRepository,
    watchState: WatchState,
    coroutineScope: CoroutineScope,
    complicationSlotsManager: ComplicationSlotsManager,
) : CanvasRenderer(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    CanvasType.HARDWARE
) {

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = 100f
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var previousMinute: Int = -1
    private var offsetY = 0f

    override fun render(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        canvas.drawColor(Color.BLACK)

        val hour = zonedDateTime.hour
        val minute = zonedDateTime.minute

        if (minute != previousMinute) {
            previousMinute = minute
            animateMinuteChange()
        }

        val hourText = String.format("%02d", hour % 12)
        val minuteText = String.format("%02d", minute)

        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()

        paint.textSize = bounds.width() / 2.5f

        // Draw Hour
        canvas.drawText(hourText(hour), centerX, centerY - paint.textSize / 2 + offsetY, paint)

        // Draw Minute
        canvas.drawText(minuteText(minute), centerX, centerY + paint.textSize / 1.5f + offsetY, paint)

        previousMinute = minute
    }

    private fun hourText(hour: Int): String = String.format("%02d", if (hour % 12 == 0) 12 else hour % 12)

    private var previousMinute = -1

    private fun animateTimeChange() {
        val animator = ValueAnimator.ofFloat(50f, 0f).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                offsetY = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    init {
        previousMinute = -1
    }

    override fun renderHighlightLayer(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {}
}
