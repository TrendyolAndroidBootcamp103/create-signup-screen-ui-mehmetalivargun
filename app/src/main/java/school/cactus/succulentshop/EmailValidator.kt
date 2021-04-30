package school.cactus.succulentshop

import android.text.TextUtils
import android.util.Patterns

class EmailValidator : Validator {
    override fun validate(field: String) = when {
        TextUtils.isEmpty(field) -> R.string.this_field_is_required
        !Patterns.EMAIL_ADDRESS.matcher(field).matches() -> R.string.not_valid_email
        else -> null
    }

}