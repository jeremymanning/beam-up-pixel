# Multi-Device Support in Watch Face Format

## Current Status

The Watch Face Format (WFF) doesn't support percentage-based units directly. Instead, it uses a different approach for multi-device compatibility.

## WFF Multi-Device Strategy

1. **Default Size**: The watch face is designed at 450x450 pixels
2. **Automatic Scaling**: WFF automatically scales the watch face to fit different screen sizes
3. **Aspect Ratio**: The framework maintains aspect ratios during scaling

## Current Implementation

The watch face uses absolute positioning optimized for 450x450 screens, which WFF scales automatically:

- Time digits positioned at specific x,y coordinates
- Font sizes set in pixels (117px for time)
- Beams sized and positioned in pixels
- Progress bar at fixed position

## How WFF Handles Different Screens

### Round Screens
- **Smaller (320x320)**: Scaled down by 71%
- **Medium (384x384)**: Scaled down by 85%
- **Larger (454x454)**: Scaled up by 101%

### Square Screens
WFF centers the content and may crop circular designs at corners.

## Best Practices for WFF

1. **Design at Standard Size**: Use 450x450 as the base
2. **Center Important Content**: Keep critical elements away from edges
3. **Test Scaling**: Verify appearance on different sized emulators
4. **Avoid Edge Elements**: Elements near edges may be cropped on smaller screens

## Future Enhancements

To improve multi-device support:

1. Use `<Variant>` elements for different screen sizes (when WFF adds this feature)
2. Consider creating separate layouts for significantly different sizes
3. Use expression-based sizing tied to screen dimensions (future WFF feature)

## Current Limitations

- No direct percentage support in WFF 1.0
- No viewport units (vw, vh) equivalent
- Limited dynamic layout capabilities
- Scaling is proportional, not responsive

The current implementation works well across devices through WFF's automatic scaling, though true responsive design awaits future WFF enhancements.