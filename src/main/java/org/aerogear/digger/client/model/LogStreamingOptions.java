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
