package com.sergiocruz.roomtests

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.TypeConverter
import androidx.viewbinding.ViewBinding
import com.sergiocruz.roomtests.databinding.FragmentFirstBinding
import com.sergiocruz.roomtests.model.Word
import com.sergiocruz.roomtests.viewmodel.WordViewModel
import com.sergiocruz.roomtests.viewmodel.WordViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val binding: FragmentFirstBinding get() {
        return _binding ?: throw UnsupportedOperationException("Cannot access views before onCreateView or after onDestroyView! WTF are you doing?")
    }

    private var _binding: FragmentFirstBinding? = null

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((activity?.application as App).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerview
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        setupObservers()


        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allWords.observe(viewLifecycleOwner) { words ->
            // Update the cached copy of the words in the adapter.
            adapter.submitList(words)
        }

        binding.buttonZero.setOnClickListener {
            wordViewModel.getUsersAddresses().observe(viewLifecycleOwner) {
                print(it)
            }
            CoroutineScope(Dispatchers.Default).launch {
                val users = wordViewModel.getAllUsers()
                print(users)
            }
        }


        binding.buttonOne.setOnClickListener {
            wordViewModel.triggerGetAllUsers()
        }


        binding.buttonTwo.setOnClickListener {
            val text = binding.editTextTextPersonName.text.toString()
            val word = Word(word = text)
            wordViewModel.insert(word)
        }

        wordViewModel.loginService.observe(viewLifecycleOwner) {
            print(it)
        }

        binding.buttonThree.setOnClickListener {
            wordViewModel.loginClick("cenas")
//            val wordID = binding.editTextTextPersonName.text.toString().toIntOrNull() ?: 0
//            val v: LiveData<List<UserWithAddress>>? = wordViewModel.getUserWithID(5)
//            print(v)
        }

        binding.buttonNextFragment.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonFour.setOnClickListener {
            wordViewModel.getUserAndLibrary().observe(viewLifecycleOwner) {

            }
        }

        binding.buttonOneToMany.setOnClickListener {
            wordViewModel.getUsersWithPlayLists(callback = {
                print(it)
            })
        }

    }


    private fun setupObservers() {
        wordViewModel.progressBarVisibility.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = it
        }

        wordViewModel.allUsers.observe(viewLifecycleOwner) {
            binding.editTextTextPersonName.setText(it?.getOrNull(0)?.firstName ?: "")
            Log.i("Sergio", "setupObservers allusers: $it")
        }
    }
}

