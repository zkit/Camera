package io.github.zkit.commons.differs

import androidx.recyclerview.widget.DiffUtil
import io.github.zkit.beans.BeanWatermarkConfig

internal class DifferFragmentConfig(
    private val new: MutableList<Pair<String, BeanWatermarkConfig>>,
    private val old: MutableList<Pair<String, BeanWatermarkConfig>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old.get(index = oldItemPosition)
        val newItem = new.get(index = newItemPosition)
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old.get(index = oldItemPosition)
        val newItem = new.get(index = newItemPosition)
        return oldItem.second.value == newItem.second.value

    }
}