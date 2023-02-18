package com.kazbekov.olimpiada.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kazbekov.olimpiada.R
import com.kazbekov.olimpiada.databinding.ItemServiceBinding

class ServiceAdapter(private val onItemClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    private var currentList: List<ServiceVK> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val itemBinding = ItemServiceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ServiceViewHolder(itemBinding, onItemClick)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun submitList(list: List<ServiceVK>) {
        currentList = list
        notifyDataSetChanged()
    }

    class ServiceViewHolder(
        itemBinding: ItemServiceBinding,
        onItemClick: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private val serviceIcon: ImageView = itemBinding.serviceIcon
        private val serviceName: TextView = itemBinding.serviceName

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(service: ServiceVK) {
            serviceName.text = service.name
            Glide.with(itemView)
                .load(service.iconUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(serviceIcon)
        }
    }
}