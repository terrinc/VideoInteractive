package com.example.videointeractive.di

import com.example.videointeractive.presentation.view.InteractiveActivity
import dagger.Component

@Component(modules = [InteractiveModule::class])
interface InteractiveComponent {
    fun inject(interactiveActivity: InteractiveActivity)
}
