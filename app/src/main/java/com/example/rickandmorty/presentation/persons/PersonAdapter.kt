package com.example.rickandmorty.presentation.persons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.core.image.ImageLoader
import com.example.rickandmorty.databinding.ItemPersonBinding
import com.example.rickandmorty.domain.entities.Person

class PersonAdapter(
    private val imageLoader: ImageLoader,
    private val details: (Int) -> Unit
) :
    PagingDataAdapter<Person, PersonViewHolder>(object : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
            oldItem == newItem
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false),
            imageLoader
        ) {
            getItem(it)?.id?.let { personId ->
                details(personId)
            }
        }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

class PersonViewHolder(
    view: View,
    private val imageLoader: ImageLoader,
    private val details: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val binding by viewBinding(ItemPersonBinding::bind)

    init {
        binding.root.setOnClickListener { details(bindingAdapterPosition) }
    }

    fun bind(item: Person) = with(binding) {
        name.isVisible = !item.name.isNullOrEmpty()
        statusBlock.isVisible = !item.status.isNullOrEmpty()
        locationLabel.isVisible = !item.location?.name.isNullOrEmpty()
        originLabel.isVisible = !item.origin?.name.isNullOrEmpty()

        name.text = item.name
        status.text = item.status
        location.text = item.location?.name
        origin.text = item.origin?.name

        iconStatus.setImageResource(
            if (item.isAlive()) R.drawable.bg_circle_status_green
            else R.drawable.bg_circle_status_red
        )

        imageLoader.displayImage(item.image, R.drawable.ic_image_placeholder, image) {
            imageLoading.isVisible = it
        }
    }
}