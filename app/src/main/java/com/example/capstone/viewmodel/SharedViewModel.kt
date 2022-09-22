package com.example.capstone.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.capstone.database.getExercisesDataBase
import com.example.capstone.database.getTemplatesDatabase
import com.example.capstone.entities.ExercisesData
import com.example.capstone.entities.TemplatesData
import com.example.capstone.repository.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * Use AndroidViewModel(application) to give the
 * SharedViewModel class an application parameter
 * to pass to the database
 */
class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _checkedExercisesList = MutableLiveData<List<ExercisesData>>()
    val checkedExercisesList: LiveData<List<ExercisesData>>
        get() = _checkedExercisesList

    private val exercisesDatabase = getExercisesDataBase(application)
    private val templatesDatabase = getTemplatesDatabase(application)
    private val repository = Repository(exercisesDatabase, templatesDatabase)
    private val finalExercisesList = mutableListOf<ExercisesData>()

    val templates = repository.templatesList
    val exercisesList = repository.exercisesList

    var isEditing: Boolean = false
    var isEditTemplateSaved: Boolean = false
    lateinit var current_editing_template: TemplatesData

    val allBodyparts = repository.allBodyParts
    val allEquipments = repository.allEquipments
    val allTarget = repository.allTargets

    /**
     * Get all the exercises from database
     */
    fun getAllExercises(){
        repository.getAllExercisesList()
    }

    /**
     * Search Bar: search an exercise with name
     */
    fun searchExercise(name: String){
        repository.searchExercise(name)
    }

    /**
     * More general way of filtering all three types (Body Parts, Equipments, Targets)
     */
    fun filterExercises(bodyPart: String, equipment: String, target: String){
        repository.filterExercises(bodyPart, equipment, target)
    }

    /**
     * Deletes a specific exercise
     */
    fun deleteExercise(exercisesData: ExercisesData) {
        finalExercisesList.remove(exercisesData)
    }

    /**
     * Insert an exercise in finalExercisesList
     */
    fun insertExercise(exercisesData: ExercisesData) {
        finalExercisesList.add(exercisesData)
    }

    /**
     * Deletes template in TemplatesRoom
     */
    fun deleteTemplate(template: TemplatesData) {
        repository.deleteTemplate(template)
    }

    /**
     * Insert new template in TemplatesRoom
     */
    fun insertNewTemplate(newTemplate: TemplatesData) {
        repository.insertNewTemplate(newTemplate)
    }

    /**
     * Remove all elements in finalExercisesList
     */
    fun clearFinalExercisesList() {
        _checkedExercisesList.postValue(emptyList())
        return finalExercisesList.clear()
    }

    /**
     * Allows ProfileDetailsFragment to keep adding exercises without
     * resetting the value everytime "ADD EXERCISES" button is clicked
     * RETURN: MutableList of ExercisesData
     */
    fun returnFinalExercisesList(): MutableList<ExercisesData> {
        _checkedExercisesList.value?.forEach {
            if (it !in finalExercisesList) {
                finalExercisesList.add(it)
            }
        }
        return finalExercisesList
    }

    /**
     * Update the value of checkedExercisesList with the value
     * from ExercisesAdapter.returnCheckBoxList()
     */
    fun setCheckedExercisesList(dataList: MutableList<ExercisesData>) {
        _checkedExercisesList.postValue(dataList)
    }

    /**
     * Change the value of all
     * ExercisesData.isChecked to false
     */
    fun checkBoxAllFalse() {
        exercisesList.value?.forEach { exercise ->
            if (exercise.isChecked) {
                exercise.isChecked = false
            }
        }
    }

    init {
        /**
         * Refresh the Data and make sure API is connected
         */
        viewModelScope.launch {
            try {
                repository.refreshData()
                _status.postValue("Connected")
                Log.i("ExercisesViewModel", status.value.toString())
            } catch (e: Exception) {
                _status.postValue("Failure: " + e.message)
            }
        }
    }
}