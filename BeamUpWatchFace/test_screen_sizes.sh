#!/bin/bash

# Test watch face on different simulated screen sizes
echo "Testing Beam Up watch face on different screen sizes..."

# ADB path
ADB="~/Library/Android/sdk/platform-tools/~/Library/Android/sdk/platform-tools/adb"

# Create screenshots directory
mkdir -p screenshots/screen_tests

# Original size (for reference)
echo "Taking screenshot at original size (454x454)..."
~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screenshot.png
~/Library/Android/sdk/platform-tools/adb pull /sdcard/screenshot.png screenshots/screen_tests/454x454_original.png
echo "Screenshot saved: 454x454_original.png"

# Test different common Wear OS screen sizes
declare -a SIZES=(
    "320x320"  # Small round watches
    "384x384"  # Medium round watches (Pixel Watch)
    "280x280"  # Small square watches
    "340x340"  # Medium square watches
)

for size in "${SIZES[@]}"; do
    echo ""
    echo "Testing size: $size"
    
    # Change the display size
    ~/Library/Android/sdk/platform-tools/adb shell wm size $size
    
    # Wait for the display to adjust
    sleep 3
    
    # Take screenshot
    ~/Library/Android/sdk/platform-tools/adb shell screencap -p /sdcard/screenshot.png
    ~/Library/Android/sdk/platform-tools/adb pull /sdcard/screenshot.png screenshots/screen_tests/${size}_scaled.png
    
    echo "Screenshot saved: ${size}_scaled.png"
done

# Restore original size
echo ""
echo "Restoring original size..."
~/Library/Android/sdk/platform-tools/adb shell wm size 454x454
~/Library/Android/sdk/platform-tools/adb shell wm size reset

echo ""
echo "Testing complete! Check screenshots/screen_tests/ for results."
echo ""
echo "Screenshots taken:"
ls -la screenshots/screen_tests/

# Display analysis
echo ""
echo "Analysis:"
echo "- 454x454: Original design (100% scale)"
echo "- 384x384: Medium watches like Pixel Watch (~85% scale)"
echo "- 320x320: Smaller round watches (~71% scale)"
echo "- 340x340: Medium square watches (~75% scale)"
echo "- 280x280: Small square watches (~62% scale)"