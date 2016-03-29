package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.BluetoothClient

/**
 * Created by vitaly on 01.03.16.
 */

class ActivitySaber: Activity(), SensorEventListener{

    var isWork = false
    var dataTV: TextView? = null
    var lenearCoordTV: TextView? = null
    var angleCoordTV: TextView? = null

    val lenearCoords = doubleArrayOf(0.0, 0.0, 0.0)
    val lenearVelosity = doubleArrayOf(0.0, 0.0, 0.0)
    val angleCoords = doubleArrayOf(0.0, 0.0, 0.0)

    val gravity = doubleArrayOf(0.0, 0.0, 0.0)
    val linearAcceleration = doubleArrayOf(0.0, 0.0, 0.0)

    var accelTime = 0L
    var gyroTime = 0L

    var lastTime : Long = 0

    val X = 0
    val Y = 1
    val Z = 2

    var client : BluetoothClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_saber)

        val saber = findViewById(R.id.saber)
        dataTV = findViewById(R.id.saber_tv) as TextView
        lenearCoordTV = findViewById(R.id.linear_coord_tv) as TextView
        angleCoordTV = findViewById(R.id.angle_coord_tv) as TextView

        val mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);

        saber.setOnClickListener {
            isWork = !isWork
        }


    }

    override fun onResume() {
        super.onResume()
        client = (application as App).client
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(isWork && event != null){

            when(event.sensor.type){
                Sensor.TYPE_ACCELEROMETER -> {

                    if(accelTime == 0L){
                        accelTime = System.currentTimeMillis()
                    }

                    val deltaTime = (System.currentTimeMillis() - accelTime) / 1000.0


                    val alpha = 0.8f;

                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                    linearAcceleration[0] = event.values[0] - gravity[0];
                    linearAcceleration[1] = event.values[1] - gravity[1];
                    linearAcceleration[2] = event.values[2] - gravity[2];


                    lenearVelosity[X] += deltaTime * linearAcceleration[X]
                    lenearVelosity[Y] += deltaTime * linearAcceleration[Y]
                    lenearVelosity[Z] += deltaTime * linearAcceleration[Z]


                    lenearCoords[X] += lenearVelosity[X] * deltaTime + linearAcceleration[X] * deltaTime * deltaTime / 2
                    lenearCoords[Y] += lenearVelosity[Y] * deltaTime + linearAcceleration[Y] * deltaTime * deltaTime / 2
                    lenearCoords[Z] += lenearVelosity[Z] * deltaTime + linearAcceleration[Z] * deltaTime * deltaTime / 2

                    accelTime = System.currentTimeMillis()
                }
                Sensor.TYPE_GYROSCOPE -> {

                    if(gyroTime == 0L){
                        gyroTime = System.currentTimeMillis()
                    }

                    val deltaTime = (System.currentTimeMillis() - gyroTime) /1000.0

                    angleCoords[X] += event.values[X] * deltaTime
                    angleCoords[Y] += event.values[Y] * deltaTime
                    angleCoords[Z] += event.values[Z] * deltaTime

                    accelTime = System.currentTimeMillis()
                }
            }

            if(System.currentTimeMillis() - lastTime > 2000) {
                lastTime = System.currentTimeMillis()
                lenearCoordTV?.text = "X: ${lenearCoords[X]}, Y: ${lenearCoords[Y]}, Z: ${lenearCoords[Z]}"
                angleCoordTV?.text = "X: ${angleCoords[X]}, Y: ${angleCoords[Y]}, Z: ${angleCoords[Z]}"

                val temp = "X: ${lenearCoords[X]}, Y: ${lenearCoords[Y]}, Z: ${lenearCoords[Z]}, X: ${angleCoords[X]}, Y: ${angleCoords[Y]}, Z: ${angleCoords[Z]}"

                if( client != null ){
                    client?.send(temp)
                }

            }

        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //throw UnsupportedOperationException()
    }

}