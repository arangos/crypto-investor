package com.codebulls.criptoinvestor.configuration

import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import io.github.cdimascio.dotenv.DotenvBuilder

@Configuration
class VariablesConfig {

   @PostConstruct
   fun init(){
       val variables = DotenvBuilder().systemProperties()
           .ignoreIfMalformed()
           .ignoreIfMissing()
       variables.load()
   }

}