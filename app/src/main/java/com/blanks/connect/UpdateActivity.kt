package com.blanks.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blanks.connect.data.Employee
import com.blanks.connect.data.EmptyResponse
import com.blanks.connect.databinding.ActivityMainBinding
import com.blanks.connect.databinding.ActivityUpdateBinding
import com.blanks.connect.external.ApiBuilder
import com.blanks.connect.service.EmployeeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class UpdateActivity : AppCompatActivity() {

    private val clientService: EmployeeService = ApiBuilder().getEmployeeService()
    private lateinit var binding: ActivityUpdateBinding
    private var employee: Employee? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareForUpdate()

        binding.btnAction.setOnClickListener {
            val ctx = this

            val body = HashMap<String, Any>().apply {
                set("name", binding.etName.text.toString())
                set("alamat", binding.etAddress.text.toString())
                set("hp", binding.etPhone.text.toString())
            }

            if (employee != null) {
                body.set("id", employee!!.id)

            }
                lifecycleScope.launch {
                    val message = fetchToAPI(body)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
                        startActivity(Intent(ctx, MainActivity::class.java))
                    }
            }
        }
    }

    fun prepareForUpdate() {
        val ctx = this
        val id = intent.getStringExtra("id")

        CoroutineScope(Dispatchers.IO).launch {
            val body = HashMap<String, Any>().apply {
                set("id", id!!)
            }

            val data = clientService.getEmployee(body).await()

            employee = data.data.get(0)

            if (employee != null) {
                withContext(Dispatchers.Main) {
                    binding.etName.setText(employee?.staf_name)
                    binding.etPhone.setText(employee?.staf_hp)
                    binding.etAddress.setText(employee?.staf_alamat)
                }
            }
        }
    }

    private suspend fun fetchToAPI(body: HashMap<String, Any>): String {
        val response: EmptyResponse

        if (body.get("id") != null) {
            response = clientService.updateStaff(body).await()
        } else {
            response = clientService.createEmployee(body).await()
        }

        return response.message
    }
}

