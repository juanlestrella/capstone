package com.example.capstone.profiledetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.capstone.R
import com.example.capstone.databinding.FragmentProfileDetailsBindingImpl
import com.example.capstone.entities.TemplatesData
import com.example.capstone.profile.ProfileFragment
import com.example.capstone.viewmodel.SharedViewModel
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import okhttp3.internal.wait

class ProfileDetailsFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileDetailsBindingImpl.inflate(inflater)

        val navController = findNavController()

        val profileDetailsAdapter = ProfileDetailsAdapter()

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.recyclerProfileDetails.adapter = profileDetailsAdapter

        /**
         * List of all checked boxes from the viewModel
         * then submit to ProfileDetailsAdapter
         */
        val finalList = viewModel.returnFinalExercisesList()
        (binding.recyclerProfileDetails.adapter as ProfileDetailsAdapter).submitList(finalList)

        /**
         * When user clicks ADD EXERCISES, navigate to the ExercisesFragment
         */
        binding.addExercisesButtonId.setOnClickListener {
            navController.navigate(R.id.action_profileDetailFragment_to_exercisesFragment)
        }

        /**
         * After the list of exercises are added, click the DONE button
         * and navigate back to ProfileFragment with
         * Template's Title and list of Exercises
         */
        binding.doneButtonId.setOnClickListener {
            val title = binding.templateNameEditviewId.text.toString()
            val newTemplatesData: TemplatesData = TemplatesData(title, finalList)
            viewModel.insertNewTemplate(newTemplatesData)

            navController.navigate(R.id.action_profileDetailFragment_to_profileFragment)

            viewModel.clearFinalExercisesList()
        }

        return binding.root
    }
}