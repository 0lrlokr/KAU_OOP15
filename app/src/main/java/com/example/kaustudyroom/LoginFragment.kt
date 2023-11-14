package com.example.kaustudyroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.kaustudyroom.databinding.FragmentLoginBinding
import com.example.kaustudyroom.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    val viewModel: AuthViewModel by activityViewModels()
    var binding: FragmentLoginBinding?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        (activity as AppCompatActivity).supportActionBar?.hide()    // 액션바 숨기기
        val mainAct = activity as MainActivity
        mainAct.hideBottomNavigation(true)      // 네비바 숨기기

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginStatus.observe( viewLifecycleOwner, Observer { isSuccess ->
            if( isSuccess ) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
            else {
                Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })

        binding?.btnLogin?.setOnClickListener {
            val email = binding?.id?.text.toString()
            val password = binding?.password?.text.toString()
            viewModel.loginUser(email, password)
//            if (textId == id && textPw == password) {
//                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
//            } else {
//                if (textId.isNullOrEmpty() || textPw.isNullOrEmpty()) {
//                    Toast.makeText(requireContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
//                } else if (textId != id) {
//                    Toast.makeText(requireContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
//                } else if (textPw != password) {
//                    Toast.makeText(requireContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
//                }
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        (activity as AppCompatActivity).supportActionBar?.show()    // 다른 화면으로 이동할 때 액션바 다시 표시
        val mainAct = activity as MainActivity
        mainAct.hideBottomNavigation(false)     // 다른 화면으로 이동할 때 네비바 다시 표시

        binding = null
    }
}
