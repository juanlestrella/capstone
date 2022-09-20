package com.example.capstone.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.databinding.ListItemExercisesBinding
import com.example.capstone.entities.ExercisesData
import com.example.capstone.viewmodel.SharedViewModel

class ExercisesAdapter() :
    ListAdapter<ExercisesData, ExercisesAdapter.ViewHolder>(ExercisesDiffCallback()){

    val checkBoxList = mutableListOf<ExercisesData>()

    /**
     * RETURN: List of ExercisesData with checked box
     */
    fun returnCheckBoxList(): MutableList<ExercisesData> {
        return checkBoxList
    }

    inner class ViewHolder(private var binding: ListItemExercisesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ExercisesData) {
            binding.exercises = data

            binding.checkBox.isChecked = data.isChecked // avoid reusing checked boxes

            /**
            If current box is checked, then add current data
            to mutable list of ExercisesData. Otherwise,
            the current box is unchecked so then
            remove the current data from the list.
             **/
            binding.checkBox.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked) {
                    if(binding.exercises !in checkBoxList){
                        checkBoxList.add(data)
                    }
                } else {
                    checkBoxList.remove(data)
                }
            }
            if (binding.exercises in checkBoxList){
                binding.checkBox.isChecked = true
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemExercisesBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                return FilterResults().apply {
//                    values =
//                        if (constraint.isNullOrEmpty()){
//                            vmList
//                        }else{
//                            val filteredList = mutableListOf<ExercisesData>()
//                            vmList.filter()
//                        }
//                }
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                submitList(results?.values as List<ExercisesData>)
//            }
//        }
//    }

}

class ExercisesDiffCallback : DiffUtil.ItemCallback<ExercisesData>() {

    override fun areItemsTheSame(oldItem: ExercisesData, newItem: ExercisesData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ExercisesData, newItem: ExercisesData): Boolean {
        return oldItem.id == newItem.id
    }

}
