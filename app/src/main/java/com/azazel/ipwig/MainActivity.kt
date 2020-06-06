package com.azazel.ipwig

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.azazel.ipwig.data.ConnectionInfoProvider

private const val TAG = "MainActivity"

/**
 * The application main screen, which shows a list of connected networks.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectionAdapter: ConnectionAdapter
    private lateinit var networkCallback: OnNetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the app bar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setTitle(R.string.connected_networks)

        // Recycler view & adapter references
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        connectionAdapter = ConnectionAdapter()
        recyclerView.adapter = connectionAdapter

        // Initialize the recycler view with information regarding the connected networks
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        // We can take advantage of the activity being created to register a Network Listener
        networkCallback = OnNetworkCallback(this)
        connectivityManager.registerNetworkCallback(networkCallback.request, networkCallback)
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")

        // Find the connections and add them to the adapter.
        ConnectionInfoProvider().findConnections(this).forEach {
            connectionAdapter.add(it)
        }
    }

    override fun onPause() {
        // We need to clear the connections
        connectionAdapter.clear()
        Log.i(TAG, "onPause()")
        super.onPause()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        // Here we lost the network callback
        connectivityManager.unregisterNetworkCallback(networkCallback)
        super.onDestroy()
    }
}

