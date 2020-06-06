package com.azazel.ipwig

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azazel.ipwig.data.ConnectionInfo

/**
 * An adapter class to present the available connections.
 */
class ConnectionAdapter : RecyclerView.Adapter<ConnectionAdapter.ViewHolder>() {

    private val netList: MutableList<ConnectionInfo> = mutableListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val view = layoutInflater.inflate(
            R.layout.network_list_item,
            viewGroup, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return netList.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textViewName.text = netList[position].name
        viewHolder.textViewType.text = transportString(netList[position], viewHolder.itemView.context)
        viewHolder.textViewIpv4.text = netList[position].ipv4
        viewHolder.textViewIpv6.text = netList[position].ipv6
    }

    /**
     * Adds a network info to the list and shows it in the list view.
     * @param networkInfo The network info.
     */
    fun add(networkInfo: ConnectionInfo) {
        netList.add(networkInfo)
        notifyItemInserted(netList.lastIndex)
    }

    /**
     * Clears all the connections.
     */
    fun clear() {
        netList.clear()
        notifyDataSetChanged()
    }

    /**
     * The view holder class.
     *
     * @param itemView The item view.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewType: TextView = itemView.findViewById(R.id.textViewType)
        val textViewIpv4: TextView = itemView.findViewById(R.id.textViewIpV4)
        val textViewIpv6: TextView = itemView.findViewById(R.id.textViewIpV6)
    }

    /**
     * Builds the transport string.
     *
     * @param connectionInfo The connection info.
     * @return The transport capabilities as string.
     */
    private fun transportString(connectionInfo: ConnectionInfo, context: Context): String {
        val builder = StringBuilder()

        if (connectionInfo.hasData) {
            builder.append(context.getString(R.string.transport_cellular))
        }
        if (connectionInfo.hasWifi) {
            if (connectionInfo.hasData) {
                builder.append(context.getString(R.string.transport_separator))
            }
            builder.append(context.getString(R.string.transport_wifi))
        }

        return builder.toString()
    }
}