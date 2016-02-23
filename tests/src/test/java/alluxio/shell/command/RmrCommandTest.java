/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the “License”). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */

package alluxio.shell.command;

import alluxio.AlluxioURI;
import alluxio.exception.AlluxioException;
import alluxio.shell.AbstractAlluxioShellTest;
import alluxio.shell.AlluxioShellUtilsTest;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests for rmr command.
 */
public class RmrCommandTest extends AbstractAlluxioShellTest {
  @Test
  public void rmrTest() throws IOException {
    StringBuilder toCompare = new StringBuilder();
    mFsShell.run("mkdir", "/testFolder1/testFolder2");
    toCompare.append(getCommandOutput(new String[] {"mkdir", "/testFolder1/testFolder2"}));
    mFsShell.run("touch", "/testFolder1/testFolder2/testFile2");
    toCompare
        .append(getCommandOutput(new String[] {"touch", "/testFolder1/testFolder2/testFile2"}));
    AlluxioURI testFolder1 = new AlluxioURI("/testFolder1");
    AlluxioURI testFolder2 = new AlluxioURI("/testFolder1/testFolder2");
    AlluxioURI testFile2 = new AlluxioURI("/testFolder1/testFolder2/testFile2");
    Assert.assertTrue(fileExist(testFolder1));
    Assert.assertTrue(fileExist(testFolder2));
    Assert.assertTrue(fileExist(testFile2));
    mFsShell.run("rmr", "/testFolder1/testFolder2/testFile2");
    toCompare.append("WARNING: rmr is deprecated. Please use rm -R instead.\n");
    toCompare.append(getCommandOutput(new String[] {"rm", "/testFolder1/testFolder2/testFile2"}));
    Assert.assertEquals(toCompare.toString(), mOutput.toString());
    Assert.assertTrue(fileExist(testFolder1));
    Assert.assertTrue(fileExist(testFolder2));
    Assert.assertFalse(fileExist(testFile2));
    mFsShell.run("rmr", "/testFolder1");
    toCompare.append("WARNING: rmr is deprecated. Please use rm -R instead.\n");
    toCompare.append(getCommandOutput(new String[] {"rmr", "/testFolder1"}));
    Assert.assertEquals(toCompare.toString(), mOutput.toString());
    Assert.assertFalse(fileExist(testFolder1));
    Assert.assertFalse(fileExist(testFolder2));
    Assert.assertFalse(fileExist(testFile2));
  }

  @Test
  public void rmrWildCardTest() throws IOException, AlluxioException {
    AlluxioShellUtilsTest.resetFileHierarchy(mFileSystem);

    mFsShell.run("rmr", "/testWildCards/foo/foo*");
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/foo/foobar1")));
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/foo/foobar2")));
    Assert.assertTrue(fileExist(new AlluxioURI("/testWildCards/foo")));
    Assert.assertTrue(fileExist(new AlluxioURI("/testWildCards/bar/foobar3")));

    mFsShell.run("rmr", "/testWildCards/ba*");
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/bar")));
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/bar/foobar3")));
    Assert.assertTrue(fileExist(new AlluxioURI("/testWildCards/foobar4")));

    mFsShell.run("rmr", "/testWildCards/*");
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/bar")));
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/foo")));
    Assert.assertFalse(fileExist(new AlluxioURI("/testWildCards/foobar4")));
  }
}
