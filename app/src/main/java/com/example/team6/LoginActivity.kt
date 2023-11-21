package com.example.team6

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.team6.R
import com.example.team6.databinding.ActivityLoginBinding
import com.example.team6.models.user
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.binding.btnLogin.setOnClickListener(this)
        this.binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_login->{
                this.login()
            }
            R.id.btn_register->{
                var registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(registerIntent)
            }
        }
    }

    private fun login(){
        this.binding.tvError.setText("")
        val email = this.binding.etEmail.text.toString()
        val password = this.binding.etPassword.text.toString()
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            this.binding.tvError.setText("Error: All fields must be filled in!")
            return@login
        }
        val userListFromSP = sharedPreferences.getString("USER_LIST", "")
        if(userListFromSP!=""){
            val gson = Gson()
            val typeToken = object : TypeToken<List<user>>(){}.type
            val userList = gson.fromJson<List<user>>(userListFromSP, typeToken)
            var flag=false
            for(u in userList){
                if(u.email==email) {
                    flag=true
                    if (u.password == password) {
                        this.binding.tvError.setText("LOGIN SUCCESSFUL")
                        return@login
                    } else {
                        this.binding.tvError.setText("Error: Password entered is incorrect")
                        return@login
                    }
                }
            }
            if(!flag){
                this.binding.tvError.setText("Error: User does not exist. Please register first to continue")
                return@login
            }
        }
        else{
            this.binding.tvError.setText("Error: User does not exist. Please register first to continue")
            return@login
        }
    }
}