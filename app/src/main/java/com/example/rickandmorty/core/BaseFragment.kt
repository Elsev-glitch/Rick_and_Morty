package com.example.rickandmorty.core

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.rickandmorty.R
import com.example.rickandmorty.core.error.Failure
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    @Inject
    lateinit var router: Router

    open val title: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bind()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    abstract fun bind()
    abstract fun initViews(view: View)

    fun setTitle(text: String?) {
        (activity as? BaseActivity)?.setTitle(text)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("=== OnResume: ${javaClass.simpleName} ===")
        if (title != null)
            (activity as? BaseActivity)?.setTitle(title)
    }

    override fun onDestroy() {
        super.onDestroy()
        fadeOutProgress()
    }

    fun showSystemMessage(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun hideKeyboard(view: View? = activity?.currentFocus) {
        view?.let {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    open fun handleFailure(failure: Failure?) {
        failure?.let {
            showSystemMessage(it.message())
            Timber.d("Fragment Failure ${it.message()}")
        }
    }

    fun fadeInProgress() {
        activity?.findViewById<ViewGroup>(R.id.spinner)?.isVisible = true
    }

    fun fadeOutProgress() {
        activity?.findViewById<ViewGroup>(R.id.spinner)?.isVisible = false
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
    ) = liveData.observe(viewLifecycleOwner, Observer(body))

    fun <T : Any, F : Flow<T>> observe(flow: F, body: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collect { body(it) } }
        }
    }

    fun <T : Any?, F : Flow<T?>> observeNullable(flow: F, body: (T?) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect { body(it) }
            }
        }
    }
}
