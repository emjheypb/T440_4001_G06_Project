package com.example.team6

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.team6.databinding.ActivityLoginBinding
import com.example.team6.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.binding.root)
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        this.prefEditor = this.sharedPreferences.edit()
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
            val typeToken = object : TypeToken<List<User>>(){}.type
            val userList = gson.fromJson<List<User>>(userListFromSP, typeToken)
            var flag=false
            for(u in userList){
                if(u.email==email) {
                    flag=true
                    if (u.password == password) {
                        prefEditor.putBoolean("IS_LOGGED_IN", true)
                        val gson = Gson()
                        val loggedInUser = gson.toJson(u)
                        prefEditor.putString("LOGGED_IN_USER", loggedInUser)
                        prefEditor.apply()
                        val snackbar  = Snackbar.make(binding.root, "LOGIN SUCCESSFUL", Snackbar.LENGTH_SHORT)
                        snackbar.show()
                        Handler().postDelayed(Runnable { finish() }, 1000)
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