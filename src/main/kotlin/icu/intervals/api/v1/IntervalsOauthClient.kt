package icu.intervals.api.v1

import icu.intervals.api.v1.dto.AuthorizationCodeRequest
import icu.intervals.api.v1.dto.AuthorizationCodeResponse
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.BeanParam
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "intervals")
@Path("/api/oauth")
interface IntervalsOauthClient {
    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun token(
        @BeanParam form: AuthorizationCodeRequest
    ): Uni<AuthorizationCodeResponse>

}