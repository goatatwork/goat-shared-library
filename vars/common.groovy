def validateInput(input) {
   def hostnames = input.split(",")
   def domain = hostnames[0].split("\\.")[1..-1].join(".")  // Extract domain from first hostname
   def name = hostnames[0][0..3]
  
   if (hostnames.every { it.startsWith(name) && it.endsWith(domain) }) {
        if (hostnames.every { it.contains("apb") && it.contains("clus") }) {
            echo  "All hostnames are in the same domain (${domain}), start with 'iad', and contain 'apb' and 'clus'."
            env.CUSTOMER = 'BRAZE'
            env.ROLE = 'MONGOD'
            env.NAME = name
            env.DOMAIN = domain
            env.ISABORT = false
        } else if (hostnames.every { !it.contains("apb") && it.contains("clus") }) {
            echo  "Hostnames are with 'clus' and with same domain."
            env.CUSTOMER = 'MT'
            env.ROLE = 'MONGOD'
            env.NAME = name
            env.DOMAIN = domain
            env.ISABORT = false
        } else {
            echo ("Hostnames contains apb and nonapb")
            env.ISABORT = true        
        }
    } else {
        echo  "Not all hostnames start with 'iad'."
        env.ISABORT = true
   }

}
