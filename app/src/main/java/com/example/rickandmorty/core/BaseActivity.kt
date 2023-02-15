package com.example.rickandmorty.core

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.rickandmorty.R
import com.example.rickandmorty.core.error.Failure
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    lateinit var activityName: String

    private val navigator: Navigator by lazy {
        HideableSupportAppNavigator(this, R.id.container)
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    abstract fun setTitle(text: String?)

    override fun attachBaseContext(newBase: Context) {
        val newOverride = Configuration(newBase.resources?.configuration)
        newOverride.fontScale = 1.0f
        val fontContext = newBase.createConfigurationContext(newOverride)
        super.attachBaseContext(fontContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityName = this.localClassName
        Timber.d("Base * OnCreate * $activityName")
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onSupportNavigateUp(): Boolean {
        router.exit()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? OnBackPressed)?.onBackPressed()?.not()?.let {
            if (it) {
                router.exit()
            }
        } ?: router.exit()
    }

    fun showSystemMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    open fun handleFailure(failure: Failure?) {
        failure?.let {
            if (it.message().isNotBlank()) {
                showSystemMessage(it.message())
                Timber.d("Activity Failure  ${it.message()}")
            }
        }
    }

    fun fadeInProgress() {
        this.findViewById<ViewGroup>(R.id.spinner)?.isVisible = true
    }

    fun fadeOutProgress() {
        this.findViewById<ViewGroup>(R.id.spinner)?.isVisible = false
    }

    fun <L : LiveData<Boolean>> loading(
        liveData: L,
        body: (Boolean) -> Unit = {
            if (it) {
                fadeInProgress()
            } else {
                fadeOutProgress()
            }
        }
    ) = liveData.observe(this, Observer(body))
}