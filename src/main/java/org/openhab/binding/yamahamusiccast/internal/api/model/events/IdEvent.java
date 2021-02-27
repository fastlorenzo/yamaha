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
 * Return s Device ID.ID is same value using /system/getDeviceInfo Note:
 * Available on and after API Version 1.17
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class IdEvent {

    protected String deviceId;

    public String getDeviceId() {
        return deviceId;
    }
}
