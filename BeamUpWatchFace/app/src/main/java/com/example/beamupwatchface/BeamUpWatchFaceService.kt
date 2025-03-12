package com.example.beamupwatchface

import android.graphics.Rect
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.CanvasWatchFaceService
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer.CanvasRenderer
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.WatchState
import kotlinx.coroutines.CoroutineScope

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
            complicationSlotsManager = complicationSlotsManager,
        )
    }
}
