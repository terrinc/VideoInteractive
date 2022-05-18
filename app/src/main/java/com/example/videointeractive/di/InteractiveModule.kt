package com.example.videointeractive.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class InteractiveModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context
}
