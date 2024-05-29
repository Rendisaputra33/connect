package com.blanks.connect

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blanks.connect.data.Employee
import com.blanks.connect.data.ResponseData
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
            val body = HashMap<String, Any>().apply {
                set("id", recipe.id)
            }

            CoroutineScope(Dispatchers.IO).launch {
               val response = client.deleteEmployee(body).await()

                if (!response.status.lowercase().contains("gagal")) {
                    val data = client.getEmployees().await()

                    withContext(Dispatchers.Main) {
                        fetch(data)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, response.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onClickEdit(recipe: Employee) {
            val intent = Intent(this@MainActivity, UpdateActivity::class.java)
            intent.putExtra("id", recipe.id)

            startActivity(intent)
        }
    })

    fun fetch(response: ResponseData) {
        if (response.data == null) {
            adapter.dispatch(arrayListOf())
        } else {
            adapter.dispatch(response.data.toCollection(ArrayList<Employee>()))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = this

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createButton.setOnClickListener {
            startActivity(Intent(this, UpdateActivity::class.java))
        }

        binding.containerList.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val response = getData()

            withContext(Dispatchers.Main) {
                adapter.dispatch(response.toCollection(ArrayList<Employee>()))
            }
        }
    }

    private suspend fun getData(): List<Employee> {
        val response = client.getEmployees().await()

        if (response.data == null) return arrayListOf()
        return response.data
    }
}