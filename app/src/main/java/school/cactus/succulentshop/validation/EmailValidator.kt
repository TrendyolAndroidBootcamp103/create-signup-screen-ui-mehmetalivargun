package school.cactus.succulentshop.validation

import android.text.TextUtils
import android.util.Patterns
import school.cactus.succulentshop.R

class EmailValidator : Validator {
    override fun validate(field: String) = when {
        TextUtils.isEmpty(field) -> R.string.this_field_is_required
        field.length<=5-> R.string.email_too_short
        !Patterns.EMAIL_ADDRESS.matcher(field).matches() -> R.string.not_valid_email
        else -> null
    }

}