package net.nikonorov.lazerninja.ui

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import net.nikonorov.lazerninja.App
import net.nikonorov.lazerninja.R
import net.nikonorov.lazerninja.logic.BluetoothClient
import org.json.JSONObject

/**
 * Created by vitaly on 01.03.16.
 */

class ActivitySaber: Activity(), SensorEventListener{


    ///////

    var rotationMatrix : FloatArray? = null    //Матрица поворота
    var accelData : FloatArray? = null           //Данные с акселерометра
    var magnetData : FloatArray? = null       //Данные геомагнитного датчика
    var orientationData  = FloatArray(3) //Матрица положения в пространстве

    var gravity  = FloatArray(3) //Матрица положения в пространстве

    var lastAlpha : Double = 0.0
    var lastBetta : Double = 0.0

    ///////

    var isWork = false
    var dataTV: TextView? = null
    var lenearCoordTV: TextView? = null
    var angleCoordTV: TextView? = null


    var mSensorManager : SensorManager? = null

    var client : BluetoothClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
        mSensorManager?.registerListener(this,
                mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

//        mSensorManager?.registerListener(this,
//                mSensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
//                SensorManager.SENSOR_DELAY_GAME);

        saber.setOnClickListener {
            isWork = !isWork
        }

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

        if(isWork) {

            if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                var quaternions = FloatArray(4);
                SensorManager.getQuaternionFromVector(quaternions, event?.values);
                Log.i("TAG", "${quaternions[0]}, ${quaternions[1]}, ${quaternions[2]}, ${quaternions[3]}");

                val msg = "{\"x\":  ${quaternions[0]}, \"y\": ${quaternions[1]}, \"z\": ${quaternions[2]}, \"w\": ${quaternions[3]}}"

                (application as App).client?.send(msg)

            } else {

                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {

                    var x = event!!.values[0]
                    var y = event!!.values[1]
                    var z = event!!.values[2]


                    if (x == 0.0f){
                        x = 0.0000000001f
                    }

                    if (y == 0.0f){
                        y = 0.0000000001f
                    }

                    if (z == 0.0f){
                        z = 0.0000000001f
                    }

                    var alpha = Math.abs(Math.acos(y / Math.sqrt( (y * y + x * x).toDouble() )) / Math.PI * 180)

                    if (x < 0.0){
                        alpha *= -1
                    }

                    if (alpha.isNaN()){
                        Log.i("nan", "$alpha")
                        alpha = 0.0
                    }

                    var alphaForSend = 0.0

                    if (Math.abs(alpha - lastAlpha) < 4.0) {
                        alphaForSend = lastAlpha
                    } else {
                        alphaForSend = alpha
                    }


                    var betta = Math.abs(Math.acos(y / Math.sqrt( (y * y + z * z).toDouble() )) / Math.PI * 180)

                    if (z > 0.0){
                        betta *= -1
                    }

                    if (betta.isNaN()){
                        Log.i("nan", "$betta")
                        betta = 0.0
                    }

                    var bettaForSend = 0.0

                    if (Math.abs(betta - lastBetta) < 4.0) {
                        bettaForSend = lastBetta
                    } else {
                        bettaForSend = betta
                    }

                    Log.i("orientation: ", "alpha: $alpha betta: $betta")

                    val msg = "{\"x\":  $bettaForSend, \"y\": $alphaForSend}"

                    (application as App).client?.send(msg)

                    lastAlpha = alpha
                    lastBetta = betta


                }


            }
            //        else {
            //            loadNewSensorData(event); // Получаем данные с датчика
            //            SensorManager.getRotationMatrix(rotationMatrix, null, accelData, magnetData) //Получаем матрицу поворота
            //            SensorManager.getOrientation(rotationMatrix, orientationData) //Получаем данные ориентации устройства в пространстве
            //        }
        }

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