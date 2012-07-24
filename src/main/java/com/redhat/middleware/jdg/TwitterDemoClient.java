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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.infinispan.api.BasicCache;
import org.infinispan.api.BasicCacheContainer;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A hotrod client that populates the cache w/ Twitter stream.
 * 
 * @author <a href="mailto:rtsang@redhat.com">Ray Tsang</a>
 *
 */
public class TwitterDemoClient extends DelayableDemoClient<Long, Status> {
	private static final int DEFAULT_MAX_ENTRIES = 1000;
	
	private Logger logger = Logger.getLogger(TwitterDemoClient.class.getName());

	/**
	 * Twitter API Consumer Key, you should create your own.
	 */
	private final String consumerKey;
	
	/**
	 * Twitter API Consumer Secret, you should create your own.
	 */
	private final String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private int maxEntries = DEFAULT_MAX_ENTRIES;

	public TwitterDemoClient(BasicCache<Long, Status> cache,
			String consumerKey, String consumerSecret) {
		super(cache);
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public TwitterDemoClient(BasicCacheContainer container, String cacheName,
			String consumerKey, String consumerSecret) {
		super(container, cacheName);
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}
	
	@Override
	public void startSync() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void runSync() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear() {
		getCache().clear();
	}

	protected void authorize() throws TwitterException, IOException {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		RequestToken requestToken = twitter.getOAuthRequestToken();
		AccessToken accessToken = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (null == accessToken) {
			System.out
					.println("Open the following URL and grant access to your account:");
			System.out.println(requestToken.getAuthorizationURL());
			System.out
					.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
			String pin = br.readLine();
			try {
				if (pin.length() > 0) {
					accessToken = twitter
							.getOAuthAccessToken(requestToken, pin);
				} else {
					accessToken = twitter.getOAuthAccessToken();
				}

				this.accessToken = accessToken.getToken();
				this.accessTokenSecret = accessToken.getTokenSecret();
			} catch (TwitterException te) {
				if (401 == te.getStatusCode()) {
					System.out.println("Unable to get the access token.");
				} else {
					te.printStackTrace();
				}
			}
		}
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public int getMaxEntries() {
		return maxEntries;
	}

	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	protected class DemoTwitterListener implements StatusListener {
		private final TwitterStream twitterStream;
		private int count = 0;
		
		public DemoTwitterListener(TwitterStream twitterStream) {
			this.twitterStream = twitterStream;
		}

		public void onStatus(Status status) {
			count++;
			if (maxEntries > 0 && count > maxEntries) {
				twitterStream.shutdown();
				clear();
				return;
			}
			getCache().put(status.getId(), status);
		}

		public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		}

		public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		}

		public void onException(Exception ex) {
			ex.printStackTrace();
		}

		public void onScrubGeo(long lat, long lng) {
		}
	}
	
	public void startAsync() {

		ConfigurationBuilder cb = new ConfigurationBuilder();

		if (accessTokenSecret == null || accessToken == null) {
			try {
				this.authorize();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "error occured while authorizing", e);
			}
		}

		cb.setDebugEnabled(false).setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessTokenSecret);

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();
		twitterStream.addListener(new DemoTwitterListener(twitterStream));
		twitterStream.sample();
	}
}
