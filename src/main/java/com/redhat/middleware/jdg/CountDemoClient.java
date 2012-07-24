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

import org.infinispan.api.BasicCache;
import org.infinispan.api.BasicCacheContainer;

/**
 * A simple hotrod client that populates the cache sequentially by counting
 * from 0 to <code>entries - 1</code>.
 * 
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 */
public class CountDemoClient extends DelayableDemoClient<Integer, Object> {
	
	private static final int DEFAULT_MAX_ENTRIES = 1000;
	private static final String DEFAULT_PAYLOAD = "Some data";
	
	/**
	 * Max number of entries to generate.
	 */
	private int maxEntries = DEFAULT_MAX_ENTRIES;
	
	/**
	 * Payload you want to put for each entry (e.g., to test w/ different payload sizes).
	 */
	private Object payload = DEFAULT_PAYLOAD;

	public CountDemoClient(BasicCache<Integer, Object> cache) {
		super(cache);
	}

	public CountDemoClient(BasicCacheContainer container, String cacheName) {
		super(container, cacheName);
	}

	@Override
	public void runSync() {
		for (int i = 0; i < maxEntries; i++) {
			getCache().put(i, payload);
			delay();
		}
	}
	
	@Override
	public void clear() {
		for (int i = 0; i < maxEntries; i++) {
			getCache().remove(i);
			delay();
		}
		for (int i = 0; i < maxEntries; i++) {
			if (getCache().containsKey(i)) {
				System.out.println("Found i = " + i + ".  This is bad!");
			}
		}
	}

	public int getMaxEntries() {
		return maxEntries;
	}

	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public void startAsync() {
		throw new UnsupportedOperationException();
	}
}
