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

/**
 * Represents a parameter setting on a parameterized build in Jenkins.
 * <p>
 * This entity does should be used when creating a job. It should not
 * be used to pass parameters to a build when triggering a build.
 **/
public class BuildParameter {

  private String name;
  private String description;
  private String defaultValue;
  
  public BuildParameter(String name) {
    this.name = name;
    this.description = "";
    this.defaultValue = "";
  }

  /**
    * @return Updated build parameter
    */
  public BuildParameter setName(String name) {
    this.name = name;
    return this;
  }

  /**
    * @return Updated build parameter
    */
  public BuildParameter setDescription(String description) {
    this.description = description;
    return this;
  }

  /**
    * @return Updated build parameter
    */
  public BuildParameter setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
    * @return Name of the build parameter
    */
  public String getName() {
    return this.name;
  }

  /**
    * @return Description of the build parameter
    */
  public String getDescription() {
    return this.description;
  }

  /**
    * @return Default value of the build parameter
    */
  public String getDefaultValue() {
    return this.defaultValue;
  }
}
