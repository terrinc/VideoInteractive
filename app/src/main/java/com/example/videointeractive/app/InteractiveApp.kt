package com.example.videointeractive.app

import android.app.Application
import com.example.videointeractive.di.DaggerInteractiveComponent
import com.example.videointeractive.di.InteractiveComponent
import com.example.videointeractive.di.InteractiveModule

class InteractiveApp: Application() {
    lateinit var appComponent: InteractiveComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerInteractiveComponent
            .builder()
            .interactiveModule(InteractiveModule(context = this))
            .build()
    }
}
