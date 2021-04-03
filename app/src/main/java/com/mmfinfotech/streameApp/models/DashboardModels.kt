package com.mmfinfotech.streameApp.models

data class LatestClipResponse(val status: String?, val message: String?, val record: LatestClipRecord?)

data class LatestClipRecord(val data: List<Clips>?, val currentPage: Int?,
                  val last_page: Int?, val total_record: Int?, val per_page: Int?)