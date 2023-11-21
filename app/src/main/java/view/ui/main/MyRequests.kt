package view.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import viewmodel.IncomingRequestsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyRequests.newInstance] factory method to
 * create an instance of this fragment.
 */

lateinit var requestAdapter: MyRequestAdapter
lateinit var requestRecyclerView: RecyclerView
class MyRequests : Fragment() {
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

        viewModel = ViewModelProvider(this@MyRequests).get(IncomingRequestsViewModel::class.java)
        viewModel.setContext(this)
        val view = inflater.inflate(R.layout.fragment_my_requests, container, false)

        requestRecyclerView = view.findViewById(R.id.MyRequestsRecView)
        requestRecyclerView.layoutManager = LinearLayoutManager(context)


        Log.e("mrb", "burdayim")
        viewModel.getRequests2()

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
         * @return A new instance of fragment MyRequests.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyRequests().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        fun applyChanges(invitationList: List<RequestDataInterface.sentFriendRequests>) {
            requestAdapter = MyRequestAdapter(invitationList)
            requestRecyclerView.adapter = requestAdapter
        }
    }
}