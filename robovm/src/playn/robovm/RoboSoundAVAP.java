/**
 * Copyright 2012 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.robovm;

import org.robovm.apple.avfoundation.AVAudioPlayer;
import org.robovm.apple.avfoundation.AVAudioPlayerDelegate;
import org.robovm.apple.foundation.NSError;

import playn.core.SoundImpl;

/**
 * An implementation of Sound using the AVAudioPlayer. This is used for compressed audio,
 * especially lengthy music tracks.
 */
public class RoboSoundAVAP extends SoundImpl<AVAudioPlayer> {

  private AVAudioPlayerDelegate delegate = new AVAudioPlayerDelegate() {
    public void didFinishPlaying(AVAudioPlayer player, boolean flag) {}
    public void decodeErrorDidOccur(AVAudioPlayer player, NSError error) {}
    public void beginInterruption(AVAudioPlayer player) {}
    public void endInterruptionWithOptions(AVAudioPlayer player, long flags) {}
    public void endInterruptionWithFlags(AVAudioPlayer player, long flags) {}
    public void endInterruption(AVAudioPlayer player) {
      impl.setCurrentTime(0);
      impl.prepareToPlay();
      impl.play();
    }
  };

  public RoboSoundAVAP (RoboPlatform plat) {
    super(plat.exec());
  }

  @Override
  protected boolean prepareImpl() {
    return impl.prepareToPlay();
  }

  @Override
  protected boolean playingImpl() {
    return impl.isPlaying();
  }

  @Override
  protected boolean playImpl() {
    impl.setCurrentTime(0);
    return impl.play();
  }

  @Override
  protected void stopImpl() {
    // TODO: disable interruption handler?
    impl.stop();
    impl.setCurrentTime(0);
  }

  @Override
  protected void setLoopingImpl(boolean looping) {
    impl.setNumberOfLoops(looping ? -1 : 0);
  }

  @Override
  protected void setVolumeImpl(float volume) {
    impl.setVolume(volume);
  }

  @Override
  protected void releaseImpl() {
    impl.dispose();
  }
}
