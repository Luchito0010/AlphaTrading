package com.mb.neox.com.models

data class LoginRequest(
    val CompanyDB: String,
    val Password: String,
    val UserName: String
)