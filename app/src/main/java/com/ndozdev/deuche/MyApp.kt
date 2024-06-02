package com.ndozdev.deuche


import android.app.Application
import com.ndozdev.deuche.di.AppModule
import com.ndozdev.deuche.di.AppModuleImpl


class MyApp:Application() {
    companion object {
        lateinit var appModule: AppModule
    }
    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}
