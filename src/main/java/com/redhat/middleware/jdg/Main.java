/*
* JBoss, Home of Professional Open Source
* Copyright 2011 Red Hat Inc. and/or its affiliates and other
* contributors as indicated by the @author tags. All rights reserved.
* See the copyright.txt in the distribution for a full listing of
* individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package com.redhat.middleware.jdg;

import java.util.Set;
import java.util.logging.Logger;

import org.infinispan.api.BasicCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.logging.Logger.Level;

/**
 * Main demo launcher
 * 
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 */
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getName());

	/**
	 * Default initial list of JDG servers to connect to to for the demo.
	 * Format port is colon delimited and address:port pairs are semicolon delimited
	 * 	"<server1>:<port1>;<server2>:<port2>"
	 */
	private static final String DEFAULT_INITIAL_LIST = "127.0.0.1:11222";
	
	/**
	 * Default name of the cache to use for demo
	 */
	private static final String CACHE_NAME = "labCache";

	
	public static void main(String[] args) {
		final String initialList = System.getProperty("jdg.demo.initialList", DEFAULT_INITIAL_LIST);
		final String cacheName = System.getProperty("jdg.demo.cacheName", CACHE_NAME);
		final int maxEntries = Integer.parseInt(System.getProperty("jdg.demo.maxEntries", "1000"));
		final boolean clearOnFinish = Boolean.parseBoolean(System.getProperty("jdg.demo.clearOnFinish", "true"));
		final int putDelay = Integer.parseInt(System.getProperty("jdg.demo.putDelay", "50"));
		final boolean useTwitter = Boolean.parseBoolean(System.getProperty("jdg.demo.useTwitter", "false"));
		final String consumerKey = System.getProperty("jdg.demo.consumerKey");
		final String consumerSecret = System.getProperty("jdg.demo.consumerSecret");
		
		RemoteCacheManager cm = new RemoteCacheManager(initialList);
		
		if(useTwitter) {
			logger.info("Loading "+ maxEntries +" tweets into cache '" + cacheName + "' in JDG grid connected to: " + initialList);
			
			TwitterDemoClient twitterDemo = new TwitterDemoClient( cm, cacheName, consumerKey, consumerSecret);
			twitterDemo.setMaxEntries(maxEntries);	
			twitterDemo.setClearOnFinish(clearOnFinish);
			twitterDemo.setDelayMs(putDelay);
			twitterDemo.startAsync();			
		} else {
			logger.info("Loading "+ maxEntries +" objects into cache '" + cacheName + "' in JDG grid connected to: " + initialList);
			
			CountDemoClient countDemo = new CountDemoClient(cm, cacheName);
			countDemo.setMaxEntries(maxEntries);
			countDemo.setPayload("some example payload, it can be serializable object");
			countDemo.setClearOnFinish(clearOnFinish);
			countDemo.setDelayMs(putDelay);
			countDemo.startSync();
			
			System.out.println("Keys in cache!");
			BasicCache<Object, Object> cache = cm.getCache(cacheName);
			Set<Object> keys = cache.keySet();
			for (Object key : keys) {
			   System.out.println(key);
			}
			
		}		
	}
}
