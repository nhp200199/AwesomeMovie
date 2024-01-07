package vn.com.phucars.awesomemovies.ui.register

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
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
import vn.com.phucars.awesomemovies.databinding.FragmentRegisterBinding
import vn.com.phucars.awesomemovies.ui.ResultViewState
import vn.com.phucars.awesomemovies.ui.base.BaseFragment

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun getClassTag(): String {
        return RegisterFragment::class.java.simpleName
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.formValidationStateFlow.collectLatest {
                    binding.btnRegister.isEnabled = it.isFormValid
                    val emailErrorsDescription = formatErrorList(it.emailErrorState)
                    val passwordErrorsDescription = formatErrorList(it.passwordErrorState)
                    val repeatPasswordErrorsDescription = formatErrorList(it.repeatPasswordErrorState)

                    binding.tilEmail.error = emailErrorsDescription
                    binding.tilPassword.error = passwordErrorsDescription
                    binding.tilRepeatPassword.error = repeatPasswordErrorsDescription
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerStateFlow.collect {
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
        binding.btnRegister.setOnClickListener {
            viewModel.register(binding.edtRegEmail.text.toString(), binding.edtRegPassword.text.toString()  )
        }

        binding.edtRegEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setEmailInput(binding.edtRegEmail.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.edtRegPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setPasswordInput(binding.edtRegPassword.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.edtRepeatPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setRepeatPasswordInput(binding.edtRepeatPassword.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }
}