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
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.credentials.UsernamePasswordCredential;
import org.aerogear.digger.client.model.BuildDiscarder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

    @Mock
    private JenkinsServer server;
    private JobService jobService;

    @Before
    public void beforeTests() {
        jobService = new JobService(false);
    }

    @Test
    public void shouldCreateJob() throws Exception {
        JobWithDetails job = mock(JobWithDetails.class);
        BuildDiscarder buildDiscarder = new BuildDiscarder();
        BuildWithDetails build = mock(BuildWithDetails.class);
        when(job.getBuildByNumber(anyInt())).thenReturn(build);
        when(build.details()).thenReturn(build);
        jobService.create(server, "name", "repo", "branch", buildDiscarder);
        verify(server, times(1)).createJob(anyString(), anyString());
    }

    @Test
    public void shouldUpdateJob() throws Exception {
        JobWithDetails job = mock(JobWithDetails.class);
        BuildDiscarder buildDiscarder = new BuildDiscarder();
        BuildWithDetails build = mock(BuildWithDetails.class);
        when(job.getBuildByNumber(anyInt())).thenReturn(build);
        when(build.details()).thenReturn(build);
        jobService.update(server, "name", "repo", "branch", buildDiscarder);
        verify(server, times(1)).updateJob(anyString(), anyString());
    }

    @Test
    public void shouldGetJob() throws Exception {
        JobWithDetails job = mock(JobWithDetails.class);
        BuildWithDetails build = mock(BuildWithDetails.class);
        jobService.get(server, "name");
        verify(server, times(1)).getJob(anyString());
    }

    @Test
    public void shouldCreateJobWithCredentials() throws Exception {
        UsernamePasswordCredential repoCredential = new UsernamePasswordCredential();
        repoCredential.setId("testCredentialId");
        repoCredential.setUsername("test");
        repoCredential.setPassword("test");
        jobService.create(server, "name", "repo", "branch", null, repoCredential, null);
        verify(server, times(1)).deleteCredential(repoCredential.getId(), false);
        verify(server, times(1)).createCredential(repoCredential, false);
        verify(server, times(1)).createJob(anyString(), anyString());
    }

    @Test
    public void shouldUpdateJobWithCredentials() throws Exception {
        UsernamePasswordCredential repoCredential = new UsernamePasswordCredential();
        BuildDiscarder buildDiscarder = new BuildDiscarder();
        repoCredential.setId("testCredentialId");
        repoCredential.setUsername("test");
        repoCredential.setPassword("test");
        jobService.update(server, "name", "repo", "branch", buildDiscarder, repoCredential, null);
        verify(server, times(1)).deleteCredential(repoCredential.getId(), false);
        verify(server, times(1)).createCredential(repoCredential, false);
        verify(server, times(1)).updateJob(anyString(), anyString());
    }

    @Test
    public void shouldDeleteJob() throws Exception {
        jobService.delete(server, "testJob", null);
        verify(server, times(1)).deleteCredential(anyString(), anyBoolean());
        verify(server, times(1)).deleteJob(anyString());
    }

}
