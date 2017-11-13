# spring-terrific-support-starter

Spring-Terrific-Support module can be configured using Auto-Configuration. This document provides a basic overview on how to utilize the spring-terrific-support-starter. Detailed information on how to work with the starter may be observed in the spring-terrific-support-samples-starter project.

## Step 1: Add the required dependencies

Add the dependency for the module itself (i.e. spring-terrific-support) and the corresponding starter module (i.e. spring-terrific-support-starter) which is responsible for the auto-configuration of the module.

    <dependency>
        <groupId>com.namics.oss.spring.support.terrific</groupId>
        <artifactId>spring-terrific-support-starter</artifactId>
        <version>1.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.namics.oss.spring.support.terrific</groupId>
        <artifactId>spring-terrific-support</artifactId>
        <version>1.0.0</version>
    </dependency>

### Configuration of the web interface
The starter allows you to override the default settings for the terrific filter.

    # Some samples properties for spring-terrific-support
    terrific.config=classpath:/terrific/config.json
    terrific.debug=false
