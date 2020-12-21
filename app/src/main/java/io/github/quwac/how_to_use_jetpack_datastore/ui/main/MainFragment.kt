package io.github.quwac.how_to_use_jetpack_datastore.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.quwac.how_to_use_jetpack_datastore.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelProvider = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )
        viewModel = viewModelProvider.get(MainViewModel::class.java)
        viewModel.exampleText.observe(viewLifecycleOwner, {
            binding.message.text = it
        })
        viewModel.exampleNumber.observe(viewLifecycleOwner, {
            binding.message2.text = it.toString()
        })

        binding.save.setOnClickListener {
            viewModel.saveText(binding.inputText.text.toString())
        }
        binding.save2.setOnClickListener {
            viewModel.saveNumber(binding.inputText2.text.toString().toInt())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}