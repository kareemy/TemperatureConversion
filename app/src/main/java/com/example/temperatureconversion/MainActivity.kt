package com.example.temperatureconversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.temperatureconversion.ui.theme.TemperatureConversionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureConversionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TemperatureLayout()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureLayout() {
    // State to be remembered and observed by Compose
    var inputTemp by remember { mutableStateOf("")} // String user types into TextField
    var fToC by remember { mutableStateOf(false) } // Boolean representing switch

    // inputTemp string converted to double or 0 if no input.
    val temp = inputTemp.toDoubleOrNull() ?: 0.0

    // outputTemp is Double, this is the converted temperature
    val outputTemp = temperatureFormula(temp, fToC)
    val degC = "\u2103" // °C degree symbol in unicode
    val degF = "\u2109"// °F

    var headerText = stringResource(id = R.string.head, degC, degF)
    var labelText = stringResource(id = R.string.celsius)
    var outputText = String.format("%.2f ${degC} is %.2f ${degF}", temp, outputTemp)

    // If converting from Fahrenheit to Celsius, change text to indicate that
    if (fToC) {
        headerText = stringResource(id = R.string.head, degF, degC)
        labelText = stringResource(id = R.string.fahrenheit)
        // Format to 2 decimal places
        outputText = String.format("%.2f ${degF} is %.2f ${degC}", temp, outputTemp)
    }

    Column(
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headerText,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        TextField(
            value = inputTemp,
            onValueChange = { inputTemp = it },
            singleLine = true,
            label = { Text(labelText) },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.thermostat), null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        ChangeConversionSwitch(
            fToC = fToC,
            onCheckedChange = { fToC = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = outputText,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
    }
}

@Composable
fun ChangeConversionSwitch(
    fToC: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp)
    ) {
        Text("Convert \u2109 to \u2103?")
        Switch(
            checked = fToC,
            onCheckedChange = onCheckedChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

private fun temperatureFormula(inputTemp: Double, fToC: Boolean): Double {
    if (fToC) return (inputTemp - 32.0) * 5.0 / 9.0
    else return 9.0 / 5.0 * inputTemp + 32.0
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TemperatureLayoutPreview() {
    TemperatureConversionTheme {
        TemperatureLayout()
    }
}