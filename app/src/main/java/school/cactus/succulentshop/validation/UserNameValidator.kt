package school.cactus.succulentshop.validation

import android.text.TextUtils
import school.cactus.succulentshop.R

class UserNameValidator : Validator {
    override fun validate(field: String) = when {
        TextUtils.isEmpty(field) -> R.string.this_field_is_required
        !field.contains("""^[a-z0-9_]+${'$'}""".toRegex())-> R.string.username_error
        field.length <= 2 -> R.string.username_too_short
        field.length > 8 -> R.string.username_too_long

        else -> null
    }
}