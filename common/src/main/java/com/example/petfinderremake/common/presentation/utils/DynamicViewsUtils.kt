package com.example.petfinderremake.common.presentation.utils

import android.view.ViewGroup
import androidx.core.view.children

// TODO: Rebuild and develop

//inline fun <reified T> ViewGroup.removeDynamicViews(predication: () -> Boolean) where T : ViewGroup {
//    if (!predication()) return
//
//    val children = this.children
//    val iterator = children.iterator()
//    while (iterator.hasNext()) {
//        val view = iterator.next()
//        if (view is T) {
//            removeView(view)
//        }
//    }
//}

//        if (isBottomLoading) return@with

//        with(binding.discoverBottom.discoverBottomConstraintContainer) {
//            removeDynamicViews<FrameLayout>(predication = {
//                // ConstraintLayout contains Flow helper widget count as one, so if count is more
//                // than 1 it means that contains dynamic views
//                children.count() > 1
//            })
//        }

//        val dynamicReferencedIds = mutableListOf<Int>()
//        animalTypes.forEachIndexed { index, animalType ->
//            val inflater = LayoutInflater.from(context)
//            val view = AnimalTypeButtonItemBinding.inflate(
//                inflater,
//                (requireView() as ViewGroup),
//                false
//            )
//            view.apply {
//                val id = generateViewId()
//                dynamicReferencedIds.add(id)
//                this.root.id = id
//            }
//            view.imageButtonAnimal.apply {
//                with(animalType.name) {
//                    text = this
//                    contentDescription =
//                        resources.getString(commonString.animal_type_button, this)
//                }
//                setOnClickListener { _ ->
//                    discoverNavigation.navigateToSearch(
//                        this@DiscoverFragment,
//                        AnimalParameters(type = animalType.name)
//                    )
//                }
//            }
//            binding.discoverBottom.discoverBottomConstraintContainer.addView(view.root, index)
//        }
//        binding.discoverBottom.discoverBottomFlowContainer.referencedIds =
//            dynamicReferencedIds.toIntArray()