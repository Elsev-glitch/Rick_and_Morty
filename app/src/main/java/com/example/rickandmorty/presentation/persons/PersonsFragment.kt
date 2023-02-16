package com.example.rickandmorty.presentation.persons

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.rickandmorty.R
import com.example.rickandmorty.core.BaseFragment
import com.example.rickandmorty.core.image.ImageLoader
import com.example.rickandmorty.databinding.ScreenPersonsBinding
import com.example.rickandmorty.presentation.Screens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import javax.inject.Inject


@AndroidEntryPoint
class PersonsFragment : BaseFragment(R.layout.screen_persons) {

    private val binding by viewBinding(ScreenPersonsBinding::bind)
    private val viewModel by viewModels<PersonsViewModel>()

    @Inject
    lateinit var imageLoader: ImageLoader

    private val personAdapter by lazy {
        PersonAdapter(imageLoader) {
            router.navigateTo(Screens.personDescription(it))
        }
    }

    private val menuHost by lazy { requireActivity() as MenuHost }

    override val title: String
        get() = getString(R.string.persons_title)

    override fun bind() {
        with(viewModel) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                personsPagingData.collectLatest {
                    personAdapter.submitData(it)
                }
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                personAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }.collect {
                    when (it.refresh) {
                        is LoadState.Loading -> fadeInProgress()
                        is LoadState.NotLoading -> {
                            fadeOutProgress()
                            binding.emptyPlaceholder.isVisible = personAdapter.itemCount == 0
                        }
                        is LoadState.Error -> {
                            fadeOutProgress()
                            showSystemMessage(
                                (it.refresh as LoadState.Error).error.message
                                    ?: getString(R.string.error_connection)
                            )
                        }
                    }
                }
            }
            observeNullable(searchedPersonName) {
                menuHost.invalidateMenu()
            }
        }
    }

    override fun initViews(view: View) {
        initMenu()
        with(binding) {
            persons.apply {
                adapter = personAdapter
                itemAnimator = null
            }
        }
    }

    private fun initMenu() {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                val actionSearch = menu.findItem(R.id.action_search)
                val searchView = actionSearch?.actionView as SearchView
                searchView.queryHint = getString(R.string.enter_person_name)

                val searchTextField = searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
                searchTextField.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.white40))
                searchTextField.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                val searchCloseIcon : ImageView = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn) as ImageView
                searchCloseIcon.setImageResource(R.drawable.ic_close)

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.submitPersonName(query)
                        actionSearch.collapseActionView()
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.action_reset -> viewModel.submitPersonName(null)
                }
                return false
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                menu.findItem(R.id.action_reset).isVisible = !viewModel.searchedPersonName.value.isNullOrEmpty()
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}