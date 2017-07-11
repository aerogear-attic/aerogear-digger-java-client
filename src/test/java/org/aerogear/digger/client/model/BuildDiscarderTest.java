package org.aerogear.digger.client.model;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BuildDiscarderTest {

    @Test
    /**
     * Tests that non-positive integers will be set to -1
     * This is the value the Jenkins XML expects empty values to be
     */
    public void shouldSetValidBuildDiscardValue() {
        BuildDiscarder buildDiscarder = new BuildDiscarder();
        buildDiscarder.setStoreArtifactsDays(-4);
        buildDiscarder.setStoreArtifactsTotal((5));
        buildDiscarder.setStoreBuildsDays(1);
        buildDiscarder.setStoreBuildsTotal(0);
        assertThat(buildDiscarder.getStoreArtifactsDays()).isEqualTo(-1);
        assertThat(buildDiscarder.getStoreArtifactsTotal()).isEqualTo(5);
        assertThat(buildDiscarder.getStoreBuildsDays()).isEqualTo(1);
        assertThat(buildDiscarder.getStoreBuildsTotal()).isEqualTo(-1);
    }
}
