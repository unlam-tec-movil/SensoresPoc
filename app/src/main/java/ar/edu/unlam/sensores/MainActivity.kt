package ar.edu.unlam.sensores

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.unlam.sensores.databinding.ActivityMainBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        readAccelerometer()
        readGravity()

        binding.ballGame.setOnClickListener {
            startActivity(Intent(this, BallActivity::class.java))
        }
    }


    private fun readAccelerometer() {
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        if (sensor != null) {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.run {
                        val value = DecimalFormat("##.######").format(this.values[0])
                        binding.accelerometerData.text = "accelerometer: $value"

                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }

            }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun readGravity() {
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        if (sensor != null) {
            sensorManager.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.run {
                        val value = DecimalFormat("##.######").format(this.values[0])
                        binding.gravityData.text = "gravity: $value"

                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }

            }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}

