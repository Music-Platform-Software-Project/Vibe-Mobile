package viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
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
import view.my_room

class RoomImageAdapter(private val context: Context, private val data: List<RequestDataInterface.ImageData>)
    : RecyclerView.Adapter<RoomImageAdapter.ViewHolder>(){

    private lateinit var viewModel : ChangeImageViewModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomImageAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.images_rec_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RoomImageAdapter.ViewHolder, position: Int) {
        val imageItem = data[position]
        var base64 = constants.dummyBase64 //imageItem.image TODO

        val decodedBytesDummy = Base64.decode(base64, Base64.DEFAULT)
        val bitmapDummy = BitmapFactory.decodeByteArray(decodedBytesDummy, 0, decodedBytesDummy.size)
        holder.image.setImageBitmap(bitmapDummy)
        holder.imageName.text = imageItem.name

        holder.item.setOnClickListener {
            setImage(imageItem.id)
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.roomImageView)
        val imageName : TextView = itemView.findViewById(R.id.roomImageName)
        val item : LinearLayout = itemView.findViewById(R.id.imageItem)

    }



    private fun setImage(imageID : String){
        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(constants.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val retrofitBuilder = retrofit.create(APIRequest::class.java)

            val token = "Bearer " + constants.bearerToken
            val request = RequestDataInterface.SetRoomTrackRequest(imageID)
            val retrofitData = retrofitBuilder.setRoomImage(token, request)

            retrofitData.enqueue(object : Callback<RequestDataInterface.ImageData> {
                override fun onResponse(call: Call<RequestDataInterface.ImageData>, response: Response<RequestDataInterface.ImageData>) {
                    Log.e("setting image:", "retrieving body")
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.e("setting image",
                            (responseBody ?: "Response body is null").toString()
                        )
                        Log.e("setting image", "IMAGE SET")
                        context.startActivity(Intent(context, my_room::class.java))


                    }
                    else {
                        try {
                            var errorBody = response.errorBody()?.string()
                            errorBody =  errorBody!!.substringAfter(":").trim()
                            errorBody = errorBody.replace(Regex("[\"{}]"), "").trim()
                            Log.e("setting image error ", "HTTP ${response.code()}: $errorBody")

                        } catch (e: Exception) {
                            Log.e("setting image error", "Error parsing error response.")
                        }
                    }
                }

                override fun onFailure(call: Call<RequestDataInterface.ImageData>, t: Throwable) {
                    Log.e("setting image error ", t.toString())
                }
            })
        }
        catch (e: Exception) {
            Log.e("error", e.toString())
            // Handle the exception here (e.g. log it or display an error message)
        }
    }
}