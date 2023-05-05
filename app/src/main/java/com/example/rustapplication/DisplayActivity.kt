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


class DisplayActivity : ComponentActivity() {

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
                    var history = ""
                    if (this@DisplayActivity.intent.getStringExtra("Empty") != "True") {
                        history = History(this@DisplayActivity.intent.getStringExtra("History").toString()).displayToString()
                    }

                    Column {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.size(14.dp))
                            Row {
                                Spacer(modifier = Modifier.size(14.dp))
                                Button(onClick = {
                                    val intent = Intent(this@DisplayActivity, MainActivity::class.java)

                                    startActivity(intent)
                                }) {
                                    Text(text = "Add Entry")
                                }

                                Spacer(modifier = Modifier.size(53.dp))
                                Button(onClick = {
                                    val intent = Intent(this@DisplayActivity, AddActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Text(text = "Clear")
                                }

                                Spacer(modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.size(14.dp))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row {
                                Spacer(modifier = Modifier.size(14.dp))
                                OutlinedTextField(
                                    value = history,
                                    onValueChange = { },
                                    label = { Text("History") },
                                    readOnly = true
                                )
                                Spacer(modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
