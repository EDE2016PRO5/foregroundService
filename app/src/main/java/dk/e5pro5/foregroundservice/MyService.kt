package dk.e5pro5.foregroundservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MyService : Service(), SensorEventListener {


    private var mSensorManager: SensorManager? = null
    private var mStepDetector: Sensor? =null
    private var mStepsDBHelper: StepsDBHelper? = null
    private val myBinder = MyLocalBinder()
    private var counter=0
    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if(mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            mStepDetector= mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            mSensorManager!!.registerListener(this, mStepDetector,
                SensorManager.SENSOR_DELAY_NORMAL)
            mStepsDBHelper = StepsDBHelper(this)
        }
        else{
            Toast.makeText(this, "No Step Detector sensor was found!",
                Toast.LENGTH_LONG).show()
        }

    }

    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }
    fun getSteps(): Int{
        return counter
    }
    /*
    fun getCurrentTime(): String {
        val dateformat = SimpleDateFormat("HH:mm:ss MM/dd/yyyy",
            Locale.US)
        return dateformat.format(Date())
    }
    */
    inner class MyLocalBinder : Binder() {
        fun getService() : MyService {
            return this@MyService
        }
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        toast("Service started.")

        return Service.START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        toast("Service destroyed.")
        stopForeground(true)
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        counter+= 1
        //txtWalk.text = counter.toString()
        mStepsDBHelper!!.createStepsEntry()
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    // Extension function to show toast message
    fun Context.toast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}