package com.example.moneyspliter

import java.text.DecimalFormat


fun getTipAmount(userAmount: String, tipPercentage: Float): String {

    return when {
        userAmount.isEmpty() -> {
            ""
        }

        else -> {
            val amount = userAmount.toFloat()
            ((amount * tipPercentage).div(100)).toString()

        }
    }

}

fun getTotalHeaderAmount(amount: String, tipPercentage: Float, perPersonCounter: Int): String {

    return when {
        amount.isEmpty() -> {
            ""
        }

        else -> {
            val userAmount = amount.toFloat()
            val tipAmount = (userAmount * tipPercentage).div(100)
            val perHeadAmount = (userAmount + tipAmount).div(perPersonCounter)
            perHeadAmount.toString()
        }
    }
}

fun formatTwoDecimalPoint(str: String): String {

    return if (str.isEmpty()) {
        " "
    } else {
        val format =
            DecimalFormat("##################.##")  // returns the float after formatting into string
        format.format(str.toFloat())  // converts the string into the float
    }
}