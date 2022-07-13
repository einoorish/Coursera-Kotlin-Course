package rationals

import java.math.BigInteger

data class Rational(private val rationalStr: String) : Comparable<Rational>{
    private val numerator: BigInteger
    private val denominator: BigInteger

    init {
        val splitResult = rationalStr.split("/")

        var numeratorBeforeNormalization = splitResult[0].toBigInteger()
        var denominatorBeforeNormalization = when (splitResult[1]) {
            "0" -> throw Exception("not allowed")
            else -> splitResult[1].toBigInteger()
        }

        val isNegative =
            (denominatorBeforeNormalization >= BigInteger.ZERO && numeratorBeforeNormalization < BigInteger.ZERO) ||
                    (denominatorBeforeNormalization <= BigInteger.ZERO && numeratorBeforeNormalization > BigInteger.ZERO)

        denominatorBeforeNormalization = denominatorBeforeNormalization.abs()
        numeratorBeforeNormalization = numeratorBeforeNormalization.abs()

        val gcd = numeratorBeforeNormalization.gcd(denominatorBeforeNormalization)

        if(isNegative)
            numeratorBeforeNormalization = -numeratorBeforeNormalization

        numerator = numeratorBeforeNormalization/gcd
        denominator = denominatorBeforeNormalization/gcd
    }

    operator fun plus(other: Rational): Rational{
        val commonDenominator = denominator * other.denominator
        val newNumerator = numerator * (commonDenominator / denominator) +
                other.numerator * (commonDenominator / other.denominator)

        return Rational(newNumerator, commonDenominator)
    }

    constructor(numerator: BigInteger, denominator: BigInteger) : this("${numerator}/${denominator}")
    constructor(numerator: Int, denominator: Int) : this("${numerator}/${denominator}")
    constructor(numerator: Long, denominator: Long) : this("${numerator}/${denominator}")

    operator fun minus(other: Rational): Rational{
        val commonDenominator = denominator * other.denominator
        val newNumerator = numerator * (commonDenominator / denominator) -
                other.numerator * (commonDenominator / other.denominator)

        return Rational(newNumerator, commonDenominator)
    }


    operator fun times(other: Rational): Rational{
        return Rational(numerator * other.numerator, denominator * other.denominator)
    }

    operator fun div(other: Rational): Rational{
        return Rational(numerator * other.denominator, denominator * other.numerator)
    }


    override fun toString(): String {
        if(denominator== BigInteger.ONE) return numerator.toString()
        return "$numerator/$denominator"
    }

    operator fun unaryMinus(): Rational {
        return Rational(-numerator, denominator)
    }

    override fun equals(other: Any?): Boolean {
        if(other is Rational) return compareTo(other) == 0
        return super.equals(other)
    }

    override operator fun compareTo(other: Rational): Int {
        val difference = this.minus(other)
        return difference.numerator.compareTo(BigInteger.ZERO)
    }

    data class RationalRange(val start: Rational, val end: Rational) {
        init {
            if(start > end) throw java.lang.IllegalArgumentException("invalid range")
        }

        operator fun contains(other: Rational) = start <= other && end >= other
    }

    operator fun rangeTo(end: Rational): RationalRange {
        return RationalRange(this, end)
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

infix fun BigInteger.divBy(denominator: BigInteger): Rational {
    if(denominator == BigInteger.ZERO) throw java.lang.IllegalArgumentException("0 not allowed for denominator")
    else return Rational(this, denominator)
}

infix fun Long.divBy(denominator: Long): Rational {
    if(denominator == 0L) throw java.lang.IllegalArgumentException("0 not allowed for denominator")
    else return Rational(this, denominator)
}

infix fun Int.divBy(denominator: Int): Rational {
    if(denominator == 0) throw java.lang.IllegalArgumentException("0 not allowed for denominator")
    else return Rational(this, denominator)
}


fun String.toRational(): Rational {
    val list = this.split('/')
    if(list.size > 2)
        throw java.lang.IllegalArgumentException("invalid rational string")

    val num1 = list[0].toBigIntegerOrNull()

    val num2 = run {
        if(list.size == 1) BigInteger.ONE
        else list[1].toBigIntegerOrNull()
    }
    
    if(num1 == null || num2 == null)
        throw java.lang.IllegalArgumentException("invalid rational string")

    return Rational(num1, num2)
}
