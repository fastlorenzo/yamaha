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
 * Return s Device ID.ID is same value using /system/getDeviceInfo Note:
 * Available on and after API Version 1.17
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class IdEvent {

    @SerializedName("device_id")
    protected String deviceId;

    public String getDeviceId() {
        return deviceId;
    }
}
