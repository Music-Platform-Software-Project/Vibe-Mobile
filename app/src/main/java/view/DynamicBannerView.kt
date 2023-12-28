import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

class DynamicBannerView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private var phase = 0f
    private val amplitude = 20f // Adjust as needed
    private val frequency = 0.03f // Adjust as needed
    private var isLoading = true
    private var waveProperties = WaveProperties(0f, 0f, 0f)

    init {
        paint.color = Color.RED // Set wave color
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val path = Path()
        path.reset()

        for (x in 0 until width.toInt()) {
            val y = amplitude * sin(frequency * x + phase) + height / 2
            if (x == 0) {
                path.moveTo(x.toFloat(), y.toFloat())
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }

        canvas.drawPath(path, paint)

        // Update the phase to animate the wave
        phase += 0.1f

        if (!isLoading) {
            drawWave(canvas)
        }
    }

    fun setWaveProperties(energy: Float, instrumentalness: Float, tempo: Float) {
        waveProperties = WaveProperties(energy, instrumentalness, tempo)
        isLoading = false
        invalidate()
    }

    private fun drawWave(canvas: Canvas) {
        val energy = waveProperties.energy
        val instrumentalness = waveProperties.instrumentalness
        val tempo = waveProperties.tempo

        // Determine color based on energy
        val gradientColors = when {
            energy > 0.66 -> intArrayOf(Color.RED, Color.YELLOW, Color.MAGENTA)
            energy > 0.33 -> intArrayOf(Color.RED, Color.BLUE, Color.GRAY)
            else -> intArrayOf(Color.GRAY, Color.GREEN, Color.GRAY)
        }

        val gradient = LinearGradient(0f, 0f, canvas.width.toFloat(), 0f, gradientColors, null, Shader.TileMode.CLAMP)
        paint.shader = gradient
        paint.strokeWidth = 5f // Thicker wave

        // Dynamic wave properties
        val maxAmplitude = 40 * instrumentalness // You can adjust the multiplier for the amplitude
        val baseFrequency = 0.01 + (tempo - 120) * 0.0005 // Adjust frequency based on tempo

        val path = Path()
        path.reset()

        for (x in 0 until canvas.width) {
            val y = maxAmplitude * sin(x * baseFrequency) + canvas.height / 2
            if (x == 0) {
                path.moveTo(x.toFloat(), y.toFloat())
            } else {
                path.lineTo(x.toFloat(), y.toFloat())
            }
        }

        canvas.drawPath(path, paint)
    }

    private data class WaveProperties(val energy: Float, val instrumentalness: Float, val tempo: Float)
}
