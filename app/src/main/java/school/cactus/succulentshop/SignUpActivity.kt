package school.cactus.succulentshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import school.cactus.succulentshop.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val emailValidator = EmailValidator()
    private val userNameValidator = UserNameValidator()
    private val signUpPasswordValidator = SignUpPasswordValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Sign Up"


        binding.apply {
            signUpButton.setOnClickListener {
                emailInputLayout.validate()
                usernameInputLayout.validate()
                passwordInputLayout.validate()
            }
            logInButton.setOnClickListener {
                intentLoginActivity();
            }
        }


    }

    private fun intentLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
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
}