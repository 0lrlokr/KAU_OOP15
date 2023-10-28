package com.example.kaustudyroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kaustudyroom.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    var binding: FragmentLoginBinding?= null

    private val id = "asdf"   // 로그인할 수 있는 유일한 아이디
    private val password = "1234"   // 로그인할 수 있는 유일한 비밀번호

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

        binding?.btnLogin?.setOnClickListener {

            var textId = binding?.id?.text.toString()
            var textPw = binding?.password?.text.toString()

            if (textId == id && textPw == password) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            } else {
                if (textId.isNullOrEmpty() || textPw.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                } else if (textId != id) {
                    Toast.makeText(requireContext(), "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                } else if (textPw != password) {
                    Toast.makeText(requireContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                }
            }
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
