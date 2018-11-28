package dk.e5pro5.foregroundservice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ActivityManager
import android.content.Context
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val serviceClass = MyService::class.java
        val serviceIntent = Intent(applicationContext, serviceClass)

        button_start.setOnClickListener{
            if (!isServiceRunning(serviceClass)) {
                // Start the service
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                    // Do something for OREO and above versions
                    startForegroundService(serviceIntent)
                } else{
                    // do something for phones running an SDK before OREO
                    startService(serviceIntent)
                }
            } else {
                toast("Service already running.")
            }
        }

        // Button to stop the service
        button_stop.setOnClickListener{
            // If the service is running then stop it
            if (isServiceRunning(serviceClass)) {
                // Stop the service
                stopService(serviceIntent)
                toast("Service stopped.")
            } else {
                toast("Service already stopped.")
            }
        }
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

    // Extension function to show toast message
    private fun Context.toast(message:String){
    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
}
}

