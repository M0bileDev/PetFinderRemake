package com.example.petfinderremake.features.notifications.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petfinderremake.common.domain.model.notification.Notification
import com.example.petfinderremake.common.presentation.utils.DateFormatUtils
import com.example.petfinderremake.common.presentation.utils.commonDrawable
import com.example.petfinderremake.features.notifications.R
import com.example.petfinderremake.features.notifications.databinding.NotificationItemBinding
import java.time.LocalDateTime
import java.time.ZoneOffset


class NotificationAdapter(
    private val onClick: (Long) -> Unit,
    private val onMoreClick: (Long) -> Unit
) : ListAdapter<Notification, RecyclerView.ViewHolder>(DiffCallback()) {

    private lateinit var notificationItemBinding: NotificationItemBinding

    private class DiffCallback : DiffUtil.ItemCallback<Notification>() {

        override fun areItemsTheSame(
            oldItem: Notification,
            newItem: Notification
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Notification,
            newItem: Notification
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        notificationItemBinding =
            NotificationItemBinding.inflate(layoutInflater, parent, false)
        return NotificationItemViewHolder(notificationItemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        (holder as NotificationItemViewHolder).bind(item)
    }

    inner class NotificationItemViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) = with(notification) {
            val localDateTime = LocalDateTime.ofEpochSecond(createdAt, 0, ZoneOffset.UTC)
            binding.notificationCreatedAt.text = DateFormatUtils.format(localDateTime)
            binding.notificationTitle.text = title
            binding.notificationDescription.text = description
            binding.root.setOnClickListener {
                onClick(id)
            }
            binding.notificationButtonMore.setOnClickListener {
                onMoreClick(id)
            }

            if (displayed) {
                binding.root.alpha = 0.9f
                binding.notificationStatus.setImageResource(commonDrawable.ic_check)
            } else {
                binding.root.alpha = 1f
                binding.notificationStatus.setImageResource(commonDrawable.ic_notification)
            }

        }


    }
}
