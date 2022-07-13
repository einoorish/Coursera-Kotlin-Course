package nicestring

fun String.isNice(): Boolean {
    val containsBadSubstrings: (s: String) -> Boolean = {
        !this.contains("bu") && !this.contains("ba") && !this.contains("be")
    }

    val vowels = listOf('a', 'e', 'i', 'o', 'u')
    val containsAtLeastThreeVowels: (s: String) -> Boolean = {
        val vowelsInString = it.partition { ch -> vowels.contains(ch) }.first
        vowelsInString.count() > 2
    }

    val containsADoubleLetter: (s: String) -> Boolean = {
        it.zipWithNext().any { (first, second) -> first == second }
    }

    val conditionStatus: List<Boolean> = listOf(
        containsBadSubstrings(this),
        containsAtLeastThreeVowels(this),
        containsADoubleLetter(this)
    )

    return conditionStatus.count { it } >=2
}