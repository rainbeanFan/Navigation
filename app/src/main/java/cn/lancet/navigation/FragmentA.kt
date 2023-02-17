package cn.lancet.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import cn.lancet.navigation.databinding.FragmentABinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FragmentA : Fragment() {

    private var dummyButton: Button? = null

    private var _binding: FragmentABinding? = null


    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate  ","Fragment A")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("onCreateView  ","Fragment A")
        _binding = FragmentABinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume  ","Fragment A")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("onViewCreated  ","Fragment A")

        dummyButton = binding.dummyButton

        val options = navOptions {
            anim {
                enter = R.anim.slide_in
                exit = R.anim.fade_out
                popEnter = R.anim.fade_in
                popExit = R.anim.slide_out
            }
        }

        dummyButton?.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.fragmentB, null, options)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        dummyButton = null

        Log.d("onDestroy  ","Fragment A")

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView  ","Fragment A")

        _binding = null
    }


}