package com.blanks.connect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blanks.connect.data.Employee
import com.blanks.connect.databinding.ActivityMainBinding
import com.blanks.connect.external.ApiBuilder
import com.blanks.connect.presenter.EmployeeAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val client = ApiBuilder().getEmployeeService()

    private val adapter: EmployeeAdapter = EmployeeAdapter(arrayListOf(), object: EmployeeAdapter.Events {
        override fun onDelete(recipe: Employee) {
//
            CoroutineScope(Dispatchers.IO).launch {
                val body = HashMap<String, Any>().apply {
                    set("id", recipe.id)
                }

               val response = client.deleteEmployee(body).await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, response.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java))
        }

        binding.containerList.adapter = adapter
        binding.containerList.layoutManager = LinearLayoutManager(binding.containerList.context)

        CoroutineScope(Dispatchers.IO).launch {
            val response = getData()

            response.forEach {
                Log.i("INDO DATA : " , it.staf_alamat)
            }

            withContext(Dispatchers.Main) {
                adapter.dispatch(response)
            }
        }
    }

    private suspend fun getData(): ArrayList<Employee> {
        val response = client.getEmployees().await()

        if (response.data == null) return arrayListOf()
        return response.data
    }
}