package com.example.qualiwatch

import android.app.Application
import com.example.qualiwatch.data.AppContainer
import com.example.qualiwatch.data.DefaultAppContainer


class QualiwatchApplication : Application() {
    companion object {
        lateinit var appContainer: AppContainer
    }

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(this)
    }
}