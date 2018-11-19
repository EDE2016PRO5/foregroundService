package dk.e5pro5.foregroundservice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.content.ServiceConnection
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView

class MainActivity : AppCompatActivity(){

    var myService: MyService? = null
    var isBound = false

    lateinit var mStepCountList: ArrayList<DateStepsModel>
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder) {
            val binder = service as MyService.MyLocalBinder
            myService = binder.getService()
            isBound = true
            getBackgroundNotification(applicationContext, myService).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }
    fun showTime() {
        val currentSteps=myService?.getSteps()
        toast(currentSteps.toString())
        //val currentTime = myService?.getCurrentTime()
        //toast(currentTime.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceClass = MyService::class.java
        val serviceIntent = Intent(applicationContext, serviceClass)
        // If the service is not running then start it
        if (!isServiceRunning(serviceClass)) {
            // Start the service
            startService(serviceIntent)
            bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE)
        } else {
            toast("Service already running.")
            bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE)
        }

        getDataForList()
        //val myAdapter = ListAdapter(this,0,mStepCountList)
        val myAdapter=ListAdapter(this,0,mStepCountList)
        //Set adapter of the ListView
        main_list_view.adapter=myAdapter

        button_start.setOnClickListener{
            if (!isServiceRunning(serviceClass)) {
                // Start the service
                startService(serviceIntent)
                bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE)
            } else {
                toast("Service already running.")
                bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE)
            }
        }

        // Button to stop the service
        button_stop.setOnClickListener{
            //Unbind Service
            try {
                unbindService(myConnection)
            } catch (e: IllegalArgumentException) {
                Log.w("MainActivity", "Error Unbinding Service.")
            }
            // If the service is running then stop it
            if (isServiceRunning(serviceClass)) {
                // Stop the service
                stopService(serviceIntent)
                toast("Service stopped.")
            } else {
                toast("Service already stopped.")
            }
        }
        // Get the service status
        button_stats.setOnClickListener{
            if (isServiceRunning(serviceClass)) {
                toast("Service is running.")
            } else {
                toast("Service is stopped.")
            }
            showTime()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState)
        //outState.putInt("STATE_STEPS", counter)
    }

    override fun onDestroy() {
        /*
        val serviceClass = MyService::class.java
        val serviceIntent = Intent(applicationContext, serviceClass)
        try {
            unbindService(myConnection)
        } catch (e: IllegalArgumentException) {
            Log.w("MainActivity", "Error Unbinding Service.")
        }
        if (isServiceRunning(MyService::class.java)) {
            stopService(serviceIntent)
        }
        */
/*
        if(isFinishing){
            mSensorManager!!.unregisterListener(this)
        }
*/
        super.onDestroy()
    }

    // Custom method to determine whether a service is running
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // Loop through the running services

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
    fun getDataForList() {
        val mStepsDBHelper = StepsDBHelper(this)
        mStepCountList = mStepsDBHelper.readStepsEntries()
    }

// Extension function to show toast message
fun Context.toast(message:String){
    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
}}
