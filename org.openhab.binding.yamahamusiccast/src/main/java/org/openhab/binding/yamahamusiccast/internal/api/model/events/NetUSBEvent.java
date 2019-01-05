/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.yamahamusiccast.internal.api.model.events;

/**
 * Returns Net/USB related information
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class NetUSBEvent extends Event {

    protected Integer play_error;
    protected Integer multiple_play_errors;
    protected String play_message;
    protected boolean account_updated;
    protected Integer play_time;
    protected boolean preset_info_updated;
    protected boolean recent_info_updated;
    protected Object preset_control;
    protected String type;
    protected Integer num;
    protected String result;
    protected Object trial_status;
    protected String input;
    protected boolean enable;
    protected Object trial_time_left;
    protected String input;
    protected Integer time;
    protected boolean play_info_updated;
    protected boolean list_info_updated;

    /*
     * Returns error codes happened during playback for displaying appropriate
     * messages to the external application user interface. If multiple errors
     * happen at the same time, refer to the value of multiple_play_errors sent
     * together for proper messaging
     * Values:
     * 0: No Error
     * 1: Access Error (common for all Net/USB sources)
     * 2: Playback Unavailable (common for all Net/USB sources)
     * 3: Skip Limit Reached (Rhapsody / Napster / Pandora)
     * 4: Invalid Session (Rhapsody / Napster / SiriusXM)
     * 5: High-Resolution File Not Playable at MusicCast Leaf (Server)
     * 6: User Uncredentialed (Qobuz)
     * 7: Track Restricted by Right Holders (Qobuz)
     * 8: Sample Restricted by Right Holders (Qobuz)
     * 9: Genre Restricted by Streaming Credentials (Qobuz)
     * 10: Application Restricted by Streaming Credentials (Qobuz)
     * 11: Intent Restricted by Streaming Credentials (Qobuz)
     * 100: Multiple Errors (common for all Net/USB sources)
     */
    public Integer getPlayError() {
        return play_error;
    }

    /*
     * Returns bit field flags of multiple playback errors. Flags are expressed as
     * OR of bit field. play_error code x is stored as a flag in b[x] shown below.
     * x=0 is reserved for it is for No Error, and x=100 is ignored here b[0]
     * reserved (for it’s No Error) b[1]   Access Error (common for all Net/USB sources)
     * ... b[11]  Intent Restricted by Streaming Credentials (Qobuz) 
     */
    public Integer getMultiplePlayErrors() {
        return multiple_play_errors;
    }

    /*
     * Returns playback related message. Max size is 256 bytes 
     */
    public String getPlayMessage() {
        return play_message;
    }

    /*
     * Returns whether or not account info has changed. If so, pull renewed info
     * using /netusb/getAccountStatus 
     */
    public boolean getAccountUpdated() {
        return account_updated;
    }

    /*
     * Returns current playback time (unit in second) Value: rages -59999 - 59999 
     */
    public Integer getPlayTime() {
        return play_time;
    }

    /*
     * Returns whether or not preset info has changed. If so, pull renewed info
     * using netusb/getPresetInfo 
     */
    public boolean getPresetInfoUpdated() {
        return preset_info_updated;
    }

    /*
     * Returns whether or not playback history info has changed. If so, pull
     * renewed info using /netusb/getRecentInfo  
     */
    public boolean getRecentInfoUpdated() {
        return recent_info_updated;
    }

    /*
     * Returns results of Preset operations   
     */
    public Object getPresetControl() {
        return preset_control;
    }

    /*
     * Returns a type of Preset operations Values: "store" / "clear" / "recall"   
     */
    public String getType() {
        return type;
    }

    /*
     * Returns a Preset number being operated Value: one in the range gotten
     * via /system/getFeatures   
     */
    public Integer getNum() {
        return num;
    }

    /*
     * Returns the result of operation
     * Values:
     * "success" (for all types) /
     * "error" (for all types) /
     * "empty" (only for recall) /
     * "not_found" (only for recall) 
     */
    public String getResult() {
        return result;
    }

    /*
     * Returns trial status of a Device   
     */
    public Object getTrialStatus() {
        return trial_status;
    }

    /*
     * Returns Input IDs related to Net/USB   
     */
    public String getInput() {
        return input;
    }

    /*
     * Returns whether or not trial can be initiated. If false, new trial cannot
     * get started due to a Device in trial status 
     */
    public boolean getEnable() {
        return enable;
    }

    /*
     * Returns remaining time of a trial   
     */
    public Object getTrialTimeLeft() {
        return trial_time_left;
    }

    /*
     * Returns Net/USB related Input IDs   
     */
    public String getInput() {
        return input;
    }

    /*
     * Returns remaining days of trial.
     * 0 means it expires within 24 hours.
     * -1 means it has expired,
     * -2 means no info is retrieved yet from the server 
     */
    public Integer getTime() {
        return time;
    }

    /*
     * Returns whether or not playback info has changed. If so, pull renewed info
     * using /netusb/getPlayInfo
     */
    public boolean getPlayInfoUpdated() {
        return play_info_updated;
    }

    /*
     * Returns whether or not list info has changed. If so, pull renewed info
     * using /netusb/getListInfo 
     */
    public boolean getListInfoUpdated() {
        return list_info_updated;
    }
}
