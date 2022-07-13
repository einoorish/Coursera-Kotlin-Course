package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var right = 0
    var wrong = 0
    var secretArrList = secret.toMutableList()
    var correctPositions: MutableList<Int> = mutableListOf()

    for(i in secret.indices){
        if(secret[i] == guess[i]){
            right++
            secretArrList.remove(secret[i])
            correctPositions.add(i)
        }
    }

    for(i in secret.indices){
        if (secretArrList.contains(guess[i]) && i !in correctPositions){
            secretArrList.remove(guess[i])
            wrong++
        }
    }

    return Evaluation(right, wrong)
}
