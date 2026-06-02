package it.lysz210.akasha.claviger.api

import io.smallrye.mutiny.Uni
import it.lysz210.akasha.claviger.api.mapper.Mapper
import it.lysz210.akasha.claviger.domain.IntervalsOauthService
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.UriInfo
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI

@Path("/intervals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class IntervalsResource (
    private val authService: IntervalsOauthService,
    private val mapper: Mapper
) {

    @GET
    @Path("")
    fun info() = authService.credential.map { mapper.toDto(it) }

    @GET
    @Path("/url/callback")
    fun getUrlCallback(@Context uriInfo: UriInfo): String =
        uriInfo.resourceUri(IntervalsResource::class.java, "callback").toString()

    @GET
    @Path("/login")
    fun login(@Context uriInfo: UriInfo): Uni<RestResponse<URI>> = this.authService.getAuthorizeUri(
            uriInfo.resourceUri(IntervalsResource::class.java, "callback")
        ).onItem().ifNull().continueWith { uriInfo.resourceUri(IntervalsResource::class.java) }
        .map(RestResponse<URI>::seeOther)

    @GET
    @Path("/callback")
    fun callback(
        @QueryParam("code") code: String,
        @Context uriInfo: UriInfo,
    ): Uni<RestResponse<URI>> = this.authService.exchangeCodeForAccessToken(code)
        .map{ uriInfo.resourceUri(IntervalsResource::class.java) }
        .map(RestResponse<URI>::seeOther)

    @GET
    @Path("/revoke")
    fun revoke(): Uni<String> = this.authService.revoke()

}

fun UriInfo.resourceUri(resource: Class<*>, method: String? = null): URI {
    val builder = this.baseUriBuilder.path(resource)
    if (method != null) {
        builder.path(resource, method)
    }
    return builder.build()
}