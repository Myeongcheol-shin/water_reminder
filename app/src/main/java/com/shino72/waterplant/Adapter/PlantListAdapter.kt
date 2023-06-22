package com.shino72.waterplant.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shino72.waterplant.databinding.ItemCbBinding

class PlantListAdapter : RecyclerView.Adapter<PlantListAdapter.PlantListViewHolder>() {
    private var items: List<Plant> = ArrayList()
    private lateinit var itemClickListner: ItemClickListener

    private var mSelectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantListViewHolder =
        PlantListViewHolder(ItemCbBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(holder: PlantListViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setItem(item: List<Plant>) {
        items = item
        mSelectedItem = -1
        notifyDataSetChanged()
    }


    interface ItemClickListener {
        fun onClick(view: View, cbItem: Plant, position: Int)
    }

    inner class PlantListViewHolder(private val binding: ItemCbBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Plant, position: Int) {
            val checkBox = binding.itemCb
            val deleteBtn = binding.deleteBtn
            binding.nameTv1.text = item.name
            checkBox.isChecked = position == mSelectedItem

            checkBox.setOnClickListener {
                mSelectedItem = position

                itemClickListner.onClick(it,item, position)
                notifyItemRangeChanged(0, items.size)
            }
            deleteBtn.setOnClickListener {
                itemClickListner.onClick(it,item, position)
                notifyItemRangeChanged(0, items.size)
            }

        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}