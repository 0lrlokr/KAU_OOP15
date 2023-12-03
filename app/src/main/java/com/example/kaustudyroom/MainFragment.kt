package com.example.kaustudyroom


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.kaustudyroom.databinding.FragmentMainBinding
import com.example.kaustudyroom.viewmodel.MainPageViewModel


class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    val viewModel: MainPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.retrieveQuote()

        viewModel.quote.observe(viewLifecycleOwner) {advice ->
            binding?.txtQuote?.text = advice
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}