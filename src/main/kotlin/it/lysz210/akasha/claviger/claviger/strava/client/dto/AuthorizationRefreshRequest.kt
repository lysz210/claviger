package it.lysz210.akasha.claviger.claviger.strava.client.dto

import org.jboss.resteasy.reactive.RestForm

class AuthorizationRefreshRequest(
    @field:RestForm("client_id") val clientId: String,
    @field:RestForm("client_secret") val clientSecret: String,
    @field:RestForm("refresh_token") val refreshToken: String,
) {
    @RestForm("grant_type")
    val grantType: String = "refresh_token"
}