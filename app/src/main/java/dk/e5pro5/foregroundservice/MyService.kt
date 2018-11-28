package dk.e5pro5.foregroundservice

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.IBinder
import android.widget.Toast

class MyService : Service(), SensorEventListener {
    private var mSensorManager: SensorManager? = null
    private var mStepDetector: Sensor? =null
    private var counter=0
    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            mStepDetector= mSensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            mSensorManager!!.registerListener(this, mStepDetector,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
        else{
            Toast.makeText(this, "No Step Detector sensor was found!",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        toast("Service started.")
        GetBackgroundNotification(applicationContext,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    // Extension function to show toast message
    private fun Context.toast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}