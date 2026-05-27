package it.lysz210.akasha.claviger.strava

import io.smallrye.mutiny.Uni
import com.strava.api.v3.dto.Athlete
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.NotAuthorizedException
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.UriInfo
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI

@Path("/strava")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class StravaResource (
    private val stravaAuthService: StravaOauthService
) {

    @GET
    fun info(): Athlete = stravaAuthService.athlete
        ?: throw NotAuthorizedException("Strava")

    @GET
    @Path("/token")
    fun token() = stravaAuthService.token

    @GET
    @Path("/login")
    fun login(@Context uriInfo: UriInfo): Uni<RestResponse<URI>> = this.stravaAuthService.getAuthorizeUri(
            uriInfo.resourceUri(StravaResource::class.java, "callback")
        ).onItem().ifNull().continueWith { uriInfo.resourceUri(StravaResource::class.java) }
        .map(RestResponse<URI>::seeOther)

    @GET
    @Path("/callback")
    fun callback(
        @QueryParam("code") code: String,
        @Context uriInfo: UriInfo,
    ): Uni<RestResponse<URI>> = this.stravaAuthService.exchangeCodeForAccessToken(code)
        .map{ uriInfo.resourceUri(StravaResource::class.java) }
        .map(RestResponse<URI>::seeOther)

    @GET
    @Path("/revoke")
    fun revoke(): Uni<String> = this.stravaAuthService.revoke()

}

fun UriInfo.resourceUri(resource: Class<*>, method: String? = null): URI {
    val builder = this.baseUriBuilder.path(resource)
    if (method != null) {
        builder.path(resource, method)
    }
    return builder.build()
}