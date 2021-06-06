package com.example.androidappremotecontroljoystick.viewModel

import android.view.View
import android.widget.SeekBar
import com.example.androidappremotecontroljoystick.model.Model


class ViewModel {
    lateinit var ipInput: String
    lateinit var portInput: String
    val model: Model = Model()

    fun connectFlightGear(ip: String, port: Int) {
        model.connectFlightGear(ip, port)
    }

    fun setSeekBars(rudder : SeekBar, throttle : SeekBar) {
        model.setSeekBars(rudder,throttle)
    }

    fun setAileron(aileron : Float) {
        model.setAileron(aileron)
    }

    fun setElevator(elevator : Float) {
        model.setElevator(elevator)
    }

    fun disconnect() {
        model.disconnect()
    }

}