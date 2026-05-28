package it.lysz210.akasha.claviger.api.mapper

import it.lysz210.akasha.claviger.api.dto.ErrorResponse
import it.lysz210.akasha.claviger.domain.exception.CredentialNotFoundException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

@Provider
class DomainExceptionMapper {

    @ServerExceptionMapper
    fun mapCredetialNotFound(ex: CredentialNotFoundException): RestResponse<ErrorResponse> {
        val payload = ErrorResponse(
            message = ex.message ?: "Resource not found",
            code = "CLAVIGER_CREDENTIALS_MISSING"
        )
        return RestResponse.status(
            Response.Status.NOT_FOUND,
            payload
        )
    }
}