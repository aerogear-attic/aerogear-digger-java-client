package org.aerogear.digger.client.model;

/**
 * This objects represent the Jenkins model which takes care of
 * clearing up old builds and artifacts and managing the total
 * number of saved builds and artifacts at a given time.
 * This values are set by the user at job creation time.
 */
public class BuildDiscarder {

    private int storeBuildsDays = -1;
    private int storeBuildsTotal = -1;
    private int storeArtifactsDays = -1;
    private int storeArtifactsTotal = -1;

    /**
     * @return the number of days builds for a job should be stored for
     */
    public int getStoreBuildsDays() {
        return storeBuildsDays;
    }

    /**
     * @return the total number of builds that are stored for a job
     */
    public int getStoreBuildsTotal() {
        return storeBuildsTotal;
    }

    /**
     * @return the number of days artifacts for a build are stored for
     */
    public int getStoreArtifactsDays() {
        return storeArtifactsDays;
    }

    /**
     * @return the total number of artifacts that are be stored for a job
     */
    public int getStoreArtifactsTotal() {
        return storeArtifactsTotal;
    }

    /**
     * Set the number of days builds for a job should be stored for
     * @param daysToStoreBuilds build records are only kept up to this number of days
     */
    public void setStoreBuildsDays(int daysToStoreBuilds) {
        this.storeBuildsDays = validateIntegerInput(daysToStoreBuilds);
    }

    /**
     * Set the total number of builds that should be stored for a job
     * @param totalBuildsToStore only up to this number of build records are kept
     */
    public void setStoreBuildsTotal(int totalBuildsToStore) {
        this.storeBuildsTotal = validateIntegerInput(totalBuildsToStore);
    }

    /**
     * Set the number of days artifacts for a build should be stored for
     * @param daysToStoreArtifacts artifacts from builds older than this number of days will be deleted
     */
    public void setStoreArtifactsDays(int daysToStoreArtifacts) {
        this.storeArtifactsDays = validateIntegerInput(daysToStoreArtifacts);
    }

    /**
     * Set the total number of artifacts that should be stored for a job
     * @param totalArtifactsToStore only up to this number of builds have their artifacts retained
     */
    public void setStoreArtifactsTotal(int totalArtifactsToStore) {
        this.storeArtifactsTotal = validateIntegerInput(totalArtifactsToStore);
    }

    /**
     * In the LogRotator section of the XML template, if the user does not specify
     * an integer to represent days, it is set as -1 when saved.
     * If a value of less than -1 is provided, set it to -1
     * @param input an integer value for LogRotator param
     * @return the validated or modified input
     */
    private int validateIntegerInput(int input) {
        if (input < 1 ) {
            input = -1;
        }
        return input;
    }
}
