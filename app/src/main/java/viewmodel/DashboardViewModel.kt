package viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cs308_00.R
import model.Item
import network.RetrofitClient
import view.Dashboard
import view.RecyclerViewAdapter
import view.RegisterPage
import view.Settings
import view.my_room

class DashboardViewModel() : ViewModel() {

    private lateinit var ctx: Context
    private lateinit var retrofitClient: RetrofitClient


    fun setContext(ctx: Dashboard) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun setContext(ctx: my_room) {
        this.ctx = ctx
        retrofitClient = RetrofitClient(ctx)
    }

    fun switchToSettings(){
        val intent = Intent(ctx, Settings::class.java)
        ctx.startActivity(intent)
    }



    fun setRecyclerView(){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.playlists_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 3, GridLayoutManager.HORIZONTAL, false)

        val itemList = listOf(
            Item(R.drawable.vibe_logo_2, "Item 1"),
            Item(R.drawable.vibe_logo_2, "Item 2"),
            Item(R.drawable.vibe_logo_2, "Item 3"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Item 4"),
            Item(R.drawable.vibe_logo_2, "Item 5"),
            Item(R.drawable.vibe_logo_2, "Item 6"),

            Item(R.drawable.vibe_logo_2, "Item 7"),
            Item(R.drawable.vibe_logo_2, "Item 8"),
            Item(R.drawable.vibe_logo_2, "Item 9"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Item 10"),
            Item(R.drawable.vibe_logo_2, "Item 11"),
            Item(R.drawable.vibe_logo_2, "Item 12"),
        )

        val adapter = RecyclerViewAdapter(itemList, 1)
        recyclerView?.adapter = adapter


    }

    fun setRecyclerViewForArtists(){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.artists_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

        val itemList = listOf(
            Item(R.drawable.vibe_logo_2, "Artist 1"),
            Item(R.drawable.vibe_logo_2, "Artist 2"),
            Item(R.drawable.vibe_logo_2, "Artist 3"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Artist 4"),
            Item(R.drawable.vibe_logo_2, "Artist 5"),
            Item(R.drawable.vibe_logo_2, "Artist 6"),

            Item(R.drawable.vibe_logo_2, "Artist 7"),
            Item(R.drawable.vibe_logo_2, "Artist 8"),
            Item(R.drawable.vibe_logo_2, "Artist 9"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "Artist 10"),
            Item(R.drawable.vibe_logo_2, "Artist 11"),
            Item(R.drawable.vibe_logo_2, "Artist 12"),
        )

        val adapter = RecyclerViewAdapter(itemList, 2)
        recyclerView?.adapter = adapter

    }

    fun setRecyclerViewForTracks(){
        val recyclerView = (ctx as? Activity)?.findViewById<RecyclerView>(R.id.tracks_rec_view)
        recyclerView?.layoutManager =  GridLayoutManager(ctx, 2, GridLayoutManager.HORIZONTAL, false)

        val itemList = listOf(
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),

            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            // Add more items as needed
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
            Item(R.drawable.vibe_logo_2, "★ 5/5"),
        )

        val adapter = RecyclerViewAdapter(itemList, 3)
        recyclerView?.adapter = adapter


    }


}