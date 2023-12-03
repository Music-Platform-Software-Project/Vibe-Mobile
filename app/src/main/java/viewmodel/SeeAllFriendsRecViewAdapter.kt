package viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.cs308_00.R
import model.RequestDataInterface

class SeeAllFriendsRecViewAdapter(private val context : Context, private val data : List<String>)
    :RecyclerView.Adapter<SeeAllFriendsRecViewAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeAllFriendsRecViewAdapter.ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.friends_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeeAllFriendsRecViewAdapter.ViewHolder, position: Int) {
        val friendName = data[position]
        Log.e("tag", "Item is: $friendName")
        Log.e("tag", "Text View Text: " + holder.userName.text.toString())
        holder.userName.text = friendName.toString()

    }

    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView = itemView.findViewById(R.id.userImage)
        val userName: TextView = itemView.findViewById(R.id.userName)

    }
}