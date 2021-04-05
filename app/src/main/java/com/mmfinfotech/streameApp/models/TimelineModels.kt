package com.mmfinfotech.streameApp.models

data class CommentResponse(val status: String?, val message: String?, val record: CommentRecord?)

data class CommentData(val user_id: Int?, val refrence_id: String?,
                       val type: String?, val comment: String?, val owner_id: Int?,
                       val status: String?, val added_on: String?, val update_on: String?,
                       val id: Int?, val user_name: String?, val profile_status: String?, val user_profile: String?)

data class CommentRecord(val data: List<CommentData>?)