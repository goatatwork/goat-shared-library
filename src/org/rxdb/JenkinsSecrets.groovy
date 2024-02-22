package org.rxdb

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.CredentialsMatchers
import com.cloudbees.plugins.credentials.CredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.domains.DomainRequirement
import hudson.security.ACL
import jenkins.model.Jenkins

class JenkinsSecrets {

    static String getSecret(String credentialId) {
        def jenkins = Jenkins.getInstanceOrNull()
        if (!jenkins) {
            throw new IllegalStateException("Jenkins instance not available")
        }

        def domain = Domain.global()
        def domainRequirements = new ArrayList<DomainRequirement>()

        def credentials = CredentialsProvider.lookupCredentials(
            Credentials.class, jenkins, ACL.SYSTEM, domainRequirements
        )

        def credential = CredentialsMatchers.firstOrNull(
            credentials, CredentialsMatchers.withId(credentialId)
        )

        if (!credential) {
            throw new IllegalArgumentException("Credential with ID '$credentialId' not found")
        }

        if (credential instanceof com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl) {
            return credential.password
        } else if (credential instanceof org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl) {
            return credential.secret?.getPlainText() ?: ''
        } else {
            throw new IllegalArgumentException("Unsupported credential type: ${credential.class}")
        }
    }
}
