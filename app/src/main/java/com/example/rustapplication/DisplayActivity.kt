package com.example.rustapplication

//import com.example.rustapplication.lib.Inputs
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rustapplication.lib.History
import com.example.rustapplication.lib.RustLog
import com.example.rustapplication.ui.theme.RustApplicationTheme
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
                    var history = History("Date",0);
                    val bundle = intent.getBundleExtra("myBundle")
                    var empty = true;
                    var output = ""

                    if (bundle != null) {
                        history = History(bundle!!.getString("History").toString())
                        empty = bundle!!.getBoolean("Empty")
                        output = history.displayToString()
                    }

//                    var empty = this@DisplayActivity.intent.getBooleanExtra("Empty",true);
//                    var output = ""
//                    if (!empty) {
//                        history = History(this@DisplayActivity.intent.getStringExtra("History").toString());
//                        output = history.displayToString();
//                    }

                    Column {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.size(14.dp))
                            Row {
                                Spacer(modifier = Modifier.size(14.dp))
                                Button(onClick = {
                                    if (empty) {
                                        val intent = Intent(this@DisplayActivity, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        val intent = Intent(this@DisplayActivity, MainActivity::class.java)

                                        val bundle = Bundle()
                                        bundle.putString("History",history.toString())
                                        bundle.putBoolean("Empty",empty)
                                        intent.putExtra("myBundle", bundle)
//                                        intent.putExtra("History",history.toString());
//                                        intent.putExtra("Empty",empty);
                                        startActivity(intent)
                                    }

                                }) {
                                    Text(text = "Add Entry")
                                }

                                Spacer(modifier = Modifier.size(53.dp))
                                Button(onClick = {
                                    if (empty) {
                                        val intent = Intent(this@DisplayActivity, AddActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        val intent = Intent(this@DisplayActivity, DisplayActivity::class.java)
                                        startActivity(intent)
                                    }
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
                                    value = output,
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
