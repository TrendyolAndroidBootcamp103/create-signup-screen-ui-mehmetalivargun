package school.cactus.succulentshop

import java.util.regex.Pattern

class SignUpPasswordValidator : Validator {
    var numberExp = ".*[0-9].*"
    var number = Pattern.compile(numberExp, Pattern.CASE_INSENSITIVE)
    var capitalLetterexp = ".*[A-Z].*"
    var capitalLetter = Pattern.compile(capitalLetterexp)
    var smallLetterexp = ".*[a-z].*"
    var smallLetter = Pattern.compile(smallLetterexp)
    var specialCharExp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
    var specialChar = Pattern.compile(specialCharExp)

    override fun validate(field: String) = when {
        field.length < 8 ->R.string.password_too_short
        !number.matcher(field).matches() -> R.string.number
        !capitalLetter.matcher(field).matches() -> R.string.capital_letter
        !smallLetter.matcher(field).matches() -> R.string.small_letter
        !specialChar.matcher(field).matches() -> R.string.special
        else -> null
    }
}