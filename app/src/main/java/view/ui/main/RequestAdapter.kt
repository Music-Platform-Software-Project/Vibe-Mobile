package view.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import network.APIRequest
import network.constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// RequestAdapter.kt
class RequestAdapter(private var requests: List<RequestDataInterface.friendRequests>) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in the item layout
        // Example: val textView: TextView = itemView.findViewById(R.id.textView)
        val username: TextView = itemView.findViewById(R.id.incomingUsername)
        val acceptButton: Button = itemView.findViewById<Button>(R.id.acceptButton)
        val rejectButton: Button = itemView.findViewById<Button>(R.id.rejectButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and return the ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to views in the item layout
        // Example: holder.textView.text = requests[position]
        holder.username.text = requests[position].sender
        holder.acceptButton.setOnClickListener {
            acceptRequest(requests[position].id)
        }

        holder.rejectButton.setOnClickListener {
            rejectRequest(requests[position].id)
        }
    }



    override fun getItemCount(): Int {
        return requests.size
    }

    private fun acceptRequest(id: String) {
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.acceptRequestPayload(id)
            val retrofitData = retrofitBuilder.acceptRequest(token, request)
            Log.e("accepting invitations", "going...")

            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("accepting invitations:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("accepting invitations: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        // Parse the responseBody as needed or handle the string response
                        if (responseBody != null) {
                            Log.e("accepting", "invitation list changed")
                        }
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("accepting error: ", "HTTP ${response.code()}: $errorBody")

                        } catch (e: Exception) {
                            Log.e("accepting error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("accepting error: ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }

        val updatedList = requests.filterNot { it.id == id }
        requests = updatedList
        notifyDataSetChanged()

    }

    private fun rejectRequest(id: String) {
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.acceptRequestPayload(id)
            val retrofitData = retrofitBuilder.rejectRequest(token, request)
            Log.e("rejecting invitations", "going...")

            retrofitData.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("rejecting invitations:", "rejecting body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("rejecting invitations: ",
                            (responseBody ?: "Response body is null").toString()
                        )
                        // Parse the responseBody as needed or handle the string response
                        if (responseBody != null) {
                            Log.e("rejecting", "invitation list changed")
                        }
                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("rejecting error: ", "HTTP ${response.code()}: $errorBody")

                        } catch (e: Exception) {
                            Log.e("rejecting error: ", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("rejecting error: ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
        val updatedList = requests.filterNot { it.id == id }
        requests = updatedList
        notifyDataSetChanged()
    }
}
