package vn.com.phucars.awesomemovies.ui.register

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.databinding.FragmentLoginBinding
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.base.BaseFragment

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun getClassTag(): String {
        return this::class.java.simpleName
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginStateFlow.collect {
                    when(it) {
                        is ResultViewState.Loading -> {
                            Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                        }
                        is ResultViewState.Success -> {
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        }
                        is ResultViewState.Error -> {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.formValidationStateFlow.collectLatest {
                    binding.btnLogin.isEnabled = it.isFormValid
                    val emailErrorsDescription = formatErrorList(it.emailErrorState)
                    val passwordErrorsDescription = formatErrorList(it.passwordErrorState)

                    binding.tilEmail.error = emailErrorsDescription
                    binding.tilPassword.error = passwordErrorsDescription
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle((Lifecycle.State.STARTED)) {
                viewModel.userDataFlow.collectLatest {
                    Toast.makeText(requireContext(), "User data = $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun formatErrorList(errorState: List<AuthorizationUIError>): String {
        if (errorState.isEmpty()) return ""
        val sb = StringBuilder()
        errorState.forEachIndexed { index, error ->
            sb.append(error.name)
            if (index != errorState.size - 1) { //not last element of the list
                sb.append("\n")
            }
        }
        return sb.toString()
    }

    override fun setViewListener() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
        }

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setEmailInput(binding.edtEmail.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setPasswordInput(binding.edtPassword.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

}