package view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.Item

class RecyclerViewAdapter(private val itemList: List<Item>, private val type : Int) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
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
        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView.text = currentItem.text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
