package school.cactus.succulentshop.validation

import school.cactus.succulentshop.R

class SignUpPasswordValidator : Validator {


    override fun validate(field: String) = when {
        field.isEmpty()-> R.string.password_is_required
        field.length < 8 -> R.string.password_too_short
        !field.contains("""[0-9]""".toRegex()) -> R.string.number
        !field.contains("""[A-Z]""".toRegex()) -> R.string.capital_letter
        !field.contains("""[a-z]""".toRegex()) -> R.string.small_letter
        !field.contains("""[~!@#\${'$'}%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?]""".toRegex()) -> R.string.special
        else -> null
    }
}