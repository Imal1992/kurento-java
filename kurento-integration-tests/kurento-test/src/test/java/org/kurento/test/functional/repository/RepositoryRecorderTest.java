/*
 * (C) Copyright 2014 Kurento (http://kurento.org/)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 */

package org.kurento.test.functional.repository;

import java.awt.Color;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.kurento.client.EndOfStreamEvent;
import org.kurento.client.EventListener;
import org.kurento.client.MediaPipeline;
import org.kurento.client.PlayerEndpoint;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.repository.RepositoryHttpRecorder;
import org.kurento.repository.RepositoryItem;
import org.kurento.test.base.RepositoryFunctionalTest;
import org.kurento.test.browser.WebRtcChannel;
import org.kurento.test.browser.WebRtcMode;
import org.kurento.test.config.TestScenario;

/**
 * Test of a Recorder in the repository, using the stream source from a PlayerEndpoint through an
 * WebRtcEndpoint
 * </p>
 * Media Pipeline(s):
 * <ul>
 * <li>PlayerEndpoint -> RecorderEndpoint & WebRtcEndpoint</li>
 * </ul>
 * Browser(s):
 * <ul>
 * <li>Chrome</li>
 * <li>Firefox</li>
 * </ul>
 * Test logic:
 * <ol>
 * <li>(KMS) Media server switchs the media from two WebRtcEndpoint using a Dispatcher, streaming
 * the result through antoher WebRtcEndpoint</li>
 * <li>(Browser) WebRtcPeer in rcv-only receives media</li>
 * </ol>
 * Main assertion(s):
 * <ul>
 * <li>Playing event should be received in remote video tag</li>
 * <li>The color of the received video should be as expected (green and the blue) <br>
 * <li>EOS event should arrive to player</li>
 * <li>Play time in remote video should be as expected</li>
 * </ul>
 * Secondary assertion(s):
 * <ul>
 * <li>--</li>
 * </ul>
 *
 * @author Boni Garcia (bgarcia@gsyc.es)
 * @since 5.0.4
 */
public class RepositoryRecorderTest extends RepositoryFunctionalTest {

  private static final int PLAYTIME = 10; // seconds

  @Parameters(name = "{index}: {0}")
  public static Collection<Object[]> data() {
    return TestScenario.localChromeAndFirefox();
  }

  @Test
  public void testRepositoryRecorder() throws Exception {
    // Media Pipeline
    MediaPipeline mp = kurentoClient.createMediaPipeline();
    PlayerEndpoint playerEp = new PlayerEndpoint.Builder(mp,
        "http://" + getTestFilesHttpPath() + "/video/10sec/ball.webm").build();
    WebRtcEndpoint webRtcEp1 = new WebRtcEndpoint.Builder(mp).build();

    RepositoryItem repositoryItem = repository.createRepositoryItem();
    RepositoryHttpRecorder recorder = repositoryItem.createRepositoryHttpRecorder();

    RecorderEndpoint recorderEp = new RecorderEndpoint.Builder(mp, recorder.getURL()).build();
    playerEp.connect(webRtcEp1);
    playerEp.connect(recorderEp);

    final CountDownLatch eosLatch = new CountDownLatch(1);
    playerEp.addEndOfStreamListener(new EventListener<EndOfStreamEvent>() {
      @Override
      public void onEvent(EndOfStreamEvent event) {
        eosLatch.countDown();
      }
    });

    // Test execution #1. Play the video while it is recorded
    launchBrowser(webRtcEp1, playerEp, recorderEp);

    // Wait for EOS
    Assert.assertTrue("Not received EOS event in player",
        eosLatch.await(getPage().getTimeout(), TimeUnit.SECONDS));

    // Release Media Pipeline #1
    recorderEp.stop();
    mp.release();
    Thread.sleep(500);
  }

  private void launchBrowser(WebRtcEndpoint webRtcEp, PlayerEndpoint playerEp,
      RecorderEndpoint recorderEp) throws InterruptedException {

    getPage().subscribeEvents("playing");
    getPage().initWebRtc(webRtcEp, WebRtcChannel.AUDIO_AND_VIDEO, WebRtcMode.RCV_ONLY);
    playerEp.play();
    final CountDownLatch eosLatch = new CountDownLatch(1);
    playerEp.addEndOfStreamListener(new EventListener<EndOfStreamEvent>() {
      @Override
      public void onEvent(EndOfStreamEvent event) {
        eosLatch.countDown();
      }
    });

    if (recorderEp != null) {
      recorderEp.record();
    }

    // Assertions
    Assert.assertTrue("Not received media (timeout waiting playing event)",
        getPage().waitForEvent("playing"));
    Assert.assertTrue("The color of the video should be black",
        getPage().similarColor(Color.BLACK));
    Assert.assertTrue("Not received EOS event in player",
        eosLatch.await(getPage().getTimeout(), TimeUnit.SECONDS));
    double currentTime = getPage().getCurrentTime();
    Assert.assertTrue(
        "Error in play time (expected: " + PLAYTIME + " sec, real: " + currentTime + " sec)",
        getPage().compare(PLAYTIME, currentTime));
  }
}
