package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.BluetoothClient
import java.io.File
import java.io.FileOutputStream

/**
 * Created by vitaly on 01.03.16.
 */

class ActivitySaber: Activity(), SensorEventListener{


    ///////
    var rotationMatrix : FloatArray? = null    //Матрица поворота
    var accelData : FloatArray? = null           //Данные с акселерометра
    var magnetData : FloatArray? = null       //Данные геомагнитного датчика
    var orientationData : FloatArray? = null //Матрица положения в пространстве

    ///////

    var isWork = false
    var dataTV: TextView? = null
    var lenearCoordTV: TextView? = null
    var angleCoordTV: TextView? = null

    val lenearCoords = doubleArrayOf(0.0, 0.0, 0.0)
    val lenearVelosity = doubleArrayOf(0.0, 0.0, 0.0)
    val angleCoords = doubleArrayOf(0.0, 0.0, 0.0)

    val tempGyro = doubleArrayOf(0.0, 0.0, 0.0)

    val gravity = doubleArrayOf(0.0, 0.0, 0.0)
    val linearAcceleration = doubleArrayOf(0.0, 0.0, 0.0)

    var accelTime = 0L
    var gyroTime = 0L

    var outStream : FileOutputStream? = null

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

        val mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);



        val root : String = Environment.getExternalStorageDirectory().toString()
        val file = File("$root/file.txt")

        outStream = FileOutputStream(file)

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


                    val alpha = 0.01f;

                    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                    linearAcceleration[0] = event.values[0] - gravity[0];
                    linearAcceleration[1] = event.values[1] - gravity[1];
                    linearAcceleration[2] = event.values[2] - gravity[2];


                    for ( i in 0..2 ){
                        if (Math.abs(linearAcceleration[i]) < 0.2) {
                            linearAcceleration[i] = 0.0
                        }
                    }

                    lenearVelosity[X] += deltaTime * linearAcceleration[X]
                    lenearVelosity[Y] += deltaTime * linearAcceleration[Y]
                    lenearVelosity[Z] += deltaTime * linearAcceleration[Z]


                    lenearCoords[X] += lenearVelosity[X] * deltaTime + linearAcceleration[X] * deltaTime * deltaTime / 2
                    lenearCoords[Y] += lenearVelosity[Y] * deltaTime + linearAcceleration[Y] * deltaTime * deltaTime / 2
                    lenearCoords[Z] += lenearVelosity[Z] * deltaTime + linearAcceleration[Z] * deltaTime * deltaTime / 2

                    accelTime = System.currentTimeMillis()

                    //outStream?.write("a:\t${linearAcceleration[0]}\t${linearAcceleration[1]}\t${linearAcceleration[2]}\t".toByteArray())
                    //outStream?.flush()

                }
                Sensor.TYPE_ORIENTATION -> {

                    if(gyroTime == 0L){
                        gyroTime = System.currentTimeMillis()
                    }

                    val deltaTime = (System.currentTimeMillis() - gyroTime) /1000.0


//                    if (Math.abs(event.values[X]) < 0.05 )
//                        tempGyro[0] = 0.0
//                    else
//                        tempGyro[0] = event.values[X] * 1.0
//
//                    if (Math.abs(event.values[Y]) < 0.05 )
//                        tempGyro[1] = 0.0
//                    else
//                        tempGyro[1] = event.values[Y] * 1.0
//
//                    if (Math.abs(event.values[Z]) < 0.05 )
//                        tempGyro[2] = 0.0
//                    else
//                        tempGyro[2] = event.values[Z] * 1.0


//                    angleCoords[X] += tempGyro[X] * deltaTime
//                    angleCoords[Y] += tempGyro[Y] * deltaTime
//                    angleCoords[Z] += tempGyro[Z] * deltaTime

                    angleCoords[X] = event.values[X] * 1.0
                    angleCoords[Y] = event.values[Y] * 1.0
                    angleCoords[Z] = event.values[Z] * 1.0

                    Log.i("TAG", "X: ${angleCoords[X]},\nY: ${angleCoords[Y]},\nZ: ${angleCoords[Z]}")

                    accelTime = System.currentTimeMillis()

                    //outStream?.write("g:\t${event.values[X]}\t${event.values[Y]}\t${event.values[Z]}\n".toByteArray())
                    //outStream?.flush()
                }
            }

            if(System.currentTimeMillis() - lastTime > 15) {
                lastTime = System.currentTimeMillis()
                lenearCoordTV?.text = "X: ${lenearCoords[X]},\nY: ${lenearCoords[Y]},\nZ: ${lenearCoords[Z]}"
                angleCoordTV?.text = "X: ${angleCoords[X]},\nY: ${angleCoords[Y]},\nZ: ${angleCoords[Z]}"

                val temp = "lX: ${lenearCoords[X]}, lY: ${lenearCoords[Y]}, lZ: ${lenearCoords[Z]}, aX: ${angleCoords[X]}, aY: ${angleCoords[Y]}, aZ: ${angleCoords[Z]}"

                if( client != null ){
                    client?.send(temp)
                }

            }

        }
    }

    override fun onStop() {
        super.onStop()
        outStream?.flush()
        outStream?.close()
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //throw UnsupportedOperationException()
    }

}