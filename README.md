This is a gradle project which is used to test Scala, Java (jUnit or Spock), Spring Boot.

- SafariBooksOnlineApplication.java: a tiny REST service (Spring Boot) that receives whatever a greasemonkey script
                                     sends; another tiny REST service is used to tell the app to write everything it
                                     receives so far to files and reset its memory; springfox is used to provide REST API documentation in a Swagger style 
  + main/resources/static, main/resources/templates/index.html (shows how to get the
                                     context path with thymeleaf), main/resources/application.yaml go with this app.
- test/groovy/home/DataDrivenSpec.java shows some Spock features
  test/resources/META-INF/services/com.athaydes.spockframework.report...: to make Spock generate beautiful test reports
- test/java/home/FileRenamingTest.java: rename a bunch of files in 1 directory
  + AutoPart.java: a way to call methods on nullable instance using java 8 Optional
  + Animal, Tiger: fluent interface in inheritance
- test/java/home/SrtTest.java: modify a subtitle file
- test/java/home/TryAndErrorTest.java: shows the usefulness of Java 8 lambda syntactic sugar in using brute force to
  solve problems.
- test/java/home/ImageSplittingTest.java: I use phantomjs to render a big image of Google maps (about 4300px by 5300px). 
                                          The code is in test/resources/map.js. This test will print out the ImageMagick
                                          commands to cut that big image into smaller images that can be printed on 8x10
                                          photos at Walgreens or CVS.
- gulp-express-browsersync: a nodejs application. It uses gulp and browser-sync to automatically reload changes. It uses
                            express to write REST services. // TODO: need to call its test with gradle.