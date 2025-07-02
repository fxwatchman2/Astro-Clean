del "%USERPROFILE%\.m2\repository\com\kalwit\astro-services\1.0\astro-services-1.0.jar"
copy \jars\astro-1.0.jar lib\astro-1.0.jar
mvn install:install-file -Dfile=lib/astro-1.0.jar -DgroupId=com.kalwit -DartifactId=astro-services -Dversion=1.0 -Dpackaging=jar
mvn clean package spring-boot:repackage