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

import org.aerogear.digger.client.model.BuildParameter;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.credentials.Credential;
import org.aerogear.digger.client.util.DiggerClientException;
import org.apache.commons.lang.StringUtils;
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
    private static final String JOB_TEMPLATE_PATH = "templates/job.xml";

    private final Logger LOG = LoggerFactory.getLogger(JobService.class);

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
     * @param gitRepoCredential credential instance. See {@link Credential}.
     * @param buildParameters list of build parameters for the a parameterized job.
     *
     * @throws IOException
     */
    public void create(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, Credential gitRepoCredential, List<BuildParameter> buildParameters) throws IOException, DiggerClientException {
        String credentialId = updateCredential(jenkinsServer, name, gitRepoCredential);
        String jobTemplate = renderJobTemplate(gitRepo, gitBranch, buildParameters, credentialId);
        jenkinsServer.createJob(name, jobTemplate);
    }

    /**
     * Create new digger job on jenkins platform with no parameters.
     *
     * @param jenkinsServer Jenkins server client
     * @param name          job name that can be used later to reference job
     * @param gitRepo       git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch     git repository branch (default branch used to checkout source code)
     * @throws IOException
     * @see #create(JenkinsServer, String, String, String, Credential, List)
     */
    public void create(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch) throws IOException, DiggerClientException {
        this.create(jenkinsServer, name, gitRepo, gitBranch, null, null);
    }

    /**
     * Update digger job on jenkins platform.
     * NOTE: If gitRepoCredential is set in #create(JenkinsServer, String, String, String, List, Credential), it needs to be set in here as well. Otherwise it will be removed.
     *
     * @param jenkinsServer   Jenkins server client
     * @param name            job name that can be used later to reference job
     * @param gitRepo         git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch       git repository branch (default branch used to checkout source code)
     * @param gitRepoCredential credential instance. See {@link Credential}.
     * @param buildParameters list of build parameters for the a parameterized job.
     */
    public void update(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch, Credential gitRepoCredential, List<BuildParameter> buildParameters) throws DiggerClientException, IOException {
        String credentialId = updateCredential(jenkinsServer, name, gitRepoCredential);
        String jobTemplate = renderJobTemplate(gitRepo, gitBranch, buildParameters, credentialId);
        jenkinsServer.updateJob(name, jobTemplate);
    }

    /**
     *  Update digger job ob jenkins platform with no parameters.
     *
     * @param jenkinsServer Jenkins server client
     * @param name          job name that can be used later to reference job
     * @param gitRepo       git repository url (full git repository url. e.g git@github.com:digger/helloworld.git
     * @param gitBranch     git repository branch (default branch used to checkout source code)
     * @throws IOException
     * @see #update(JenkinsServer, String, String, String, Credential, List)
     */
    public void update(JenkinsServer jenkinsServer, String name, String gitRepo, String gitBranch) throws IOException, DiggerClientException {
        this.update(jenkinsServer, name, gitRepo, gitBranch, null, null);
    }


    /**
     * Get the id of the given credential. If it's set on the given gitRepoCredential instance, it will be returned.
     * Otherwise an id will be generated based on the jenkins job name.
     *
     * @param jobName the name of the jenkins job
     * @param gitRepoCredential the credential instance.
     * @return the credential id.
     */
    private String getCredentialId(String jobName, Credential gitRepoCredential) {
        String credentialId = null;
        if (StringUtils.isEmpty(gitRepoCredential.getId())) {
            credentialId = String.format("%s-%s", jobName, "gitRepoCredential");
        } else {
            credentialId = gitRepoCredential.getId();
        }
        return credentialId;
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
            credentialId = getCredentialId(name, gitRepoCredential);
            gitRepoCredential.setId(credentialId);
            try {
                //remove the credential first, in case the credential value changed.
                try {
                    jenkinsServer.deleteCredential(credentialId, false);
                } catch (Exception e) {
                    LOG.warn("Can not delete credential with id " + credentialId + ". It might not exist.", e);
                }
                jenkinsServer.createCredential(gitRepoCredential, false);
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
    private String renderJobTemplate(String gitRepo, String gitBranch, List<BuildParameter> buildParameters, String credentialId) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(JOB_TEMPLATE_PATH);
        JtwigModel model = JtwigModel.newModel()
            .with(GIT_REPO_URL, gitRepo)
            .with(GIT_REPO_BRANCH, gitBranch)
            .with(BUILD_PARAMETERS, buildParameters)
            .with(GIT_CREDENTIALS_ID, credentialId);
        return template.render(model);
    }
}
