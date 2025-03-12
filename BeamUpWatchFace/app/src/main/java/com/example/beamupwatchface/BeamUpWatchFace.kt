class BeamUpWatchFaceService : CanvasWatchFaceService() {
    override fun createCanvasRenderer(
        surfaceHolder: SurfaceHolder,
        currentUserStyleRepository: CurrentUserStyleRepository,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        coroutineScope: CoroutineScope,
    ): CanvasRenderer {
        return BeamUpCanvasRenderer(
            context = applicationContext,
            surfaceHolder = surfaceHolder,
            currentUserStyleRepository = currentUserStyleRepository,
            watchState = watchState,
            coroutineScope = coroutineScope,
            complicationSlotsManager = complicationSlotsManager
        )
    }
}
