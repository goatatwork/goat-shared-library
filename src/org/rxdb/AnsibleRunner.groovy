package org.rxdb

import java.io.BufferedReader
import java.io.InputStreamReader

class AnsibleRunner {

    static void runCmd(script, Map params) {
        def virtualEnvPath = "/home/ansible/.virtualenvs/${params.venv}" // Specify the path to your virtual environment

        // def pythonInterpreter = "${virtualEnvPath}/bin/python" // Path to the Python interpreter within the virtual environment
        // def activateScript = "${virtualEnvPath}/bin/activate"

        def command = "source ${virtualEnvPath}/bin/activate && cd ${params.workingDir} && ansible-playbook"

        def argsList = [
            "-i", params.inventory.toString(), // Convert GString to string explicitly
            params.playbook.toString() // Convert GString to string explicitly
        ]

        if (params.limit) {
            argsList.add("-l")
            argsList.add(params.limit.toString()) // Convert GString to string explicitly
        }

        if (params.extraVars) {
            argsList.add("-e")
            argsList.add(params.extraVars.toString()) // Convert GString to string explicitly
        }

        if (params.vaultPasswordFile) {
            argsList.add("--vault-password-file")
            argsList.add(params.vaultPasswordFile.toString()) // Convert GString to string explicitly
        }

        if (params.listhost) {
            argsList.add("--list-hosts")
        }

        script.echo("Ansible Running: ansible-playbook ${argsList.join(' ')}")

        return BashCommand.execute(script, "${command} ${argsList.join(' ')}")
    }

    static void runPlaybook(Map params) {

        def virtualEnvPath = "/home/ansible/.virtualenvs/${params.venv}" // Specify the path to your virtual environment

        // def pythonInterpreter = "${virtualEnvPath}/bin/python" // Path to the Python interpreter within the virtual environment
        // def activateScript = "${virtualEnvPath}/bin/activate"

        def command = "source ${virtualEnvPath}/bin/activate && cd ${params.workingDir} && ansible-playbook"

        // def command = "${virtualEnvPath}/bin/ansible-playbook"
        def argsList = [
            "-i", params.inventory.toString(), // Convert GString to string explicitly
            params.playbook.toString() // Convert GString to string explicitly
        ]

        if (params.limit) {
            argsList.add("-l")
            argsList.add(params.limit.toString()) // Convert GString to string explicitly
        }

        if (params.extraVars) {
            argsList.add("-e")
            argsList.add(params.extraVars.toString()) // Convert GString to string explicitly
        }

        if (params.vaultPasswordFile) {
            argsList.add("--vault-password-file")
            argsList.add(params.vaultPasswordFile.toString()) // Convert GString to string explicitly
        }

        if (params.listhost) {
            argsList.add("--list-hosts")
        }

        def processBuilder = new ProcessBuilder("/bin/bash", "-c", "${command} ${argsList.join(' ')}")
        processBuilder.redirectErrorStream(true)
        // processBuilder.directory(new File(params.workingDir)) // Set the working directory


        // Print the command being executed (optional)
        println("Executing command: ${processBuilder.command().join(' ')}")


        // Execute the command
        def process = processBuilder.start()

        // Read the output of the command
        def output = new StringBuilder()
        def reader = new BufferedReader(new InputStreamReader(process.inputStream))
        String line
        while ((line = reader.readLine()) != null) {
            output.append(line).append('\n')
            println(line) // Print each line as it is read
        }


        // Wait for the process to finish and get the exit code
        process.waitFor()

        int exitCode = process.exitValue()
        // Get the output of the command
        // def output = process.text

        // Print the output of the command
        println("Ansible output:")
        println(output.toString())


        // Check if the command was successful
        if (exitCode == 0) {
            println("Ansible executed successfully.")
        } else {
            println("Ansible failed with exit code: $exitCode")
        }
    }
}
