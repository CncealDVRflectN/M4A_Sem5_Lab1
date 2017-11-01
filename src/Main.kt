fun calcFunction(a: Double, x: Double): Double {
    return Math.exp(a * x) / x
}

fun calcSecondDeriv(a: Double, x: Double): Double {
    return Math.exp(a * x) * (Math.pow(a * x, 2.0) - 2 * a * x + 2) / Math.pow(x, 3.0)
}

fun calcNodeNumMiddleRectangle(a: Double, intervalBottom: Double, intervalUpper: Double, accuracy: Double): Int {
    return Math.ceil(Math.sqrt((Math.pow(intervalUpper - intervalBottom, 3.0) * calcSecondDeriv(a, intervalBottom)) / (24 * accuracy))).toInt()
}

fun calcNodeNumTrapeze(a: Double, intervalBottom: Double, intervalUpper: Double, accuracy: Double): Int {
    return Math.ceil(Math.sqrt((Math.pow(intervalUpper - intervalBottom, 3.0) * calcSecondDeriv(a, intervalBottom)) / (12 * accuracy))).toInt()
}

fun calcIntegralLeftRectangle(a: Double, intervalBottom: Double, intervalUpper: Double, step: Double): Double {
    val nodeNum: Int = ((intervalUpper - intervalBottom) / step).toInt()
    return step * (0..(nodeNum - 1)).sumByDouble { calcFunction(a, intervalBottom + it * step) }
}

fun calcIntegralMiddleRectangle(a: Double, intervalBottom: Double, intervalUpper: Double, accuracy: Double): Double {
    val nodeNum = calcNodeNumMiddleRectangle(a, intervalBottom, intervalUpper, accuracy)
    val step = (intervalUpper - intervalBottom) / nodeNum
    println("Количество узлов: " + nodeNum)
    println("Шаг: " + step)
    return step * (0..(nodeNum - 1)).sumByDouble { calcFunction(a, intervalBottom + (it + 0.5) * step) }
}

fun calcIntegralTrapeze(a: Double, intervalBottom: Double, intervalUpper: Double, accuracy: Double): Double {
    val nodeNum = calcNodeNumTrapeze(a, intervalBottom, intervalUpper, accuracy)
    val step = (intervalUpper - intervalBottom) / nodeNum
    println("Количество узлов: " + nodeNum)
    println("Шаг: " + step)
    return step * ((calcFunction(a, intervalBottom) + calcFunction(a, intervalUpper)) / 2 +
            (1..(nodeNum - 1)).sumByDouble { calcFunction(a, intervalBottom + it * step) })
}

fun calcRunge(prevStep: Double, curStep: Double, prevIntegral: Double, curIntegral: Double, power: Double): Double {
    return (curIntegral - prevIntegral) / (1 - Math.pow(curStep / prevStep, power))
}

fun calcIntegralLeftRectangleRunge(a: Double, intervalBottom: Double, intervalUpper: Double, accuracy: Double): Double {
    var prevStep: Double
    var curStep: Double = intervalUpper - intervalBottom
    var prevIntegral: Double
    var curIntegral: Double = calcIntegralLeftRectangle(a, intervalBottom, intervalUpper, curStep)
    var coefRunge: Double
    do {
        prevStep = curStep
        curStep = prevStep / 2
        prevIntegral = curIntegral
        curIntegral = calcIntegralLeftRectangle(a, intervalBottom, intervalUpper, curStep)
        coefRunge = calcRunge(prevStep, curStep, prevIntegral, curIntegral, 1.0)
    } while (Math.abs(coefRunge) > accuracy)
    println("Шаг: " + curStep)
    println("Точность: " + accuracy)
    return curIntegral + (curIntegral - prevIntegral) / (Math.pow(prevStep / curStep, 1.0) - 1)
}

fun calcIntegralGauss(a: Double, intervalBottom: Double, intervalUpper: Double, nodeNum: Int): Double {
    var t: Double
    val nodes = arrayOf(-0.90617985, -0.53846931, 0.0, 0.53846931, 0.90617985)
    val coefs = arrayOf(0.23692688, 0.47862868, 0.56888889, 0.47862868, 0.23692688)
    println("Количество узлов: " + (nodeNum + 1))
    return (intervalUpper - intervalBottom) / 2 * (0..nodeNum).sumByDouble {
        t = ((intervalUpper - intervalBottom) * nodes[it] + intervalBottom + intervalUpper) / 2
        coefs[it] * calcFunction(a, t)
    }
}

fun main(args: Array<String>) {
    val a = 1.0
    val intervalBottom = 0.5
    val intervalUpper = 1.5
    val accuracy = Math.pow(10.0, -5.0)
    val nodeNumGauss = 4
    println("Формула левых прямоугольников(по правилу Рунге): ")
    println("Значение интеграла: " + calcIntegralLeftRectangleRunge(a, intervalBottom, intervalUpper, accuracy))
    println()
    println("Формула средних прямоугольников: ")
    println("Значение интеграла: " + calcIntegralMiddleRectangle(a, intervalBottom, intervalUpper, accuracy))
    println()
    println("Формула трапеций: ")
    println("Значение интеграла: " + calcIntegralTrapeze(a, intervalBottom, intervalUpper, accuracy))
    println()
    println("Квадратурная формула типа Гаусса: ")
    println("Значение интеграла: " + calcIntegralGauss(a, intervalBottom, intervalUpper, nodeNumGauss))
}