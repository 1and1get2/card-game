package cardgame.derek.util

import java.security.InvalidParameterException
import java.util.*

/**
 * User: derek
 * Date: 10/10/18 2:45 PM
 */


val random = Random()

/**
 *
 * @param max: exclusive
 */
fun randomIndexes(size: Int, max : Int, min : Int = 0) : Array<Int> {
    if (size > max - min) throw InvalidParameterException()
    return arrayOf(1, 2, 4)
}