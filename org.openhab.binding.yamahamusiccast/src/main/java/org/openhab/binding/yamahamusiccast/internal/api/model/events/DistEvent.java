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
 * Returns Link distribution related information 
 *
 * @author Hector Rodriguez Medina - Initial contribution
 */
public class DistEvent {

    protected Boolean distInfoUpdated;

    /*
     * Returns whether or not Link distribution Device info has changed. If so, pull renewed info using /dist/getDistributionInfo
     */
    public Boolean getDistInfoUpdated() {
        return distInfoUpdated;
    }
}
