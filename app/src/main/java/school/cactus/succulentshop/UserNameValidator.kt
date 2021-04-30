package school.cactus.succulentshop

import android.text.TextUtils

class UserNameValidator : Validator {
    override fun validate(field: String) = when {
        TextUtils.isEmpty(field) -> R.string.this_field_is_required
        field.length < 2 -> R.string.username_too_short
        field.length > 8 -> R.string.username_too_long

        else -> null
    }
}