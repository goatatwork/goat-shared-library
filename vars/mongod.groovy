def bootstrap() {
    def script = this
    def ansibleParams = [
        venv: 'ansible_env',
        workingDir: '/path/to/ansible/playbook/directory',
        inventory: script.env.HOSTNAMES,
        playbook: 'site.yml',
        extraVars: "some_var=value",
    ]

    try {
        org.rxdb.AnsibleRunner.runCmd(script, ansibleParams)
    } catch (Exception e) {
        script.echo "Failed to execute Ansible playbook: ${e.getMessage()}"
        script.currentBuild.result = 'FAILURE'
        return // Stop the execution
    }
}
