package com.example.petfinderremake.features.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderremake.features.filter.databinding.SelectItemBinding
import com.example.petfinderremake.features.filter.presentation.model.adapter.SelectAdapterModel

class SelectAdapter(
    private val onClick: (String) -> Unit
) :
    ListAdapter<SelectAdapterModel, RecyclerView.ViewHolder>(DiffCallback()) {

    private lateinit var selectItemBinding: SelectItemBinding

    private class DiffCallback : DiffUtil.ItemCallback<SelectAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: SelectAdapterModel,
            newItem: SelectAdapterModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SelectAdapterModel,
            newItem: SelectAdapterModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        selectItemBinding =
            SelectItemBinding.inflate(layoutInflater, parent, false)
        return SelectItemViewHolder(selectItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        (holder as SelectItemViewHolder).bind(item)
    }

    inner class SelectItemViewHolder(private val binding: SelectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(selectAdapterModel: SelectAdapterModel) = with(selectAdapterModel) {
            binding.selectItemCheckbox.isChecked = checked
            binding.selectItemText.text = name
            binding.selectItemRoot.setOnClickListener { _ ->
                onClick(name)
            }
        }


    }
}