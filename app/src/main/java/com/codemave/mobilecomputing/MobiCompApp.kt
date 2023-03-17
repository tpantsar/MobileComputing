package com.codemave.mobilecomputing

import android.app.Application

class MobiCompApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}