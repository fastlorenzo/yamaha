/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.yamahamusiccast.internal.api.model;

/**
 * PlayInfo
 *
 * @author Dries Decock - Initial contribution
 * @author Hector Rodriguez Medina - Change variable names, added mising values
 */

public class PlayInfo extends Response {
    public static final String url = "/YamahaExtendedControl/v2/netusb/getPlayInfo";

    protected String input;
    protected String playQueueType;
    protected String playback;
    protected String repeat;
    protected String shuffle;
    protected Integer playTime;
    protected Integer totalTime;
    protected String artist;
    protected String album;
    protected String track;
    protected String albumartUrl;
    protected Integer albumartId;
    protected String usbDevicetype;
    protected Boolean autoStopped;
    protected Integer attribute;

    /*
     * Returns current Net/USB related Input ID Refer to
     * “All ID List” for details
     */
    public String getInput() {
        return input;
    }

    /*
     * Reserved
     */
    public String getPlayQueueType() {
        return playQueueType;
    }

    /*
     * Returns playback status Values: "play" / "stop" / "pause" / "fast_reverse" / "fast_forward"
     */
    public String getPlayback() {
        return playback;
    }

    /*
     * Returns repeat setting status Value: "off" / "one" / "all"
     */
    public String getRepeat() {
        return repeat;
    }

    /*
     * Returns shuffle setting status Values: "off" / "on" / "songs" / "albums"
     */
    public String getShuffle() {
        return shuffle;
    }

    /*
     * Returns current playback time (unit in second). Returns -60000 if playback time
     * is invalid Value Range: -60000 (invalid) / -59999 ~ 59999 (valid)
     */
    public Integer getPlayTime() {
        return playTime;
    }

    /*
     * Returns total playback time (unit in second). Returns 0 if total time is not
     * available or invalid Value Range: 0 ~ 59999
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /*
     * Returns artist name.
     * Returns station name if the input is Net Radio / Pandora / radiko.
     * Returns station name/artist name if the input is Rhapsody (Radio).
     * Returns ad name if Pandora playbacks ad contents.
     * If input is MC Link, returns master’s internal content info or Room Name if the master input
     * is one of external sources
     */
    public String getArtist() {
        return artist;
    }

    /*
     * Returns album name. Returns channel name if the input is SiriusXM.
     * Returns subtitle name if the input is radiko.
     * Returns company name if Pandora playbacks an ad.
     * If input is MC Link, returns master’s internal content info or
     * Input Name if the master input is one of external sources
     */
    public String getAlbum() {
        return album;
    }

    /*
     * Returns track name.
     * Returns song name if the input is Rhapsody / SiriusXM / Pandora.
     * Returns title name if the input is radiko.
     * If input is MC Link, returns master’s internal content info or
     * empty text if the master input is one of external sources
     */
    public String getTrack() {
        return track;
    }

    /*
     * Returns a URL to retrieve album art data. Data is in jpg/png/bmp/ymf format.
     * The path is given as relative address.
     * If "xxx/yyy/zzz.jpg" is returned, the absolute path is expressed as http://{host}/xxx/yyy/zzz.jpg
     * Note: ymf is original format encrypted by Yamaha AV encryption method.
     * Note: Return jpg/png/bmp fomat data on and after API Version 1.17.
     */
    public String getAlbumartUrl() {
        return albumartUrl;
    }

    /*
     * Returns ID to identify album art.
     * If ID got changed, retry to get album art data via albumart_url Value Range: 0 ～ 255
     */
    public Integer getAlbumartId() {
        return albumartId;
    }

    /*
     * Returns USB device type.
     * Returns "unknown" if no USB is connected Values: "msc" / "ipod" / "unknown"
     */
    public String getUsbDevicetype() {
        return usbDevicetype;
    }

    /*
     * Returns whether or not auto top has initiated.
     * If it is true, display appropriate messages to the external application user interface depending on which input
     * current one is.
     * This flag is cleared (set back to false) with these conditions as follows;
     * - Playback is initiated properly
     * - /netusb/setPlayback is executed
     * - type = play found in /netusb/setListControl is executed
     * Target Input : Pandora / SiriusXM
     * A MusicCast Device that detects non-operation time (by key operation on
     * the Device or by remote control) will always return false flag in this data
     */
    public Boolean getAutoStopped() {
        return autoStopped;
    }

    /*
     * Returns playback attribute info.
     * Attributes are expressed as OR of bit field as shown below;
     * b[0] Playable
     * b[1] Capable of Stop
     * b[2] Capable of Pause
     * b[3] Capable of Prev Skip
     * b[4] Capable of Next Skip
     * b[5] Capable of Fast Reverse
     * b[6] Capable of Fast Forward
     * b[7] Capable of Repeat
     * b[8] Capable of Shuffle
     * b[9] Feedback Available (Pandora)
     * b[10] Thumbs-Up (Pandora)
     * b[11] Thumbs-Down (Pandora)
     * b[12] Video (USB)
     * b[13] Capable of Bookmark (Net Radio)
     * b[14] DMR Playback (Server)
     * b[15] Station Playback (Rhapsody / Napster)
     * b[16] AD Playback (Pandora)
     * b[17] Shared Station (Pandora)
     * b[18] Capable of Add Track (Rhapsody/Napster/Pandora/JUKE/Qobuz)
     * b[19] Capable of Add Album (Rhapsody / Napster / JUKE)
     * b[20] Shuffle Station (Pandora)
     * b[21] Capable of Add Channel (Pandora)
     * b[22] Sample Playback (JUKE)
     * b[23] MusicPlay Playback (Server)
     * b[24] Capable of Link Distribution
     * b[25] Capable of Add Playlist (Qobuz)
     * b[26] Capable of add MusicCast Playlist With Pandora,
     * b[9] = 1 validates "thumbs_up" / "thumbs_down" / "mark_tired" of managePlay and "why_this_song" of
     * getPlayDescription.
     * b[21] = 1 validates "add_channel_track" / "add_channel_artist" Note: Rhapsody service name will be changed to
     * Napster.
     */
    public Integer getAttribute() {
        return attribute;
    }
}
