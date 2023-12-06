package viewmodel

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.cs308_00.R
import model.RequestDataInterface
import view.SeeAllFriends

class SeeAllFriendsRecViewAdapter(private val context : Context, private val data : MutableList<String>, private val viewModel : SeeAllFriendsViewModel)
    :RecyclerView.Adapter<SeeAllFriendsRecViewAdapter.ViewHolder>()
{

    private val mutableData = data.toMutableList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeAllFriendsRecViewAdapter.ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.see_all_friends_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeeAllFriendsRecViewAdapter.ViewHolder, position: Int) {
        Log.e("data", data.toString())
        val friendName = data[position]
        Log.e("tag", "Item is: $friendName")
        Log.e("tag", "Text View Text: " + holder.userName.text.toString())
        holder.userName.text = friendName.toString()

        holder.removeFriend.setOnClickListener {
            // Remove item from local data
            Log.e("mutable data: ", mutableData.toString())
            Log.e("position", position.toString())
            val itemToRemove = data[position]
            data.remove(itemToRemove)

            // Notify adapter about data change
            notifyItemRemoved(position)

            // Update ViewModel
            viewModel.removeFriend(friendName)
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView = itemView.findViewById(R.id.userImage)
        val userName: TextView = itemView.findViewById(R.id.removeUsername)
        val removeFriend : Button = itemView.findViewById(R.id.removeFriendBtn)

    }
}