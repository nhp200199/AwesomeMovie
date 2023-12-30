package vn.com.phucars.awesomemovies.ui.register

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.com.phucars.awesomemovies.databinding.FragmentLoginBinding
import vn.com.phucars.awesomemovies.ui.base.BaseFragment

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
                viewModel.formValidationStateFlow.collectLatest {
                    binding.btnLogin.isEnabled = it.isFormValid
                    val emailErrorsDescription = formatErrorList(it.emailErrorState)
                    val passwordErrorsDescription = formatErrorList(it.passwordErrorState)

                    binding.tilEmail.error = emailErrorsDescription
                    binding.tilPassword.error = passwordErrorsDescription
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