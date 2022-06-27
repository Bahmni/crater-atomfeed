package org.bahmni.module.feedintegration.atomfeed.jobs;

public interface FeedJob {
    void process() throws InterruptedException;
}
