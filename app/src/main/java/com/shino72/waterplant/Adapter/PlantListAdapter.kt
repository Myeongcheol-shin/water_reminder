package com.shino72.waterplant.Adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
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
            binding.nameTv1.text = item.name
            checkBox.isChecked = position == mSelectedItem

            binding.apply {
                iv1.apply {
                    Glide.with(this)
                        .load(item.iv1)
                        .centerCrop()
                        .into(this)
                }
                iv2.apply {
                    Glide.with(this)
                        .load(item.iv2)
                        .centerCrop()
                        .into(this)
                }
                iv3.apply {
                    Glide.with(this)
                        .load(item.iv3)
                        .centerCrop()
                        .into(this)
                }
                iv4.apply {
                    Glide.with(this)
                        .load(item.iv4)
                        .centerCrop()
                        .into(this)
                }
            }
            checkBox.setOnClickListener {
                mSelectedItem = position

                itemClickListner.onClick(it,item, position)
                notifyItemRangeChanged(0, items.size)
            }
        }
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}