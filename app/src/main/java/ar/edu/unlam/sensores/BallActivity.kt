package ar.edu.unlam.sensores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap

import android.content.Context

import android.graphics.BitmapFactory
import android.hardware.SensorManager


import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.hardware.Sensor

import android.view.WindowManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.Window


class BallActivity : AppCompatActivity(), SensorEventListener {

    var mCustomDrawableView: CustomDrawableView? = null

    var xAcceleration = 0f
    var xVelocity = 0.0f

    var yAcceleration = 0f
    var yVelocity = 0.0f
    var xmax = 0f
    var ymax = 0f
    private lateinit var sensorManager: SensorManager
    var frameTime = 0.666f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Get a reference to a SensorManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )

        mCustomDrawableView = CustomDrawableView(this)
        setContentView(mCustomDrawableView)

        //Calculate Boundry
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        xmax = displayMetrics.widthPixels.toFloat()- 50
        ymax = displayMetrics.heightPixels.toFloat() - 50
    }

    // This method will update the UI on new sensor events
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        run {
            if (sensorEvent.sensor.type == Sensor.TYPE_ORIENTATION) {
                //Set sensor values as acceleration
                yAcceleration = sensorEvent.values[1]
                xAcceleration = sensorEvent.values[2]
                updateBall()
            }
        }
    }

    private fun updateBall() {


        //Calculate new speed
        xVelocity += xAcceleration * frameTime
        yVelocity += yAcceleration * frameTime

        //Calc distance travelled in that time
        val xS: Float = xVelocity / 2 * frameTime
        val yS: Float = yVelocity / 2 * frameTime

        //Add to position negative due to sensor
        //readings being opposite to what we want!
        xPosition -= xS
        yPosition -= yS
        if (xPosition > xmax) {
            xPosition = xmax
        } else if (xPosition < 0) {
            xPosition = 0f
        }
        if (yPosition > ymax) {
            yPosition = ymax
        } else if (yPosition < 0) {
            yPosition = 0f
        }
    }

    override fun onAccuracyChanged(arg0: Sensor?, arg1: Int) {

    }

    override fun onResume() {
        super.onResume()
        if (isSensorManagerInitialized()) {
            sensorManager.registerListener(
                this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onStop() {
        if (isSensorManagerInitialized()) {
            sensorManager.unregisterListener(this)
        }

        super.onStop()
    }

    private fun isSensorManagerInitialized() = this::sensorManager.isInitialized

    companion object{
        var xPosition = 0f
        var yPosition = 0f
    }

    class CustomDrawableView(context: Context?) :
        View(context) {
        private var mBitmap: Bitmap
        private var mWood: Bitmap

        init {
            val ball = BitmapFactory.decodeResource(resources, R.drawable.ball)
            val dstWidth = 150
            val dstHeight = 150
            mBitmap = Bitmap.createScaledBitmap(ball, dstWidth, dstHeight, true)
            mWood = BitmapFactory.decodeResource(resources, R.drawable.wood)
        }

        override fun onDraw(canvas: Canvas) {
            val bitmap: Bitmap = mBitmap
            canvas.drawBitmap(mWood, 0f, 0f, null)
            canvas.drawBitmap(bitmap, xPosition, yPosition, null)
            invalidate()
        }
    }
}