package com.example.androidappremotecontroljoystick.model

import android.annotation.SuppressLint
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.SeekBar
import android.widget.TextView
import java.io.IOException
import java.io.OutputStream
import java.net.Socket


class Model {
    private lateinit var socket: Socket
    private lateinit var outStream: OutputStream
    private lateinit var rudder: SeekBar
    private lateinit var throttle: SeekBar
    private var isConnected: Boolean = false

    @SuppressLint("SetTextI18n")
    fun connectFlightGear(ip: String, port: Int) {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        this.socket = Socket(ip,port)
        this.outStream= this.socket.getOutputStream()
        this.isConnected = true
    }

    fun setSeekBars(rudder : SeekBar, throttle : SeekBar) {
        this.rudder = rudder
        this.throttle = throttle

        this.rudder.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (isConnected) {
                    sendData(progress.toFloat(), "rudder")
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        this.throttle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (isConnected){
                    sendData(progress.toFloat(),"throttle")
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun setAileron(aileron : Float) {
        if(this.isConnected) {
            sendData(aileron, "aileron")
        }
    }

    fun setElevator(elevator : Float) {
        if (this.isConnected){
            sendData(elevator, "elevator")
        }
    }

    fun sendData(value : Float, data : String) {
        val t = Thread(Runnable {
            var command = ""
            if (data =="rudder") {
                command = "set /controls/flight/rudder ${value/1000} \r\n"
            } else if (data == "throttle") {
                command = "set /controls/engines/current-engine/throttle ${value/1000} \r\n"
            } else if (data == "aileron") {
                command = "set /controls/flight/aileron $value \r\n"
            } else if (data == "elevator") {
                command = "set /controls/flight/elevator $value \r\n"
            }
            val sendCommand = command.toByteArray(Charsets.UTF_8)
            try {
                outStream.write(sendCommand)
                outStream.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
        t.start()
        t.join()
    }

    fun disconnect() {
        this.outStream.close()
        this.socket.close()
        this.isConnected = false
    }
}