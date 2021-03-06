/*
 * Copyright © 2017, 2019 Ivar Grimstad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.eclipse.krazo.test.ext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import org.eclipse.krazo.test.util.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class PebbleIT {

    private static final String WEB_INF_SRC = "src/main/resources/pebble/";

    @ArquillianResource
    private URL baseURL;

    @Drone
    private WebDriver webDriver;

    @Deployment(testable = false, name = "pebble")
    public static Archive createDeployment() {
        return new WebArchiveBuilder()
            .addPackage("org.eclipse.krazo.test.ext.pebble")
            .addView(Paths.get(WEB_INF_SRC).resolve("views/filter.peb").toFile(), "filter.peb")
            .addBeansXml()
            .addDependency("org.eclipse.krazo.ext:krazo-pebble")
            .build();
    }

    @Test
    public void testUsesFilter() {
        String text = "To be filtered";

        webDriver.navigate().to(baseURL + "pebble/filter");

        final String body = webDriver.findElement(By.tagName("body")).getText();
        assertFalse(body.contains(text));
        assertTrue(body.contains(text.toUpperCase()));
    }

}
