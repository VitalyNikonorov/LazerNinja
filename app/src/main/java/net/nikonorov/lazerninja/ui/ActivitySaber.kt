package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.BluetoothClient

/**
 * Created by vitaly on 01.03.16.
 */

class ActivitySaber: Activity(), SensorEventListener{


    ///////

    var rotationMatrix : FloatArray? = null    //Матрица поворота
    var accelData : FloatArray? = null           //Данные с акселерометра
    var magnetData : FloatArray? = null       //Данные геомагнитного датчика
    var orientationData  = FloatArray(3) //Матрица положения в пространстве

    ///////

    var isWork = false
    var dataTV: TextView? = null
    var lenearCoordTV: TextView? = null
    var angleCoordTV: TextView? = null
    

    var mSensorManager : SensorManager? = null

    var client : BluetoothClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_saber)


        rotationMatrix = FloatArray(16)
        accelData = FloatArray(3)
        magnetData = FloatArray(3)


        val saber = findViewById(R.id.saber)
        dataTV = findViewById(R.id.saber_tv) as TextView
        lenearCoordTV = findViewById(R.id.linear_coord_tv) as TextView
        angleCoordTV = findViewById(R.id.angle_coord_tv) as TextView

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

//        mSensorManager?.registerListener(this,
//                mSensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
//                SensorManager.SENSOR_DELAY_FASTEST);
//
//        mSensorManager?.registerListener(this,
//                mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager?.registerListener(this,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST);


    }


    fun loadNewSensorData(event : SensorEvent?) {
        val type = event?.sensor?.getType(); //Определяем тип датчика
        if (type == Sensor.TYPE_ACCELEROMETER) { //Если акселерометр
            accelData = event?.values?.clone();
        }

        if (type == Sensor.TYPE_MAGNETIC_FIELD) { //Если геомагнитный датчик
            magnetData = event?.values?.clone();
        }
    }

    override fun onResume() {
        super.onResume()
        client = (application as App).client
    }

    override fun onSensorChanged(event: SensorEvent?) {


        if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR){
            var quaternions = FloatArray(4);
            SensorManager.getQuaternionFromVector(quaternions, event?.values);
            Log.i("TAG", "${quaternions[0]}, ${quaternions[1]}, ${quaternions[2]}, ${quaternions[3]}");
        }
//        else {
//            loadNewSensorData(event); // Получаем данные с датчика
//            SensorManager.getRotationMatrix(rotationMatrix, null, accelData, magnetData) //Получаем матрицу поворота
//            SensorManager.getOrientation(rotationMatrix, orientationData) //Получаем данные ориентации устройства в пространстве
//        }

    }

    override fun onPause() {
        mSensorManager?.unregisterListener(this);
        super.onPause()

    }


    override fun onStop() {
        super.onStop()
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //throw UnsupportedOperationException()
    }

}