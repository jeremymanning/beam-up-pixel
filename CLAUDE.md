# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Wear OS watch face application called "Beam Up" - a clone of the original Pebble/Fitbit watch face implementation. The project is built using Kotlin and the AndroidX WearOS watchface APIs.

## Build System

This is a standard Android Gradle project targeting Wear OS devices.

### Key Build Commands
```bash
# Navigate to the Android project directory first
cd BeamUpWatchFace

# Build the project
./gradlew build

# Clean build artifacts
./gradlew clean

# Install debug build to connected device
./gradlew installDebug

# Run lint checks
./gradlew lint

# Run unit tests
./gradlew test

# Assemble APK
./gradlew assemble
```

## Architecture

The watch face follows the standard Wear OS watch face architecture:

- **BeamUpWatchFaceService**: Entry point service that creates the canvas renderer
- **BeamUpCanvasRenderer.kt**: Main rendering class that handles drawing the watch face and animations
- **BeamUpWatchFace**: Configuration and style management
- **BeamUpWatchFaceColors**: Color scheme definitions

### Key Components

- Uses AndroidX Wear WatchFace APIs (1.2.1)
- Canvas-based rendering with hardware acceleration
- Time-based animations with ValueAnimator
- Monospace font rendering for digital time display
- 12-hour format display

### Implementation Details

- **Beam-up Animation**: Digits animate upward when changing, with extending beam effects
- **Canvas Rendering**: Uses hardware-accelerated canvas with 60fps animation target
- **Digital Time Display**: Shows 12-hour format with animated colon separator
- **State Tracking**: Monitors digit changes to trigger appropriate animations
- **Visual Effects**: Beam lines with particle effects for sci-fi aesthetic

## Development Notes

- Minimum SDK: 26 (Android 8.0)
- Target SDK: 34
- Kotlin 1.9.0
- Uses hardware-accelerated canvas rendering
- No complications or settings UI currently implemented