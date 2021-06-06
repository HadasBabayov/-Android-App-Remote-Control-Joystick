package com.example.androidappremotecontroljoystick.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.androidappremotecontroljoystick.R
import com.example.androidappremotecontroljoystick.viewModel.ViewModel

class MainActivity : AppCompatActivity() {

    private val vm = ViewModel()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val joystick : Joystick = findViewById(R.id.joystick)
        val simpleEditText = findViewById<View>(R.id.message) as TextView
        joystick.onChange = {a : Float, e : Float ->
            vm.setAileron(a)
            vm.setElevator(e)
        }
        val rudderBar : SeekBar = findViewById(R.id.rudder_bar)
        val throttleBar : SeekBar = findViewById(R.id.throttle_bar)
        vm.setSeekBars(rudderBar, throttleBar)
        val connectButton: Button = findViewById(R.id.connect_button)
        connectButton.setOnClickListener { connectFlightGear() }
        val disconnectButton: Button = findViewById(R.id.disconnect_button)
        disconnectButton.setOnClickListener { disconnect() }
    }

    @SuppressLint("SetTextI18n")
    fun connectFlightGear() {
        val simpleEditText = findViewById<View>(R.id.message) as TextView
        try {
            val ip = findViewById<EditText>(R.id.ip).text.toString()
            val port = findViewById<EditText>(R.id.port).text.toString().toInt()
            vm.connectFlightGear(ip, port)
            simpleEditText.setTextColor(Color.GREEN)
            simpleEditText.text = "Connected ! :)"
        } catch (e: Exception){
            e.printStackTrace()
            simpleEditText.setTextColor(Color.RED)
            simpleEditText.text = "Connection failed. Enter a valid IP and PORT."
        }
    }

    @SuppressLint("SetTextI18n")
    fun disconnect() {
        val simpleEditText = findViewById<View>(R.id.message) as TextView
        try {
            vm.disconnect()
            simpleEditText.setTextColor(Color.GREEN)
            simpleEditText.text = "disconnected !"
        } catch (e: Exception){
            e.printStackTrace()
            simpleEditText.setTextColor(Color.RED)
            simpleEditText.text = "disconnection failed."
        }
    }
}