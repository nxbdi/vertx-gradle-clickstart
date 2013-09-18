# Welcome to Vert.x on CloudBees

This is a "ClickStart" that gets you going with a Gradle - Vert.x 2"seed" project starting point. You can launch it here:

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/CloudBees-community/vertx-gradle-clickstart/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

This will setup a continuous deployment pipeline - a CloudBees Git repository, a Jenkins build compiling and running the test suite (on each commit).
Should the build succeed, this seed app is deployed on a Tomcat 7 container.

# CloudBees Vert.x container

To directly deploy a vert.x module:

```
$ bees app:deploy -a APP_ID -t java \
   -RPLUGIN.SRC.java=https://community.ci.cloudbees.com/job/vertx-clickstack/lastSuccessfulBuild/artifact/build/distributions/vertx2-clickstack-1.0.0-SNAPSHOT.zip \
   path/to/module.zip
```

Please don't remove `-t java` as long as `-t vertx2` has not been setup by CloudBees engineering team.