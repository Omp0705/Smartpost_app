package com.om.smartpost.customer.home.domain.model

data class PackageItem(
    val trackingNumber: String,
    val senderOrSource: String, // e.g., "Amazon Seller", "Flipkart Warehouse"
    val status: String, // e.g., "Assigned", "Created", "Arriving Soon"
    val timeOrDate: String, // e.g., "Tomorrow, 10-12 AM", "Feb 16, 2-4 PM"
    val postmanName: String? = null // Null if not assigned/out for delivery yet
)
