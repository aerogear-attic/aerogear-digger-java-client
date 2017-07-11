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
package org.aerogear.digger.client.services;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.credentials.Credential;
import org.aerogear.digger.client.model.BuildDiscarder;
import org.aerogear.digger.client.model.BuildParameter;
import org.aerogear.digger.client.util.DiggerClientException;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Create digger job on jenkins platform
 */
public class JobService {

    private final static String GIT_REPO_URL = "GIT_REPO_URL";
    private final static String GIT_CREDENTIALS_ID = "GIT_CREDENTIALS_ID";
    private final static String GIT_REPO_BRANCH = "GIT_REPO_BRANCH";
    private final static String BUILD_PARAMETERS = "BUILD_PARAMETERS";
    private final static String STORE_BUILDS_DAYS = "STORE_BUILDS_DAYS";
    private final static String STORE_BUILDS_TOTAL = "STORE_BUILDS_TOTAL";
    private final static String STORE_ARTIFACTS_DAYS = "STORE_ARTIFACTS_DAYS";
    private final static String STORE_ARTIFACTS_TOTAL = "STORE_ARTIFACTS_TOTAL";
    private static final String JOB_TEMPLATE_PATH = "templates/job.xml";

    private final Logger LOG = LoggerFactory.getLogger(JobService.class);

    private boolean crumbFlag = false;

    /**
     * Constructor.
     * @param crumbFlag Specify if CSRF Protection is enabled on the Jenkins server.
     */
    public JobService(boolean crumbFlag) {
        this.crumbFlag = crumbFlag;
    }

    /**
     * Get a digger job on jenkins platform.
     *
     * @param jenkinsServer Jenkins server client
     * @param name          name of the job to retrieve
     * @return job from Jenkins platform or null if not found
     * @throws IOException
     */
    public JobWithDetails get(JenkinsServer jenkinsServer, String name) throws IOException {
        return jenkinsServer.getJob(name);
    }

    /**
     * Create new digger job on jenkins platform
     *
     * @param jenkinsServer   Jenkins server client
     * @param name            job name that can be used later to reference job
     * @param gitRepo         git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch       git repository branch (default branch used to checkout source code)
     * @param buildDiscarder  BuildDiscarder instance. See {@link BuildDiscarder}
     * @param gitRepoCredential credential instance. See {@link Credential}.
     * @param buildParameters list of build parameters for the a parameterized job.
     *
     * @throws IOException
     */
    public void create(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, BuildDiscarder buildDiscarder, Credential gitRepoCredential, List<BuildParameter> buildParameters) throws IOException, DiggerClientException {
        String jobTemplate = prepareJob(jenkinsServer, name, gitRepo, gitBranch, buildDiscarder, gitRepoCredential, buildParameters);
        jenkinsServer.createJob(name, jobTemplate);
    }

    /**
     * Create new digger job on jenkins platform
     *
     * @param jenkinsServer   Jenkins server client
     * @param name            job name that can be used later to reference job
     * @param gitRepo         git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch       git repository branch (default branch used to checkout source code)
     * @param gitRepoCredential credential instance. See {@link Credential}.
     * @param buildParameters list of build parameters for the a parameterized job.
     *
     * @throws IOException
     */
    public void create(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, Credential gitRepoCredential, List<BuildParameter> buildParameters) throws IOException, DiggerClientException {
        this.create(jenkinsServer, name, gitRepo, gitBranch, null, gitRepoCredential, buildParameters);
    }

    /**
     * Create new digger job on jenkins platform with no parameters.
     *
     * @param jenkinsServer Jenkins server client
     * @param name          job name that can be used later to reference job
     * @param gitRepo       git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch     git repository branch (default branch used to checkout source code)     * @param storeBuildsDays the number of days a build should be persisted for before cleanup
     * @param buildDiscarder  BuildDiscarder instance. See {@link BuildDiscarder}
     * @throws IOException
     * @see #create(JenkinsServer, String, String, String, BuildDiscarder, Credential, List)
     */
    public void create(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, BuildDiscarder buildDiscarder) throws IOException, DiggerClientException {
        this.create(jenkinsServer, name, gitRepo, gitBranch, buildDiscarder, null , null);
    }


    /**
     * Create new digger job on jenkins platform with no parameters.
     *
     * @param jenkinsServer Jenkins server client
     * @param name          job name that can be used later to reference job
     * @param gitRepo       git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch     git repository branch (default branch used to checkout source code)     * @param storeBuildsDays the number of days a build should be persisted for before cleanup
     * @throws IOException
     * @see #create(JenkinsServer, String, String, String, BuildDiscarder, Credential, List)
     */
    public void create(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch) throws IOException, DiggerClientException {
        this.create(jenkinsServer, name, gitRepo, gitBranch, null, null , null);
    }

    /**
     * Update digger job on jenkins platform.
     * NOTE: If gitRepoCredential is set in #create(JenkinsServer, String, String, String, List, Credential), it needs to be set in here as well. Otherwise it will be removed.
     *
     * @param jenkinsServer   Jenkins server client
     * @param name            job name that can be used later to reference job
     * @param gitRepo         git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch       git repository branch (default branch used to checkout source code)
     * @param buildDiscarder  BuildDiscarder instance. See {@link BuildDiscarder}
     * @param gitRepoCredential credential instance. See {@link Credential}.
     * @param buildParameters list of build parameters for the a parameterized job.
     */
    public void update(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, BuildDiscarder buildDiscarder, Credential gitRepoCredential, List<BuildParameter> buildParameters) throws DiggerClientException, IOException {
        String jobTemplate = prepareJob(jenkinsServer, name, gitRepo, gitBranch, buildDiscarder, gitRepoCredential, buildParameters);
        jenkinsServer.updateJob(name, jobTemplate);
    }

