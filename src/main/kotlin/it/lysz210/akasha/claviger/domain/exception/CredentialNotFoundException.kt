package it.lysz210.akasha.claviger.domain.exception

import it.lysz210.akasha.claviger.domain.model.Key

class CredentialNotFoundException (key: Key):
    RuntimeException("Active Credentials could not be found for key: ${key.qualifiedId}")