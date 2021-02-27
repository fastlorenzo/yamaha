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
 * Returns Clock related information
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class ClockEvent {

    protected Boolean settingsUpdated;

    /*
     * Returns whether or not clock info has changed. If so, pull renewed info using /clock/getSettings
     */
    public Boolean getSettingsUpdated() {
        return settingsUpdated;
    }
}
