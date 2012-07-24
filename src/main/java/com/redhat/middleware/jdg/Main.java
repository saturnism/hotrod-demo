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

import org.infinispan.client.hotrod.RemoteCacheManager;

/**
 * Main class.  Configure accordingly!  Pay attention to
 * <code>INITIAL_LIST</code> and <code>CACHE_NAME</code>
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 */
public class Main {
	/**
	 * Initial hotrod server list
	 */
	private static final String INITIAL_LIST = "127.0.0.1";
	
	/**
	 * Name of the cache to use for demo
	 */
	private static final String CACHE_NAME = "___defaultcache";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RemoteCacheManager cm = new RemoteCacheManager(INITIAL_LIST);
		
		CountDemoClient countDemo = new CountDemoClient(cm, CACHE_NAME);
		// optional parameters
		// countDemo.setMaxEntries(1000);
		// countDemo.setPayload("some example payload, it can be serializable object");
		countDemo.startSync();
		
		// TODO: replace CONSUMER KEY and CONSUMER SECRET!!
		TwitterDemoClient twitterDemo = new TwitterDemoClient(cm, CACHE_NAME, "CONSUMER KEY", "CONSUMER SECRET");
		// optional parameters
		// twitterDemo.setMaxEntries(1000);
		
		twitterDemo.startAsync();
		
	}
}
