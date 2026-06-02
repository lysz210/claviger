package icu.intervals.api.v1.dto

import org.jboss.resteasy.reactive.RestForm

class AuthorizationCodeRequest(
    @field:RestForm("client_id") val clientId: String,
    @field:RestForm("client_secret") val clientSecret: String,
    @field:RestForm("code") val code: String,
) {
    @RestForm("grant_type")
    val grantType: String = "authorization_code"
}