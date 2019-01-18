package com.azazel.ipwig

import android.net.NetworkInfo
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class NetworkAdapter : RecyclerView.Adapter<NetworkAdapter.ViewHolder>() {

    private val netList: MutableList<NetworkInfo> = mutableListOf()

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
        viewHolder.name.text = netList[position].toString()
    }

    fun add(networkInfo: NetworkInfo) {
        netList.add(networkInfo)
        notifyItemInserted(netList.lastIndex)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)

    }

}