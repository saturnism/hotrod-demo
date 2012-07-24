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
 * 
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 * @param <K> key type
 * @param <V> value type
 */
public abstract class AbstractHotRodDemoClient<K, V> implements HotRodDemoClient<K, V> {
	public static final boolean DEFAULT_CLEAR_ON_FINISH = true;
	
	private final BasicCache<K, V> cache;
	
	/**
	 * If set to true, cache will be cleared once runSync() completes
	 */
	private boolean clearOnFinish = DEFAULT_CLEAR_ON_FINISH;
	
	
	@SuppressWarnings("unchecked")
	public AbstractHotRodDemoClient(BasicCacheContainer container, String cacheName) {
		this((BasicCache<K, V>) container.getCache(cacheName));
	}

	public AbstractHotRodDemoClient(BasicCache<K, V> cache) {
		super();
		this.cache = cache;
	}
	
	public void startSync() {
		runSync();
		
		if (clearOnFinish) {
			clear();
		}
	}
	
	public void startAsync() {
		// TODO start a thread
	}
	
	/**
	 * Run the demo synchronously
	 */
	public abstract void runSync();
	
	/**
	 * Clear the cache.  This will be called when clearOnFinish is set to true.
	 */
	public abstract void clear();

	public BasicCache<K, V> getCache() {
		return cache;
	}

	public boolean isClearOnFinish() {
		return clearOnFinish;
	}

	public void setClearOnFinish(boolean clearOnFinish) {
		this.clearOnFinish = clearOnFinish;
	}
}
