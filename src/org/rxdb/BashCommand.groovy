package org.rxdb

import hudson.model.TaskListener

class BashCommand {

    // Synchronously execute a bash command
    static String execute(script, String command) {
        // Execute shell command using sh step
        def result = script.sh(script: command, returnStdout: true).trim()
        return result
    }

    // Asynchronously execute a bash command and return a process
    static Process executeAsync(String command, TaskListener listener) {
        return sh(script: command, returnStatus: true, stdout: listener.logger, stderr: listener.errorLogger()).start()
    }

    static def runshell(script, args) {
        script.sh "script: ${args}, returnStdout: true"
    }

}
