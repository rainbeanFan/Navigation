package cn.lancet.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import cn.lancet.navigation.databinding.FragmentABinding
import cn.lancet.navigation.module.User

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FragmentMessage : Fragment() {


    private var _binding: FragmentABinding? = null

    private var mRvMessage:RecyclerView?=null


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

        mRvMessage = binding.rvMessage

    }

    private fun saveUser() {
        val user = User("Lucky","hello","rainbean@126.com")

        user.save(object : SaveListener<String>() {
            override fun done(objectId: String?, e: BmobException?) {
                e?.let {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }



        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mRvMessage = null

        Log.d("onDestroy  ","Fragment A")

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView  ","Fragment A")

        _binding = null
    }


}