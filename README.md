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

jdg-visualizer
========================

What is it?
-----------

This is a set of demo code that populates noticeable amount of data into Infinispan/JDG.  This can be used along w/ the Visualizer application to see data grid in action.  The Visualizer application can be found: https://github.com/infinispan/visual

System requirements
-------------------
 * JBoss Data Grid 6.0 or Infinispan
 * Maven 2

Configure Maven
---------------
If not using Infinispan, everything should be good to go.

If you are using JDG 6, please make sure JDG 6 repository is configured based on JDG 6 Maven Repository installation instructions.  Please change the `pom.xml` so that the Infinispan dependency is based on JDG 6 repository.

Configure the Demo
------------------------
All configurations are in code (sorry), located in `com.redhat.middleware.jdg.Main`.

### INITIAL_LIST
Set the `INITIAL_LIST` to at least one HotRod server.  See HotRod Client documentation equivalent of: http://docs.jboss.org/infinispan/5.1/apidocs/org/infinispan/client/hotrod/RemoteCacheManager.html#RemoteCacheManager(java.lang.String,%20boolean)

### CACHE_NAME
Set the `CACHE_NAME` to the name of the cache you want to use.  By default, JDG comes w/ "namedCache" configuration.  For Infinispan, try "___defaultcache".

### Demos to Run
If you don't want to run something, comment it out.  For `TwitterDemoClient`, you'll need to pass in a `Consumer Key` and a `Consumer Secret`.  If you don't have one, go to https://dev.twitter.com/apps and create a new application.

Build and Run the Application 
---------------------
 
To run, you can do `mvn exec:java`, or execute main class `com.redhat.middleware.jdg.Main`

Debug the Application
------------------------------------

If you want to debug the source code or look at the Javadocs of any library in the project, run either of the following commands to pull them into your local repository. The IDE should then detect them.

        mvn dependency:sources
        mvn dependency:resolve -Dclassifier=javadoc

