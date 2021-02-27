/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.yamahamusiccast.internal.api.model.events;

/**
 * Returns Tuner related information
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class TunerEvent {

    protected Boolean playInfoUpdated;
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
