package com.mmfinfotech.streameApp.models

data class LoginResponse(val message: String?, val record: LoginRecord?, val status: String?)

data class LoginRecord(val id: String?, val name: String?, val username: String?, val email: String?,
                       val gender: String?, val status: String?, val created_at: String?, val profile: String?, val token: String?)

data class SignUpResponse(val message: String?, val record: SignUpRecord?, val status: String?)

data class SignUpRecord(val id: String?, val name: String?, val username: String?, val email: String?, val gender: String?,
                  val status: String?, val created_at: String?, val profile: String?, val token: String?)