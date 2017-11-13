# Namics Spring Terrific

`namics-spring-terrific` ist eine kleine Bibliothek, welche Terrific Integration in eine Spring basierte Applikation ermöglicht.
Das Kernstück ist der Filter, welcher die css und js zusammenzieht. Der Filter kann in verschiedene Spring basierte Applikationen integrierte werden z.B. Magnolia, hybris oder Spring MVC.
Die Integration basiert auf dem Namics Terrific Micro Ansatz. So ist es möglich, dass der Frontend-Engineer einen Apache auf den terrific Ordner konfiguriert und er unabhängig von der zu integrierenden Plattform arbeiten kann.

Weitere Details unter Verwendung.

## Issue Management

[NCMTERRIFIC](https://jira.namics.com/browse/NCMTERRIFIC)

## CI

[NAMICSJAVA-COMMONSTERRIFIC](http://builds.namics.com/browse/NAMICSJAVA-COMMONSTERRIFIC)

## Dependency Management

Die neueste Version ist jeweils im [Nexus][] zu finden:

[Nexus]: http://nexus.namics.com/index.html#nexus-search;gav~~namics-spring-terrific~~~

### Release
    ...
    <dependency>
      <groupId>com.namics.commons.terrific</groupId>
      <artifactId>namics-spring-terrific</artifactId>
      <version>2.4.1</version>
    </dependency>
    ...
    <repository>
      <id>nexus.namics.com</id>
      <url>http://nexus.namics.com/content/repositories/namics-common/</url>
    </repository>
    ...

### Snapshot
    ...
    <dependency>
      <groupId>com.namics.commons.terrific</groupId>
      <artifactId>namics-spring-terrific</artifactId>
      <version>2.4.2-SNAPSHOT</version>
    </dependency>
    ...
    <repository>
      <id>snapshot.namics.nexus.namics.com</id>
      <url>http://nexus.namics.com/content/repositories/namics-projects-snapshot/</url>
      <snapshots>true</snapshots>
    </repository>
    ...

## Verwendung

### Applikationstyp

Diese Library kann in jeglichen Spring basierten Java-Projekten verwendet werden, in welchen Terrific eingesetzt wird.

### Beispiel

Siehe Modul  `namics-spring-terrific-demo-web`

### Einbindung wichtigste Punkte

> Hier werden nur die wichtigsten Konfigurationen und Konvetionen erläutert.
  Detaillierte Konfigurations-Optionen sind in der Demo-Anwendung `namics-spring-terrific-demo-web` nachzuvollziehen.
  Für Hybris gibt es im [know.namics.com][hybris] eine detaillierte Anleitung.

[hybris]: https://know.namics.com/display/frontend/Terrific+Integration+mit+Spring+MVC+%28Java,+hybris,+etc.%29

Terrific Folder Structure ist der gemeinsame Nenner. Der Filter wird auf diese Ordnerstruktur konfiguriert und der Frontend Engineer kann die Sourcen von Terrific Micro in die gleiche Struktur einbinden. Zentral ist das config.json in dem die Konkatinierung für Terrific Micro und Spring Terrific definiert wird. Im Ordner views sind die Terrific Micro Views.


config.json:

    {
        "base": "classpath:/terrific",
        "scan": [
            "/assets/**",
            "/modules/**"
        ],
        "assets": {
            "delizio.css": [
                "reset.css",
                "+variables.less",
                "+variables-delizio.less",
                "+mixins.less",
                "!todo:removeforproduction:debug.css",
                "!-cremesso.*",
                "!/modules/Landing*/*",
                "*.css",
                "*.less"
            ]
             }
    }

Ein Terrific Modul hat im Bild dargestellten Aufbau. Die .html Dateien werden vom Frontend-Engineer für das arbeiten mit Terrific Micro benötigt. Je nach Platform sind für die Backendend HTML Integration .ftl im Falle von Magnolia oder in einer Spring MVC Applikation .jsp vorhanden.
Das HTML Markup muss somit organisatorisch in den Files .html mit den .ftl oder .jsp synchron gehalten werden.

Im Node "assets" können Files definiert werden, welche durch den Filter konkatiniert zur Verfügung gestellt werden. Files die nicht hier definiert werden, werden vom Filter direkt auf die Terrific Ordnerstruktur aufgelöst.

Mit dem + wird definiert, dass vor jedem File welches z.B. von less compiliert wird immer das variables.less angehängt. (Bei Less werden immer die einzelnen Files kompiliert, so kann im Fehlerfall, die Zeilennummer des Files ermittelt werden)
Mit dem ! können excludes definiert werden.

Terrific Filter einbinden:

    <filter>
        <display-name>Terrific Filter</display-name>
        <filter-name>terrificFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
        <async-supported>true</async-supported>
    </filter>
    <filter-mapping>
        <filter-name>terrificFilter</filter-name>
        <url-pattern>/terrific/*</url-pattern>
    </filter-mapping>

Spring Context:

    <bean class="com.namics.oss.spring.support.terrific.config.TerrificConfig"/>

Default Properties können überschrieben werden. z.B.

    terrific.mapping -> /terrific/
    terrific.files.config.json -> /WEB-INF/terrific/config.json
    terrific.minify -> true
