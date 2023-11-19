package view.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import viewmodel.DashboardViewModel
import viewmodel.IncomingRequestsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IncomingRequests.newInstance] factory method to
 * create an instance of this fragment.
 */

lateinit var adapter: RequestAdapter
lateinit var recyclerView: RecyclerView
class IncomingRequests : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var viewModel: IncomingRequestsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this@IncomingRequests).get(IncomingRequestsViewModel::class.java)
        viewModel.setContext(this)
        val view = inflater.inflate(R.layout.fragment_incoming_requests, container, false)

        recyclerView = view.findViewById(R.id.incomingRequestsRecView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Example data for testing
        val request1 = RequestDataInterface.friendRequests("ege", "123")
        val request2 = RequestDataInterface.friendRequests("zeynep", "456")


        viewModel.getRequests()

        //val requestList = listOf(request1, request2)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncomingRequests.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IncomingRequests().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun applyChanges(invitationList: List<RequestDataInterface.friendRequests>) {
            adapter = RequestAdapter(invitationList)
            recyclerView.adapter = adapter
        }
    }
}