    /**
     *  Update digger job on jenkins platform with no parameters.
     *
     * @param jenkinsServer Jenkins server client
     * @param name          job name that can be used later to reference job
     * @param gitRepo       git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch     git repository branch (default branch used to checkout source code)
     * @param buildDiscarder  BuildDiscarder instance. See {@link BuildDiscarder}
     * @throws IOException
     * @see #update(JenkinsServer, String, String, String, BuildDiscarder, Credential, List)
     */
    public void update(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, BuildDiscarder buildDiscarder) throws IOException, DiggerClientException {
        this.update(jenkinsServer, name, gitRepo, gitBranch, buildDiscarder, null, null);
    }

    /**
     * Delete the job and associated credential.
     * @param jenkinsServer the Jenkins server
     * @param name the name of the Jenkins job
     * @param givenCredentialId the id of the credential. It should be the same id value if gitRepoCredential is provided in #create(JenkinsServer, String, String, String, Credential, List) and it has an id value. Otherwise pass null.
     * @throws IOException
     */
    public void delete(JenkinsServer jenkinsServer, String name, String givenCredentialId) throws IOException {
        String credentialId = getCredentialId(name, givenCredentialId);
        tryDeleteCredentailWithId(jenkinsServer, credentialId);
        jenkinsServer.deleteJob(name);
    }


    /**
     * Return the credentialId. If the givenCredentialId is set, it will be used. Otherwise a default credentail id will be generated from the jobName.
     *
     * @param jobName the name of the jenkins job
     * @param givenCredentialId the credentialId specified in a Credential instance.
     * @return the credential id.
     */
    private String getCredentialId(String jobName, String givenCredentialId) {
        String credentialId = givenCredentialId;
        if (credentialId == null) {
            credentialId = String.format("%s-%s", jobName, "gitRepoCredential");
        }
        return credentialId;
    }

    /**
     * Prepare before creating/updating the jenkins job.
     * @param jenkinsServer the jenkins server
     * @param name  the name of the job
     * @param gitRepo the git repo
     * @param gitBranch the git branch
     * @param gitRepoCredential the credential to access the git repo. can be bull.
     * @param buildParameters list of build parameters. can be null.
     * @return the XML string value of the jenkins job
     * @throws DiggerClientException
     */
    private String prepareJob(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, BuildDiscarder buildDiscarder, Credential gitRepoCredential, List<BuildParameter> buildParameters) throws DiggerClientException {
        String credentialId = updateCredential(jenkinsServer, name, gitRepoCredential);
        String jobTemplate = renderJobTemplate(gitRepo, gitBranch, buildDiscarder, buildParameters, credentialId);
        return jobTemplate;
    }

    /**
     * Update the credential in Jenkins.
     * @param jenkinsServer the jenkins server instance
     * @param name the name of the job
     * @param gitRepoCredential the new credential instance. Can be null.
     * @return the id of the credential. Can be null if gitRepoCredential is null.
     * @throws DiggerClientException
     */
    private String updateCredential(JenkinsServer jenkinsServer, String name, Credential gitRepoCredential) throws DiggerClientException {
        String credentialId = null;
        if (gitRepoCredential != null) {
            credentialId = getCredentialId(name, gitRepoCredential.getId());
            gitRepoCredential.setId(credentialId);
            try {
                //remove the credential first, in case the credential value changed.
                tryDeleteCredentailWithId(jenkinsServer, credentialId);
                jenkinsServer.createCredential(gitRepoCredential, this.crumbFlag);
            } catch (IOException ioe) {
                LOG.error("Creating credential failed with error", ioe);
                throw new DiggerClientException("can not create credential", ioe);
            }
        }
        return credentialId;
    }

    /**
     * Get the XML string value of the jenkins job.
     * @param gitRepo  the git repo url
     * @param gitBranch the git branch name
     * @param buildParameters the list of build parameters. can be null.
     * @param credentialId the id of the credential. can be null.
     * @return the XML string value of the jenkins job.
     */
    private String renderJobTemplate(String gitRepo, String gitBranch, BuildDiscarder buildDiscarder, List<BuildParameter> buildParameters, String credentialId) {

        if (buildDiscarder == null) {
            buildDiscarder = new BuildDiscarder();
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate(JOB_TEMPLATE_PATH);
        JtwigModel model = JtwigModel.newModel()
            .with(GIT_REPO_URL, gitRepo)
            .with(GIT_REPO_BRANCH, gitBranch)
            .with(STORE_BUILDS_DAYS, buildDiscarder.getStoreBuildsDays())
            .with(STORE_BUILDS_TOTAL, buildDiscarder.getStoreBuildsTotal())
            .with(STORE_ARTIFACTS_DAYS, buildDiscarder.getStoreArtifactsDays())
            .with(STORE_ARTIFACTS_TOTAL, buildDiscarder.getStoreArtifactsTotal())
            .with(BUILD_PARAMETERS, buildParameters)
            .with(GIT_CREDENTIALS_ID, credentialId);
        return template.render(model);
    }


    /**
     * Try to delete a credential from Jenkins with the given credentialId
     * @param jenkinsServer the jenkins server
     * @param credentialId the credential id
     */
    private void tryDeleteCredentailWithId(JenkinsServer jenkinsServer, String credentialId) {
        try {
            jenkinsServer.deleteCredential(credentialId, this.crumbFlag);
        } catch (Exception e) {
            LOG.warn("Can not delete credential with id " + credentialId + ". It might not exist.", e);
        }
    }
}
