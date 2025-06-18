#!/bin/bash

# Test watch face on multiple screen configurations
echo "Testing Beam Up watch face on multiple screen sizes..."

# Common Wear OS screen configurations
declare -a CONFIGS=(
    "320x320:round"    # Small round (e.g., older Wear OS devices)
    "384x384:round"    # Medium round (e.g., Pixel Watch)
    "454x454:round"    # Large round (e.g., Galaxy Watch)
    "280x280:square"   # Small square
    "340x340:square"   # Medium square
)

# Function to take screenshot
take_screenshot() {
    local size=$1
    local shape=$2
    local filename="screenshot_${size}_${shape}.png"
    
    echo "Taking screenshot for $size $shape..."
    sleep 3  # Wait for watch face to render
    adb shell screencap -p /sdcard/screenshot.png
    adb pull /sdcard/screenshot.png screenshots/$filename
    adb shell rm /sdcard/screenshot.png
    echo "Screenshot saved to screenshots/$filename"
}

# Create screenshots directory
mkdir -p screenshots

# Start the large round emulator we already have
echo "Starting Wear OS Large Round emulator..."
~/Library/Android/sdk/emulator/emulator -avd Wear_OS_Large_Round &
EMULATOR_PID=$!

# Wait for emulator to boot
echo "Waiting for emulator to boot..."
adb wait-for-device
sleep 30  # Give it time to fully boot

# Install the watch face
echo "Installing watch face..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Set the watch face
echo "Please manually select the Beam Up watch face on the emulator"
echo "Press Enter when ready..."
read

# Take screenshot for large round
take_screenshot "454x454" "round"

# Note: To test other sizes, you would need to:
# 1. Create additional AVDs with different screen sizes
# 2. Or use the emulator's device frame feature to simulate different sizes
# 3. Or adjust the emulator window size (though this is less accurate)

echo "Testing complete! Check the screenshots directory for results."

# Kill the emulator
kill $EMULATOR_PID