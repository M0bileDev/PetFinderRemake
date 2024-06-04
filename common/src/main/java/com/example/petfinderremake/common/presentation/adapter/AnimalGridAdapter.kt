package com.example.petfinderremake.common.presentation.adapter

//import com.example.petfinderremake.common.databinding.PagingBottomItemBinding
//import com.example.petfinderremake.common.domain.model.adapter.AnimalAdapter
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderremake.common.databinding.AnimalGridItemBinding
import com.example.petfinderremake.common.databinding.AnimalGridVerticalItemBinding
import com.example.petfinderremake.common.databinding.LoadMoreItemBinding
import com.example.petfinderremake.common.ext.ScaleType
import com.example.petfinderremake.common.ext.setImage
import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterEnum
import com.example.petfinderremake.common.presentation.adapter.model.AnimalAdapterModel
import com.example.petfinderremake.common.presentation.utils.combineAnimalCurrentSizeWithTotalCount
import com.example.petfinderremake.common.presentation.utils.extractBreed

class AnimalGridAdapter(
    private val resources: Resources,
    private val onViewDetailsClick: (Long) -> Unit,
    private val onMoreClick: (Long) -> Unit = {},
    private val onImageClick: (Long) -> Unit = {},
    private val onLoadMoreClick: () -> Unit = {},
) : ListAdapter<AnimalAdapterModel, RecyclerView.ViewHolder>(DiffCallback()) {

    private lateinit var animalGridItemBinding: AnimalGridItemBinding
    private lateinit var animalGridVerticalItemBinding: AnimalGridVerticalItemBinding
    private lateinit var loadMoreItemBinding: LoadMoreItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            AnimalAdapterEnum.ANIMAL_GRID_ITEM.ordinal -> {
                animalGridItemBinding = AnimalGridItemBinding.inflate(layoutInflater, parent, false)
                AnimalGridViewHolder(animalGridItemBinding)
            }

            AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM.ordinal -> {
                animalGridVerticalItemBinding =
                    AnimalGridVerticalItemBinding.inflate(layoutInflater, parent, false)
                AnimalGridVerticalViewHolder(animalGridVerticalItemBinding)
            }

            AnimalAdapterEnum.LOAD_MORE.ordinal -> {
                loadMoreItemBinding =
                    LoadMoreItemBinding.inflate(layoutInflater, parent, false)
                LoadMoreViewHolder(loadMoreItemBinding)
            }

            else -> throw Exception()
        }
    }

    fun isLoadMore(isLoading: Boolean) {
        if (::loadMoreItemBinding.isInitialized) {
            loadMoreItemBinding.pagingBottomFooterButton.isGone = isLoading
            loadMoreItemBinding.pagingBottomIsLoading.isVisible = isLoading
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (holder.itemViewType) {
            AnimalAdapterEnum.ANIMAL_GRID_ITEM.ordinal -> {
                (holder as AnimalGridViewHolder).bind(
                    item as AnimalAdapterModel.GridModel
                )
            }

            AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM.ordinal -> {
                (holder as AnimalGridVerticalViewHolder).bind(
                    item as AnimalAdapterModel.GridModelVertical
                )
            }

            AnimalAdapterEnum.LOAD_MORE.ordinal -> {
                (holder as LoadMoreViewHolder).bind(
                    item as AnimalAdapterModel.LoadMore
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return when (item.adapterViewType) {
            AnimalAdapterEnum.ANIMAL_ITEM_LINEAR -> AnimalAdapterEnum.ANIMAL_ITEM_LINEAR.ordinal
            AnimalAdapterEnum.ANIMAL_GRID_ITEM -> AnimalAdapterEnum.ANIMAL_GRID_ITEM.ordinal
            AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM -> AnimalAdapterEnum.ANIMAL_GRID_VERTICAL_ITEM.ordinal
            AnimalAdapterEnum.LOAD_MORE -> AnimalAdapterEnum.LOAD_MORE.ordinal
        }
    }

    inner class AnimalGridViewHolder(private val binding: AnimalGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animalAdapterGridModel: AnimalAdapterModel.GridModel) {
            with(animalAdapterGridModel) {

                val breed = extractBreed(breed, resources)
                val image = media.getFirstSmallestAvailablePhoto()

                binding.animalItemGridImage.setImage(image, ScaleType.CENTER_CROP)
                binding.animalItemGridName.text = name
                binding.animalItemGridBreed.text = breed

                binding.animalItemGridViewDetailsButton.setOnClickListener { _ ->
                    onViewDetailsClick(id)
                }
                binding.animalItemGridMoreButton.setOnClickListener { _ ->
                    onMoreClick(id)
                }
                binding.animalItemGridImage.setOnClickListener { _ ->
                    onImageClick(id)
                }
            }
        }
    }

    inner class AnimalGridVerticalViewHolder(private val binding: AnimalGridVerticalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animalAdapterGridModel: AnimalAdapterModel.GridModelVertical) {
            with(animalAdapterGridModel) {

                val breed = extractBreed(breed, resources)
                val image = media.getFirstSmallestAvailablePhoto()

                binding.animalItemGridImage.setImage(image, ScaleType.CENTER_CROP)
                binding.animalItemGridName.text = name
                binding.animalItemGridBreed.text = breed

                binding.animalItemGridViewDetailsButton.setOnClickListener { _ ->
                    onViewDetailsClick(id)
                }
                binding.animalItemGridMoreButton.setOnClickListener { _ ->
                    onMoreClick(id)
                }
                binding.animalItemGridImage.setOnClickListener { _ ->
                    onImageClick(id)
                }
            }
        }
    }

    inner class LoadMoreViewHolder(private val binding: LoadMoreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(animalAdapterGridModel: AnimalAdapterModel.LoadMore) {
            with(animalAdapterGridModel) {

                val animalCurrentSize =
                    currentList.filter { it.adapterViewType != AnimalAdapterEnum.LOAD_MORE }.size
                binding.pagingBottomHeaderText.text =
                    combineAnimalCurrentSizeWithTotalCount(animalCurrentSize, totalCount, resources)
                binding.pagingBottomFooterButton.setOnClickListener { _ ->
                    onLoadMoreClick()
                }
            }
        }
    }


    private class DiffCallback : DiffUtil.ItemCallback<AnimalAdapterModel>() {

        override fun areItemsTheSame(
            oldItem: AnimalAdapterModel,
            newItem: AnimalAdapterModel
        ): Boolean {
            return oldItem.adapterId == newItem.adapterId
        }

        override fun areContentsTheSame(
            oldItem: AnimalAdapterModel,
            newItem: AnimalAdapterModel
        ): Boolean {
            return oldItem == newItem
        }

    }

}