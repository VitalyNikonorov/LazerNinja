package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import net.nikonorov.lazerninja.R

/**
 * Created by vitaly on 01.03.16.
 */

class ActivitySaber: Activity(), SensorEventListener{

    var isWork = false
    var dataTV: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_saber)

        val saber = findViewById(R.id.saber)
        dataTV = findViewById(R.id.saber_tv) as TextView

        val mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);

        saber.setOnClickListener {
            isWork = true
        }


    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(isWork && event != null){
            var axisX = event.values[0];
            var axisY = event.values[1];
            var axisZ = event.values[2];

            if(dataTV != null){
                (dataTV as TextView).text = "X: " + axisX.toString() + ", Y: " + axisY.toString() + ", Z: " + axisZ.toString();
            }
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //throw UnsupportedOperationException()
    }

}