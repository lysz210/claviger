package it.lysz210.akasha.claviger.strava.client.v3

import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.strava.client.v3.dto.AuthorizationCodeRequest
import it.lysz210.akasha.claviger.strava.client.v3.dto.AuthorizationCodeResponse
import it.lysz210.akasha.claviger.strava.client.v3.dto.AuthorizationRefreshRequest
import it.lysz210.akasha.claviger.strava.client.v3.dto.TokenInfo
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "strava-v3")
@Path("/api/v3/oauth")
interface StravaOauthV3 {

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun token(
        @BeanParam form: AuthorizationCodeRequest
    ): Uni<AuthorizationCodeResponse>

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun refresh(
        @BeanParam form: AuthorizationRefreshRequest
    ): Uni<TokenInfo>
}