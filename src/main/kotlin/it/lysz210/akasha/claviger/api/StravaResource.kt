package it.lysz210.akasha.claviger.api

import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.domain.StravaOauthService
import it.lysz210.akasha.claviger.domain.model.Key
import jakarta.ws.rs.*
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
    fun info(): Uni<Key> = stravaAuthService.key

    @GET
    @Path("/token")
    fun token() = stravaAuthService.credential

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