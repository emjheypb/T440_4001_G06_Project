package com.example.team6.models

class User(val name:String, val email: String, val password: String, val membership:String) {
    override fun toString(): String {
        return "user(name='$name', email='$email', password='$password', membership='$membership')"
    }

}