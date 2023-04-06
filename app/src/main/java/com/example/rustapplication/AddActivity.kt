package com.example.rustapplication

//import com.example.rustapplication.lib.Inputs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rustapplication.lib.Entry
import com.example.rustapplication.lib.Exercise
import com.example.rustapplication.lib.History
import com.example.rustapplication.lib.RustLog
import com.example.rustapplication.ui.theme.RustApplicationTheme
import java.text.SimpleDateFormat
import java.util.*


class AddActivity : ComponentActivity() {

    companion object {
        init {
            System.loadLibrary("rust_lib")
            RustLog.initialiseLogging()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RustApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Addpage()
                }
            }
        }
    }
}

@Composable
fun Addpage() {
    var firstInput by remember { mutableStateOf("") }
    var secondInput by remember { mutableStateOf("") }
    var thirdInput by remember { mutableStateOf("") }
    var fourthInput by remember { mutableStateOf("") }
    var fifthInput by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    val history by remember { mutableStateOf(History(SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time)))}
    var showError by remember { mutableStateOf(false) } // Indicate error if the wrong input is received

    Column {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.size(14.dp))
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = firstInput,
                    onValueChange = { firstInput = it },
                    label = { Text("Enter the Exercise") },
                    isError = showError
                )
            }
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = secondInput,
                    onValueChange = { secondInput = it },
                    label = { Text("Enter Weight") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // accept only numbers
                    ),
                    isError = showError
                )
            }
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = thirdInput,
                    onValueChange = { thirdInput = it },
                    label = { Text("Enter Reps") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // accept only numbers
                    ),
                    isError = showError
                )
            }
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = fourthInput,
                    onValueChange = { fourthInput = it },
                    label = { Text("Enter Sets") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number // accept only numbers
                    ),
                    isError = showError
                )
            }
            Row {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = fifthInput,
                    onValueChange = { fifthInput = it },
                    label = { Text("Notes") },
                    isError = showError
                )
            }

            Spacer(modifier = Modifier.size(14.dp))
            Button(onClick = {
                val name = firstInput
                val weight = secondInput.toLongOrNull()
                val reps = thirdInput.toLongOrNull()
                val sets = fourthInput.toLongOrNull()
                val notes = fifthInput
                val date: String = SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time)
                if (name == null || weight == null || reps == null || sets == null) {
                    showError = true
                    return@Button
                }
                val entry = Entry(name, weight, reps, sets, notes, date)
                val exercise = Exercise(name,date)
                exercise.addEntry(entry)
                history.addExercise(exercise)
                output = history.logString
//                output = "$entry"
                showError = false
            }) {
                Text(text = "Generate")
            }
            Spacer(modifier = Modifier.size(14.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = output,
                onValueChange = { },
                label = { Text("Output") },
                readOnly = true
            )
        }
    }
}