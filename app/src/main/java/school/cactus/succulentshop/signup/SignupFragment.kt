package school.cactus.succulentshop.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.validation.EmailValidator
import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.SignUpPasswordValidator
import school.cactus.succulentshop.validation.UserNameValidator
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.signup.SignupRequest
import school.cactus.succulentshop.api.signup.SignupResponse
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentSignupBinding

class SignupFragment: Fragment() {
    private val emailValidator =
        EmailValidator()
    private val userNameValidator =
        UserNameValidator()
    private val signUpPasswordValidator =
        SignUpPasswordValidator()
    private var _binding:FragmentSignupBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSignupBinding.inflate(inflater,container,false)
        return _binding!!.root
    }
    private fun TextInputLayout.isValid(): Boolean {
        val errorMessage = validator().validate(editText!!.text.toString())
        error = errorMessage?.resolveAsString()
        isErrorEnabled = errorMessage != null
        return errorMessage == null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            signUpButton.setOnClickListener {
                if (passwordInputLayout.isValid() and emailInputLayout.isValid() and usernameInputLayout.isValid()) {
                    sendSignupRequest()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun Int.resolveAsString() = getString(this)

    private fun TextInputLayout.validate() {
        val errorMessage = validator().validate(editText!!.text.toString())
        error = errorMessage?.resolveAsString()
        isErrorEnabled = errorMessage != null
    }

    private fun TextInputLayout.validator() = when (this) {
        binding.emailInputLayout -> emailValidator
        binding.usernameInputLayout -> userNameValidator
        binding.passwordInputLayout -> signUpPasswordValidator

        else -> throw IllegalArgumentException("Cannot find any validator for the given TextInputLayout")
    }
    private fun onSignupSucces(response: SignupResponse) {
        JwtStore(requireContext()).saveJwt(response.jwt)
        findNavController().navigate(R.id.signupSuccesful)
    }
    private fun onClientError(errorBody: ResponseBody?) {
        if (errorBody == null) return onUnexpectedError()

        try {
            val message = errorBody.errorMessage()
            Snackbar.make(binding.root, message, BaseTransientBottomBar.LENGTH_LONG).show()
        } catch (ex: JsonSyntaxException) {
            onUnexpectedError()
        }
    }
    private fun ResponseBody.errorMessage(): String {
        val errorBody = string()
        val gson: Gson = GsonBuilder().create()
        val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return loginErrorResponse.message[0].messages[0].message
    }

    private fun onUnexpectedError() {
        Snackbar.make(binding.root, R.string.unexpected_error_occurred,
            BaseTransientBottomBar.LENGTH_LONG
        ).show()
    }
    private fun sendSignupRequest() {
        val email=binding.emailInputLayout.editText!!.text.toString()
        val identifier = binding.usernameInputLayout.editText!!.text.toString()
        Log.e("Error",identifier)
        val password = binding.passwordInputLayout.editText!!.text.toString()

        val request = SignupRequest(email,password,identifier)

        api.register(request).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                when (response.code()) {
                    200 -> onSignupSucces(response.body()!!)
                    in 400..499 -> onClientError(response.errorBody())
                    else -> onUnexpectedError()
                }
            }

            private fun onUnexpectedError() {
                Snackbar.make(binding.root, R.string.unexpected_error_occurred,
                    BaseTransientBottomBar.LENGTH_LONG
                ).show()
            }
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Snackbar.make(binding.root, R.string.check_your_connection,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) {
                        //sendLoginRequest()
                    }
                    .show()
            }
        })
    }
}


