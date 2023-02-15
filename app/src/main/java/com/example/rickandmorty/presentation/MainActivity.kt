package com.example.rickandmorty.presentation

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.core.BaseActivity
import com.example.rickandmorty.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun setTitle(text: String?) {
        binding.toolbarTitle.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initScreen()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        supportFragmentManager.addOnBackStackChangedListener {
            val isBackStackNotEmpty = supportFragmentManager.backStackEntryCount > 0
            supportActionBar?.setDisplayHomeAsUpEnabled(isBackStackNotEmpty)
        }
    }

    private fun initScreen() {
        router.newRootScreen(Screens.persons())
    }
}