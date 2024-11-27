package com.fgfbrands.myapplication.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the complete response for a user fetched from the API.
 *
 * Key Highlights:
 * - Captures all fields in the user API response for flexibility and future use.
 */
data class UserResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("firstName")
    val firstName: String = "Unknown",
    @SerializedName("lastName")
    val lastName: String = "User",
    @SerializedName("maidenName")
    val maidenName: String? = null,
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("username")
    val username: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("bloodGroup")
    val bloodGroup: String? = null,
    @SerializedName("height")
    val height: Double? = null,
    @SerializedName("weight")
    val weight: Double? = null,
    @SerializedName("eyeColor")
    val eyeColor: String? = null,
    @SerializedName("hair")
    val hair: Hair? = null,
    @SerializedName("address")
    val address: Address? = null,
    @SerializedName("company")
    val company: Company? = null,
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("birthDate")
    val birthDate: String? = null,
)

data class Hair(
    @SerializedName("color")
    val color: String,
    @SerializedName("type")
    val type: String
)

data class Address(
    @SerializedName("address")
    val address: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("stateCode")
    val stateCode: String,
    @SerializedName("postalCode")
    val postalCode: String,
    @SerializedName("coordinates")
    val coordinates: Coordinates,
    @SerializedName("country")
    val country: String
)

data class Coordinates(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Company(
    @SerializedName("department")
    val department: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("address")
    val address: Address
)
