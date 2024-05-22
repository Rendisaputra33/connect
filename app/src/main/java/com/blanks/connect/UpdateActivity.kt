package com.blanks.connect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blanks.connect.databinding.ActivityMainBinding
import com.blanks.connect.databinding.ActivityUpdateBinding
import com.blanks.connect.external.ApiBuilder
import com.blanks.connect.service.EmployeeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class UpdateActivity : AppCompatActivity() {

    private val clientService: EmployeeService = ApiBuilder().getEmployeeService()
    private lateinit var binding: ActivityUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAction.setOnClickListener {
            val ctx = this

            val body = HashMap<String, Any>().apply {
                set("name", binding.etName.text.toString())
                set("alamat", binding.etAddress.text.toString())
                set("hp", binding.etPhone.text.toString())
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

    private suspend fun fetchToAPI(body: HashMap<String, Any>): String {
        val response = clientService.createEmployee(body).await()
        return response.message
    }
}

