# Advanced Random Test Data Utils

System        | Status
--------------|------------------------------------------------        
CI master     | [![Build Status][travis-master]][travis-url]
CI develop    | [![Build Status][travis-develop]][travis-url]
Dependency    | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.namics.oss.spring.support.terrific/spring-terrific-support/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.namics.oss.spring.support.terrific/spring-terrific-support)

Library to support the terrific integration in a spring mvc web applications.

## Usage

### Known Issues

The minification feature of the module does not work in this release! 

### Maven Dependency (Latest Version in `pom.xml`):

	<dependency>
		<groupId>com.namics.oss.spring.support.terrific</groupId>
		<artifactId>spring-terrific-support</artifactId>
		<version>1.0.0</version>
	</dependency>
	
### Requirements	

Java: JDK 8            	 

### Integration

The user interface can be integrated with the spring boot starter.

	<dependency>
		<groupId>com.namics.oss.spring.support.terrific</groupId>
		<artifactId>spring-terrific-support-starter</artifactId>
		<version>1.0.0</version>
	</dependency>
	
Other samples are in the module spring-terrific-support-samples.
You have always to integrate the terrific filter.


[travis-master]: https://travis-ci.org/namics/spring-terrific-support.svg?branch=master
[travis-develop]: https://travis-ci.org/namics/spring-terrific-support.svg?branch=develop
[travis-url]: https://travis-ci.org/namics/spring-terrific-support
