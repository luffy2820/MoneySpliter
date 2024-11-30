package com.example.moneyspliter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TipCalculator() {

    // state management for diff. values in app
    val amount = remember {
        mutableStateOf("")
    }

    val perPersonCounter = remember {
        mutableStateOf(1)    // sets the value of per person counter to 1 by default
    }

    val tipPercentage = remember {
        mutableStateOf(0f)
    }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        TotalHeader(
            formatTwoDecimalPoint(
                getTotalHeaderAmount(
                    amount.value,
                    tipPercentage.value,
                    perPersonCounter.value
                )
            )
        )
        UserInputArea(amount = amount.value, onAmountChange = {
            amount.value = it
        }, perPersonCounter = perPersonCounter.value, onAddOrReducePerPerson = {
            if (it == 1) {
                perPersonCounter.value++ // Increment if 'it' is 1
            } else if (it == -1 && perPersonCounter.value > 1) {
                perPersonCounter.value-- // Decrement only if 'it' is -1 and count > 1
            }


        },
            tipPercentage = tipPercentage.value, {
                tipPercentage.value = it
            })


    }

}

@Composable
fun TotalHeader(amount: String) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = Color.Cyan, shape = RoundedCornerShape(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Total Per Person", style = TextStyle(
                    color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "₹${amount}", style = TextStyle(
                    color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold
                )
            )

        }

    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserInputArea(
    amount: String,
    onAmountChange: (String) -> Unit,
    perPersonCounter: Int,
    onAddOrReducePerPerson: (Int) -> Unit,   // takes integer input then work according to logic
    tipPercentage: Float,
    onTipPercentageChange: (Float) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp), shape = RoundedCornerShape(12.dp),
        tonalElevation = 12.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // when the value is updated it comes to on value change which consist of one parameter ie. of value type in our case it is string so "it" refers to that string in this case
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    onAmountChange.invoke(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter The Amount")
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )

            if (amount.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Split", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.fillMaxWidth(0.5f))

                    CustomButton(imageVector = Icons.Default.KeyboardArrowUp) {

                        onAddOrReducePerPerson.invoke(1)  // 1 as input is passed to add person
                    }

                    Text(
                        text = "${perPersonCounter}", style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    CustomButton(imageVector = Icons.Default.KeyboardArrowDown) {
                        onAddOrReducePerPerson.invoke(-1)   // -1 is passed to decrease person
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Tip", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.fillMaxWidth(0.7f))
                    Text(
                        text = "₹ ${formatTwoDecimalPoint(getTipAmount(amount, tipPercentage))}",
                        style = MaterialTheme.typography.titleMedium
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatTwoDecimalPoint(tipPercentage.toString())} %",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = tipPercentage, onValueChange = {
                        onTipPercentageChange.invoke(it)
                    }, valueRange = 0f..100f, steps = 5,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                )


            }

        }
    }


}

@Composable
fun CustomButton(imageVector: ImageVector, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(12.dp)
            .clickable {
                onClick.invoke()
            },
        shape = CircleShape
    ) {

        Icon(
            imageVector = imageVector,
            contentDescription = "icon",
            modifier = Modifier
                .size(30.dp)
                .padding(4.dp)
        )
    }
}
