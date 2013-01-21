<!---
JBoss, Home of Professional Open Source
Copyright 2011 Red Hat Inc. and/or its affiliates and other
contributors as indicated by the @author tags. All rights reserved.
See the copyright.txt in the distribution for a full listing of
individual contributors.

This is free software; you can redistribute it and/or modify it
under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation; either version 2.1 of
the License, or (at your option) any later version.

This software is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this software; if not, write to the Free
Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
02110-1301 USA, or see the FSF site: http://www.fsf.org.
--->

hotrod-demo
========================

What is it?
-----------

This is a set of demo code that populates noticeable amount of data into Infinispan/JDG.  
This can be used along w/ the Visualizer application to see data grid in action.  
The Visualizer application can be found: <https://github.com/infinispan/visual/>

# 1. Running the Data Grid Loader

## 1.1 System requirements
 * JBoss Data Grid 6.0 or Infinispan
 * Maven 2

## 1.2 Running the Pre-built Demo
Assuming the project has already been build there should be a JAR that has the dependencies included in it.  Execute main class `com.redhat.middleware.jdg.Main`.  Modify the properties for your environment and preferences for loading the grid with data.

	java -cp hotrod-demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar -Djdg.demo.initialList=localhost:11223 -Djdg.demo.cacheName=labCache -Djdg.demo.maxEntries=100 -Djdg.demo.clearOnFinish=false -Djdg.demo.putDelay=0 -Djdg.demo.useTwitter=false com.redhat.middleware.jdg.Main

* **-Djdg.demo.initialList** must be set to at least one data grid node's hotrod interface. See HotRod Client documentation equivalent of: <http://docs.jboss.org/infinispan/5.1/apidocs/org/infinispan/client/hotrod/RemoteCacheManager.html#RemoteCacheManager(java.lang.String,%20boolean)/>
* **-Djdg.demo.cacheName** must be set to the name of the cache to load the data into. All nodes in the cluster must have this cache.
* **-Djdg.demo.maxEntries** is the count of data entries to load
* **-Djdg.demo.clearOnFinish** is set to `true` or `false` to evict all the cached entries one by one after all the data has been loaded.
* **-Djdg.demo.putDelay** is the amount of time in milliseconds to wait between puts to the cache.
* **-Djdg.demo.useTwitter**:`false` to use the basic CounterDemo. `true` to load the grid with Twitter entries. **-Djdg.demo.consumerKey** and **-Djdg.demo.consumerSecret** must be set to your twitter api credentials. If you don't have credentials, go to <https://dev.twitter.com/apps and create a new application/>.

# 2. Building

## 2.1 Configure Maven
If not using Infinispan, everything should be good to go.

If you are using JDG 6, please make sure JDG 6 repository is configured based on JDG 6 Maven 
Repository installation instructions.  Please change the `pom.xml` so that the Infinispan 
dependency is based on JDG 6 repository.

## 2.2 Building and Running from Source 
 
	mvn clean package
	mvn exec:java -Djdg.demo.initialList=192.168.1.115:11223 -Djdg.demo.cacheName=labCache -Djdg.demo.maxEntries=1000 -Djdg.demo.clearOnFinish=false -Djdg.demo.putDelay=5 -Djdg.demo.useTwitter=false

* **-Djdg.demo.initialList** must be set to at least one data grid node's hotrod interface. See HotRod Client documentation equivalent of: <http://docs.jboss.org/infinispan/5.1/apidocs/org/infinispan/client/hotrod/RemoteCacheManager.html#RemoteCacheManager(java.lang.String,%20boolean)/>
* **-Djdg.demo.cacheName** must be set to the name of the cache to load the data into. All nodes in the cluster must have this cache.
* **-Djdg.demo.maxEntries** is the count of data entries to load
* **-Djdg.demo.clearOnFinish** is set to `true` or `false` to evict all the cached entries one by one after all the data has been loaded.
* **-Djdg.demo.putDelay** is the amount of time in milliseconds to wait between puts to the cache.
* **-Djdg.demo.useTwitter**:`false` to use the basic CounterDemo. `true` to load the grid with Twitter entries. **-Djdg.demo.consumerKey** and **-Djdg.demo.consumerSecret** must be set to your twitter api credentials. If you don't have credentials, go to <https://dev.twitter.com/apps and create a new application/>.

# 3 Debugging

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

	mvn dependency:sources
	mvn dependency:resolve -Dclassifier=javadoc

