package io.github.quwac.how_to_use_jetpack_datastore.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.quwac.how_to_use_jetpack_datastore.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.exampleText.observe(viewLifecycleOwner, {
            message.text = it
        })
        viewModel.exampleNumber.observe(viewLifecycleOwner, {
            message2.text = it.toString()
        })

        save.setOnClickListener {
            viewModel.saveText(inputText.text.toString())
        }
        save2.setOnClickListener {
            viewModel.saveNumber(inputText2.text.toString().toInt())
        }
    }

}