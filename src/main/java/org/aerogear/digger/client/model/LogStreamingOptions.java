/**
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aerogear.digger.client.model;

import com.offbytwo.jenkins.helper.BuildConsoleStreamListener;

/**
 * Class to control the behavior of build logs streaming.
 */
public class LogStreamingOptions {

    /**
     * Polling interval. Default to 5 seconds.
     */
    private int pollingInterval = 5;

    /**
     * Polling timeout. Default to 1 hour.
     */
    private int pollingTimeout = 60*60;

    /**
     * Listener of streaming events.
     */
    private BuildConsoleStreamListener streamListener;

    /**
     * Constructor. Create with a listener
     * @param streamListener the listener to receive log data.
     */
    public LogStreamingOptions(BuildConsoleStreamListener streamListener) {
        this.streamListener = streamListener;
    }

    /**
     * Constructor. Create with a listener, polling interval and polling timeout values.
     * @param streamListener the listener to receive log data.
     * @param pollingInterval in seconds
     * @param pollingTimeout in seconds
     */
    public LogStreamingOptions(BuildConsoleStreamListener streamListener, int pollingInterval, int pollingTimeout) {
        this.streamListener = streamListener;
        this.pollingInterval = pollingInterval;
        this.pollingTimeout = pollingTimeout;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public int getPollingTimeout() {
        return pollingTimeout;
    }

    public void setPollingTimeout(int pollingTimeout) {
        this.pollingTimeout = pollingTimeout;
    }

    public BuildConsoleStreamListener getStreamListener() {
        return streamListener;
    }

    public void setStreamListener(BuildConsoleStreamListener streamListener) {
        this.streamListener = streamListener;
    }
}
