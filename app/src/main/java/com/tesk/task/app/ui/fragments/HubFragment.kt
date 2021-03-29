package com.tesk.task.app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesk.task.R
import com.tesk.task.app.Application
import com.tesk.task.app.adapters.RepositoryAdapter
import com.tesk.task.app.viewmodels.AViewModel
import com.tesk.task.app.viewmodels.FactoryViewModel
import com.tesk.task.providers.api.impl.models.Hub
import com.tesk.task.providers.api.impl.models.User
import org.json.JSONException
import java.io.IOException
import java.lang.ref.WeakReference
import javax.inject.Inject

class HubFragment : Fragment() {

    lateinit var viewModuleFactory: FactoryViewModel
        @Inject set

    private val viewModel by lazy {
        viewModuleFactory.create(AViewModel.GetHubViewModel::class.java)
    }

    lateinit var user : User

    private lateinit var title : TextView
    private lateinit var adapter : RepositoryAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var loading : ProgressBar
    private lateinit var innerResultFrame : FrameLayout
    private lateinit var innerResultText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as Application).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.repository_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = view.findViewById(R.id.name)
        recyclerView = view.findViewById(R.id.recycler_view)
        loading = view.findViewById(R.id.loading)
        innerResultFrame = view.findViewById(R.id.inner_frame)
        innerResultText = view.findViewById(R.id.text)

        adapter = RepositoryAdapter(LayoutInflater.from(requireContext()))
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        title.text = String.format(getString(R.string.user_s), user.name)

        getData()
        subscribe()
    }

    private fun getData(){
        viewModel.getHubs(user)
    }

    private fun showInner(resource : Int){
        loading.visibility = View.GONE
        recyclerView.visibility = View.GONE
        innerResultFrame.visibility = View.VISIBLE
        innerResultText.setText(resource)
    }

    private fun showError(){
        showInner(R.string.internet_access_worn)
    }

    private fun showLoading(){
        loading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        innerResultFrame.visibility = View.GONE
    }

    private fun showHubs(list : List<Hub>){
        loading.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        innerResultFrame.visibility = View.GONE

        adapter.refresh(list)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun showOnEmpty(){
        showInner(R.string.user_have_not_repositories)
    }

    private fun subscribe(){
        viewModel.loadingLiveData.observe(this, { showLoading() })
        viewModel.isEmptyListLiveData.observe(this, { showOnEmpty() })
        viewModel.errorLiveData.observe(this, {
            when (it) {
                is JSONException -> {
                    Toast.makeText(requireContext(), getString(R.string.count_of_requests_get_a_higher_rate_limit), Toast.LENGTH_SHORT).show()
                }
                is IOException -> {
                    Toast.makeText(requireContext(), getString(R.string.internet_access_worn), Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.dataLiveData.observe(this, { showHubs(it) })
    }
}