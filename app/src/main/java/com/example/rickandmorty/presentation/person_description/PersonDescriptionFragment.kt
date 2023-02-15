package com.example.rickandmorty.presentation.person_description

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.core.ARG_PERSON_ID
import com.example.rickandmorty.core.BaseFragment
import com.example.rickandmorty.core.image.ImageLoader
import com.example.rickandmorty.core.x.failure
import com.example.rickandmorty.databinding.ScreenPersonDescriptionBinding
import com.example.rickandmorty.domain.entities.Person
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun bind() {
        with(viewModel) {
            observeNullable(person) { person ->
                binding.name.text = person?.name
                binding.status.text = person?.status
                binding.gender.text = person?.gender
                binding.episodes.text = person?.episode?.size?.toString()
                binding.species.text = person?.species
                binding.location.text = person?.location?.name
                binding.origin.text = person?.origin?.name
                binding.created.text = person?.created

                binding.iconStatus.setImageResource(
                    if (person?.isAlive() == true) R.drawable.bg_circle_status_green
                    else R.drawable.bg_circle_status_red
                )

                imageLoader.displayImage(
                    person?.image,
                    R.drawable.ic_image_placeholder,
                    binding.image
                ) {
                    binding.imageLoading.isVisible = it
                }
            }
            loading(loading)
            failure(failure) { handleFailure(it) }
        }
    }

    override fun initViews(view: View) {
        with(binding) {

        }
    }

    private fun initComponents(person: Person?) {
        with(binding) {
            name.isVisible = !person?.name.isNullOrEmpty()
            imageBlock.isVisible = !person?.image.isNullOrEmpty()
            statusBlock.isVisible = !person?.status.isNullOrEmpty()
            gender.isVisible = !person?.gender.isNullOrEmpty()
            genderLabel.isVisible = !person?.gender.isNullOrEmpty()
            episodes.isVisible = person?.episode?.size?.let { it > 0 } == true
            episodesLabel.isVisible = person?.episode?.size?.let { it > 0 } == true
            species.isVisible = !person?.species.isNullOrEmpty()
            speciesLabel.isVisible = !person?.species.isNullOrEmpty()
            location.isVisible = !person?.location?.name.isNullOrEmpty()
            locationLabel.isVisible = !person?.location?.name.isNullOrEmpty()
            origin.isVisible = !person?.origin?.name.isNullOrEmpty()
            originLabel.isVisible = !person?.origin?.name.isNullOrEmpty()
            created.isVisible = !person?.created.isNullOrEmpty()
            createdLabel.isVisible = !person?.created.isNullOrEmpty()

            name.text = person?.name
            status.text = person?.status
            gender.text = person?.gender
            episodes.text = person?.episode?.size?.toString()
            species.text = person?.species
            location.text = person?.location?.name
            origin.text = person?.origin?.name
            created.text = person?.created

            iconStatus.setImageResource(
                if (person?.isAlive() == true) R.drawable.bg_circle_status_green
                else R.drawable.bg_circle_status_red
            )

            imageLoader.displayImage(
                person?.image,
                R.drawable.ic_image_placeholder,
                binding.image
            ) {
                imageLoading.isVisible = it
            }
        }
    }
}