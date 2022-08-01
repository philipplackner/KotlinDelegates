package com.plcoding.kotlindelegates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.plcoding.kotlindelegates.ui.theme.KotlinDelegatesTheme
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity(),
        AnalyticsLogger by AnalyticsLoggerImpl(),
        DeepLinkHandler by DeepLinkHandlerImpl()
{
    private val obj by MyLazy {
        println("Hello world")
        3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner(this)
        println(obj)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeepLink(this, intent)
    }
}

class MyLazy<out T: Any>(
    private val initialize: () -> T
) {
    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if(value == null) {
            value = initialize()
            value!!
        } else value!!
    }
}

interface DeepLinkHandler {
    fun handleDeepLink(activity: Activity, intent: Intent?)
}

class DeepLinkHandlerImpl: DeepLinkHandler {
    override fun handleDeepLink(activity: Activity, intent: Intent?) {
        // Parse and handle the intent here
    }
}

interface AnalyticsLogger {
    fun registerLifecycleOwner(owner: LifecycleOwner)
}

class AnalyticsLoggerImpl: AnalyticsLogger, LifecycleEventObserver {
    override fun registerLifecycleOwner(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event) {
            Lifecycle.Event.ON_RESUME -> println("User entered the screen")
            Lifecycle.Event.ON_PAUSE -> println("User left the screen")
            else -> Unit
        }
    }
}