package com.uzuu.customer.core

import android.app.Application
import com.uzuu.customer.data.session.SessionManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}