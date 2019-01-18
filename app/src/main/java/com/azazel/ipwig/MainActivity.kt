package com.azazel.ipwig

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the app bar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setTitle(R.string.connected_networks)

        // Recycler view & adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val networksAdapter = NetworkAdapter()
        recyclerView.adapter = networksAdapter

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkList = cm.allNetworks.asList()
        networkList.forEach {
            val linkProperties = cm.getLinkProperties(it)
            val networkInfo = cm.getNetworkInfo(it)

            if (networkInfo.isConnected) {
                networksAdapter.add(networkInfo)
            }
        }

    }
}
