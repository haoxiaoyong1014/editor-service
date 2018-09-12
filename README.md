# SpringBootMavenArchetype
this is a maven archetype , which will built a java program based on springboot

### how to use it 

* get the source code 

* generate generated-sources folder , by the following code:

```
	
	mvn archetype:create-from-project

``` 

* go to target/generated-sources/archetype , and make archetype installed to your local maven storage

```

	mvn install


```

* open the target/generated-sources/archetype/pom.xml , it should like this :

```

  <groupId>com.liumapp.SpringBootMavenArchetype</groupId>
  <artifactId>SpringBootMavenArchetype-archetype</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>maven-archetype</packaging>

```

* build your new maven project anywhere you like :

```
mvn archetype:generate -DarchetypeGroupId=com.liumapp.SpringBootMavenArchetype -DarchetypeArtifactId=SpringBootMavenArchetype-archetype -DarchetypeVersion=1.0-SNAPSHOT

```

   or you can just enter :
 
    mvn archetype:generate
 
   and then choice com.liumapp.SpringBootMavenArchetype like : 
   
   ![01](https://github.com/liumapp/imageFolder/blob/master/shell/archetype01.jpg)

* enjoy your coding with SpringBoot



