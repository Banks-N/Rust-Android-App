package com.example.rustapplication

//import com.example.rustapplication.lib.Inputs
import android.content.Intent
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


class MainActivity : ComponentActivity() {

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
                    var firstInput by remember { mutableStateOf("") }
                    var secondInput by remember { mutableStateOf("") }
                    var thirdInput by remember { mutableStateOf("") }
                    var fourthInput by remember { mutableStateOf("") }
                    var fifthInput by remember { mutableStateOf("") }
                    var output by remember { mutableStateOf("") }
                    var history by remember { mutableStateOf(History(SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time),0))};
                    var empty by remember {mutableStateOf(true)};
                    var outputHistory by remember { mutableStateOf(History(SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time),1))};

                    val bundle = intent.getBundleExtra("myBundle")

                    if (bundle != null && empty) {
                        history = History(bundle!!.getString("History").toString())
                        empty = bundle!!.getBoolean("Empty")
//                        output = history.exerciseToString()
                    }
//                    output = history.exerciseToString()



//                    empty = this@MainActivity.intent.getBooleanExtra("Empty",empty)
//                    if (!this@MainActivity.intent.getBooleanExtra("Empty",true)) {
//                        history = History(this@MainActivity.intent.getStringExtra("History").toString());
////                        output = history.displayToString();
//                    }
//
//                    var temp_history = History(SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time),0);
//                    var empty by remember { mutableStateOf(this@MainActivity.intent.getBooleanExtra("Empty",true)) }
//                    if (!empty) {
//                        temp_history = History(this@MainActivity.intent.getStringExtra("History").toString());
//                    }


//
//                    var history by remember { mutableStateOf(History(SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time),0))}
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
                            Row {
                                Spacer(modifier = Modifier.size(14.dp))
                                Button(onClick = {
                                    if (empty) {
                                        val intent = Intent(this@MainActivity, AddActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        val intent = Intent(this@MainActivity, DisplayActivity::class.java)
//                                        intent.putExtra("History",history.toString())
//                                        intent.putExtra("Empty",this@MainActivity.intent.getBooleanExtra("Empty",empty));
                                        val bundle = Bundle()
                                        history.mergeHistory(outputHistory)
                                        bundle.putString("History",history.toString())
                                        bundle.putBoolean("Empty",this@MainActivity.intent.getBooleanExtra("Empty",empty))
                                        intent.putExtra("myBundle", bundle)
                                        startActivity(intent)
                                    }
                                }) {
                                    Text(text = "Back")
                                }
                                Spacer(modifier = Modifier.size(14.dp))

                                Spacer(modifier = Modifier.size(14.dp))
                                Button(onClick = {
                                    empty = false
                                    val name = firstInput
                                    val weight = secondInput.toLongOrNull()
                                    val reps = thirdInput.toLongOrNull()
                                    val sets = fourthInput.toLongOrNull()
                                    var notes = fifthInput
                                    val date: String = SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time)
                                    if (name == null || weight == null || reps == null || sets == null) {
                                        showError = true
                                        return@Button
                                    }
                                    if (notes == null) {
                                        notes = ""
                                    }
//                                    val entry = Entry(name, weight, reps, sets, notes, date)
//                                    val exercise = Exercise(name,date)
//                                    exercise.addEntry(entry)
//                                    history.addExercise(exercise)
                                    val outputEntry = Entry(name, weight, reps, sets, notes, date)
                                    val outputExercise = Exercise(name,date)
                                    outputExercise.addEntry(outputEntry)
                                    outputHistory.addExercise(outputExercise)
                                    output = outputHistory.exerciseToString()
//                                    output = history.exerciseToString()
//                                    output = "$entry"
                                    showError = false
                                }) {
                                    Text(text = "Add Entry")
                                }
                                Spacer(modifier = Modifier.size(14.dp))

                                Spacer(modifier = Modifier.size(14.dp))
                                Button(onClick = {
                                    outputHistory = History(SimpleDateFormat("MM-dd-yyyy").format(Calendar.getInstance().time),1)
                                    output = outputHistory.exerciseToString();
                                    empty = true;
                                }) {
                                    Text(text = "Clear")
                                }
                                Spacer(modifier = Modifier.size(14.dp))
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
            }
        }
    }
}

