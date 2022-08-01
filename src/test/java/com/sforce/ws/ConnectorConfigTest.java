/*
 * Copyright (c) 2017, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.sforce.ws;

import org.junit.Assert;
import org.junit.Test;

public class ConnectorConfigTest {

    @Test
    public void testDoesVersionHasABugWithInvalidVersionShouldReturnFalse() {
        Assert.assertFalse("method should return false when we are not able to parse input version", ConnectorConfig.doesVersionHasABug("a.b.c"));
    }

    @Test
    public void testDoesVersionHasABugWithBugFixedVersionShouldReturnFalse() {
        Assert.assertFalse("method should return false for a java version that has been fixed for bug", ConnectorConfig.doesVersionHasABug("1.8.0_331"));
        Assert.assertFalse("method should return false for a java version that has been fixed for bug", ConnectorConfig.doesVersionHasABug("11.0.7"));
        Assert.assertFalse("method should return false for a java version that has been fixed for bug", ConnectorConfig.doesVersionHasABug("13.1"));
        Assert.assertFalse("method should return false for a java version that has been fixed for bug", ConnectorConfig.doesVersionHasABug("14.0.1"));
        Assert.assertFalse("method should return false for a java version that has been fixed for bug", ConnectorConfig.doesVersionHasABug("15"));
    }

    @Test
    public void testDoesVersionHasABugWithBugVersionShouldReturnTrue() {
        Assert.assertFalse("method should return true for a java version that has not been fixed for the bug", ConnectorConfig.doesVersionHasABug("1.8.0_172"));
        Assert.assertFalse("method should return true for a java version that has not been fixed for the bug", ConnectorConfig.doesVersionHasABug("10.0.2"));
        Assert.assertFalse("method should return true for a java version that has not been fixed for the bug", ConnectorConfig.doesVersionHasABug("11.0.5"));
        Assert.assertFalse("method should return true for a java version that has not been fixed for the bug", ConnectorConfig.doesVersionHasABug("13.0.1"));
        Assert.assertFalse("method should return true for a java version that has not been fixed for the bug", ConnectorConfig.doesVersionHasABug("9.0.1"));
    }
}
