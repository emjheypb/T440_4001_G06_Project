package com.example.team6

import android.R
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.team6.databinding.ActivityRegisterBinding
import com.example.team6.models.user
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    val membershipList: List<String> = listOf("Tenant", "Landlord")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.simple_spinner_item, membershipList)
        binding.spinnerMembership.adapter=adapter
        this.binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        this.binding.tvError.setText("")
        val name = this.binding.etName.text.toString()
        val email = this.binding.etEmail.text.toString()
        val password = this.binding.etPassword.text.toString()
        val membership = membershipList.get(this.binding.spinnerMembership.selectedItemPosition)
        if(name.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || membership.isNullOrEmpty()){
            this.binding.tvError.setText("Error: All fields must be filled in!")
            return@registerUser
        }
        val newUser = user(name, email, password, membership)
        val userListFromSP = sharedPreferences.getString("USER_LIST", "")
        if (userListFromSP != "") {
            // convert the string back into a fruit object
            val gson = Gson()
            val typeToken = object : TypeToken<List<user>>(){}.type
            val userList = gson.fromJson<MutableList<user>>(userListFromSP, typeToken)
            userList.add(newUser)
            val listAsString = gson.toJson(userList)
            this.prefEditor.putString("USER_LIST", listAsString)
            this.prefEditor.apply()
        }
        else{
            val userList:MutableList<user> = mutableListOf(newUser)
            val gson = Gson()
            val listAsString = gson.toJson(userList)
            this.prefEditor.putString("USER_LIST", listAsString)
            this.prefEditor.apply()
        }
        this.binding.tvError.setText("USER REGISTERED SUCCESSFULLY")
    }
}