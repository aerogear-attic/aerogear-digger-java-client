package com.redhat.digkins.services;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Service used to retrieve artifacts
 */
public class ArtifactsService {

  private JenkinsServer jenkins;

  /**
   * @param jenkins jenkins api instance
   */
  public ArtifactsService(JenkinsServer jenkins) {
    this.jenkins = jenkins;
  }

  private static final Logger LOG = LoggerFactory.getLogger(ArtifactsService.class);

  /**
   * Fetch artifacts urls for specific job and build number
   *
   * @param jobName name of the job
   * @param buildNumber job build number
   * @param artifactName - name of the artifact to fetch - can be regexp
   * @return InputStream with file contents
   */
  public InputStream fetchArtifact(String jobName, long buildNumber, String artifactName) {
    try {
      Build build = jenkins.getBuild(jobName, buildNumber);
      if (build instanceof BuildWithDetails) {
        BuildWithDetails buildWithDetails = ((BuildWithDetails) build);
        List<Artifact> artifacts = buildWithDetails.getArtifacts();
        for (Artifact artifact : artifacts) {
          if (artifact.getFileName().matches(artifactName)) {
            LOG.debug("Streaming artifact {0}", artifactName);
            return buildWithDetails.downloadArtifact(artifact);
          }
        }

      }
    } catch (Exception e) {
      LOG.error("Problem when fetching artifacts for {0} {1} {2}", jobName, buildNumber, artifactName, e);
    }
    LOG.debug("Cannot find build for ", jobName, buildNumber, artifactName);
    return null;
  }

}
