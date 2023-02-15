package com.example.rickandmorty.presentation.persons

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.core.BaseFragment
import com.example.rickandmorty.core.image.ImageLoader
import com.example.rickandmorty.databinding.ScreenPersonsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class PersonsFragment : BaseFragment(R.layout.screen_persons) {

    private val binding by viewBinding(ScreenPersonsBinding::bind)
    private val viewModel by viewModels<PersonsViewModel>()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val personAdapter by lazy { PersonAdapter(imageLoader) {} }

    override val title: String
        get() = getString(R.string.persons_title)

    override fun bind() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                personsPagingData.collectLatest {
                    personAdapter.submitData(it)
                }
            }
        }
    }

    override fun initViews(view: View) {
        with(binding) {
            persons.apply {
                adapter = personAdapter
                itemAnimator = null
            }
        }
    }
}