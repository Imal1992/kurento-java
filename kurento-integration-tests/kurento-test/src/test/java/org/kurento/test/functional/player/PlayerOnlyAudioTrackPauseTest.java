/*
 * (C) Copyright 2015 Kurento (http://kurento.org/)
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

package org.kurento.test.functional.player;

import static org.kurento.test.config.Protocol.FILE;
import static org.kurento.test.config.Protocol.HTTP;
import static org.kurento.test.config.Protocol.S3;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.kurento.test.browser.WebRtcChannel;
import org.kurento.test.config.Protocol;
import org.kurento.test.config.TestScenario;

/**
 * Test of a the pause feature with only track audio for a PlayerEndpoint.
 * </p>
 * Media Pipeline(s):
 * <ul>
 * <li>PlayerEndpoint -> WebRtcEndpoint</li>
 * </ul>
 * Browser(s):
 * <ul>
 * <li>Chrome</li>
 * <li>Firefox</li>
 * </ul>
 * Test logic:
 * <ol>
 * <li>(KMS) During the playback of a stream from a PlayerEndpoint to a WebRtcEndpoint, the
 * PlayerEndpoint is paused and then resumed</li>
 * <li>(Browser) WebRtcPeer in rcv-only receives media</li>
 * </ol>
 * Main assertion(s):
 * <ul>
 * <li>Color or the video should remain when a video is paused</li>
 * <li>After the pause, the color or the video should change</li>
 * </ul>
 * Secondary assertion(s):
 * <ul>
 * <li>Playing event should be received in remote video tag</li>
 * </ul>
 *
 * @author Raul Benitez (rbenitez@gsyc.es)
 * @since 6.1.1
 */
public class PlayerOnlyAudioTrackPauseTest extends FunctionalPlayerTest {

  @Parameters(name = "{index}: {0}")
  public static Collection<Object[]> data() {
    return TestScenario.localChromeAndFirefox();
  }

  private void initTest(Protocol protocol, String nameMedia) throws Exception {
    int pauseTimeSeconds = 3;

    String mediaUrl = getMediaUrl(protocol, nameMedia);

    testPlayerPause(mediaUrl, WebRtcChannel.AUDIO_ONLY, pauseTimeSeconds, null);
  }

  @Test
  public void testPlayerOnlyAudioPauseHttpMp3() throws Exception {
    // Test data
    String mediaUrl = "/audio/10sec/cinema.mp3";
    initTest(HTTP, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseHttpM4a() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.m4a";
    initTest(HTTP, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseHttpOgg() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.ogg";
    initTest(HTTP, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseHttpWav() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.wav";
    initTest(HTTP, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseHttpWma() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.wma";
    initTest(HTTP, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseFileMp3() throws Exception {
    // Test data
    String mediaUrl = "/audio/10sec/cinema.mp3";
    initTest(FILE, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseFileM4a() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.m4a";
    initTest(FILE, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseFileOgg() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.ogg";
    initTest(FILE, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseFileWav() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.wav";
    initTest(FILE, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseFileWma() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.wma";
    initTest(FILE, mediaUrl);
  }

  @Ignore
  public void testPlayerOnlyAudioPauseS3Mp3() throws Exception {
    // Test data
    String mediaUrl = "/audio/10sec/cinema.mp3";
    initTest(S3, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseS3M4a() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.m4a";
    initTest(S3, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseS3Ogg() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.ogg";
    initTest(S3, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseS3Wav() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.wav";
    initTest(S3, mediaUrl);
  }

  @Test
  public void testPlayerOnlyAudioPauseS3Wma() throws Exception {
    // Test data
    final String mediaUrl = "/audio/10sec/cinema.wma";
    initTest(S3, mediaUrl);
  }
}
