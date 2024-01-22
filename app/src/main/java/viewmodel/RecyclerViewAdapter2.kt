package viewmodel

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.RequestDataInterface
import view.DetailedPlaylist
import view.DetailedTrack

class RecyclerViewAdapter2(private val itemList: List<RequestDataInterface.RecViewHolder>, private val type : Int) : RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val item : LinearLayout = itemView.findViewById(R.id.playlistItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflateLayout = 0
        if (type == 1) {
            inflateLayout = R.layout.dashboard_playlists_item
        } else if (type == 2){
            inflateLayout = R.layout.dashboard_artists_item
        }
        else{
            inflateLayout = R.layout.dashboard_tracks_item
        }
        val itemView = LayoutInflater.from(parent.context).inflate(inflateLayout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val currentItem = itemList[position]
        Log.e("rec adap: ", currentItem.name)
        if (currentItem.name != "Liked Songs"){
            holder.imageView.setImageResource(R.drawable.vibe_logo_2)
            holder.textView.text = currentItem.name
        }


        holder.item.setOnClickListener {

            if(type ==1 ){ //when clicked on playlist
                val intent = Intent(holder.itemView.context, DetailedTrack::class.java)
                intent.putExtra("id", currentItem.id)
                // You can also pass data to the new activity using putExtra if needed
                // intent.putExtra("key", value)
                intent.putExtra("from", "dashboard")
                holder.itemView.context.startActivity(intent)
            }
            else if(type == 2) { //when clicked on fav artist
                val intent = Intent(holder.itemView.context, DetailedTrack::class.java)
                intent.putExtra("id", currentItem.id)
                // You can also pass data to the new activity using putExtra if needed
                // intent.putExtra("key", value)
                intent.putExtra("from", "dashboard")
                holder.itemView.context.startActivity(intent)
            }
            else if(type == 3){ //when clicked on fav tracks

            }

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}
