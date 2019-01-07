/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal.api.model.events;

import com.google.gson.annotations.SerializedName;

/**
 * Returns Tuner related information 
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class TunerEvent {

    @SerializedName("play_info_updated")
    protected Boolean playInfoUpdated;
    @SerializedName("preset_info_updated")
    protected Boolean presetInfoUpdated;

    /*
     * Returns whether or not playback info has changed. If so, pull renewed info
     * using /tuner/getPlayInfo 
     */
    public Boolean getPlayInfoUpdated() {
        return playInfoUpdated;
    }

    /*
     * Returns whether or not preset info has changed. If so, pull renewed info
     * using /tuner/getPresetInfo 
     */
    public Boolean getPresetInfoUpdated() {
        return presetInfoUpdated;
    }
}