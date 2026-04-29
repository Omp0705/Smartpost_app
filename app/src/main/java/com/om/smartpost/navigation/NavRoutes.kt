package com.om.smartpost.navigation

object NavRoutes {
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD = "forgot_pass"
    const val HOME = "home" // Generic home, maybe unused now?
    const val CUSTOMER_HOME = "customer_home"
    const val POSTMAN_HOME = "postman_home"

    const val AUTH = "auth"
    const val MAIN = "main"

    // Customer Routes
    const val CUSTOMER_PARCEL = "customer_parcel"
    const val CUSTOMER_PARCEL_DETAILS = "customer_parcel_details/{parcelId}"
    const val CUSTOMER_SEND = "customer_send"
    const val CUSTOMER_SCHEDULE = "customer_schedule"
    const val CUSTOMER_PROFILE = "customer_profile"
    const val CUSTOMER_NOTIFICATIONS = "customer_notifications"

    // Postman Routes
    const val POSTMAN_PARCELS = "postman_parcels"
    const val POSTMAN_ROUTE_TAB = "postman_route_tab"
    const val POSTMAN_PROFILE = "postman_profile"
    const val POSTMAN_PARCEL_DETAILS = "postman_parcel_details/{parcelId}"
}