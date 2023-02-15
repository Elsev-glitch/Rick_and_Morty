package com.example.rickandmorty.presentation.person_description

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.core.ARG_PERSON_ID
import com.example.rickandmorty.core.BaseFragment
import com.example.rickandmorty.core.x.failure
import com.example.rickandmorty.databinding.ScreenPersonDescriptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDescriptionFragment : BaseFragment(R.layout.screen_person_description) {

    private val binding by viewBinding(ScreenPersonDescriptionBinding::bind)
    private val viewModel by viewModels<PersonDescriptionViewModel>()

    companion object {
        fun newInstance(peronId: Int): PersonDescriptionFragment =
            PersonDescriptionFragment().apply {
                arguments = bundleOf(ARG_PERSON_ID to peronId)
            }
    }

    override fun bind() {
        with(viewModel) {
            observeNullable(person) { person ->

            }
            loading(loading)
            failure(failure) { handleFailure(it) }
        }
    }

    override fun initViews(view: View) {
        with(binding) {

        }
    }
}