package com.example.petfinderremake.common.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderremake.common.databinding.AnimalTypeGridItemBinding
import com.example.petfinderremake.common.domain.model.animal.AnimalType

class AnimalTypeGridAdapter(
    val onClick: (String) -> Unit
) : ListAdapter<AnimalType, RecyclerView.ViewHolder>(DiffCallback()) {

    private lateinit var animalTypeGridItemBinding: AnimalTypeGridItemBinding

    inner class AnimalTypeViewHolder(
        private val binding: AnimalTypeGridItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(animalType: AnimalType) {
            with(animalType) {
                binding.animalTypeItemGridName.text = name
                // TODO: Create icons
//                binding.animalTypeItemGridImageView.setImage()
                binding.animalTypeGridItemContainer.setOnClickListener { _ ->
                    onClick(name)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        animalTypeGridItemBinding = AnimalTypeGridItemBinding.inflate(layoutInflater, parent, false)
        return AnimalTypeViewHolder(animalTypeGridItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        (holder as AnimalTypeViewHolder).bind(item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private class DiffCallback : DiffUtil.ItemCallback<AnimalType>() {

        override fun areItemsTheSame(
            oldItem: AnimalType,
            newItem: AnimalType
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: AnimalType,
            newItem: AnimalType
        ): Boolean {
            return oldItem == newItem
        }

    }
}

