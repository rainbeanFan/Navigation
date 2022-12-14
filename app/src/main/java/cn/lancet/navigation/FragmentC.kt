package cn.lancet.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cn.lancet.navigation.databinding.FragmentCBinding


class FragmentC : Fragment() {

    private var dummyButton: Button? = null

    private var _binding: FragmentCBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dummyButton = binding.dummyButton

        dummyButton?.setOnClickListener {

            binding.root.findNavController()
                .navigate(R.id.action_fragmentC_pop_including_fragmentA, null)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dummyButton = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}