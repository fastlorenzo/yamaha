/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.yamahamusiccast.internal;

/**
 * ThingConfig
 *
 * @author Dries Decock - Initial contribution
 */

public class YamahaMusicCastThingConfig {

    /**
     * RefreshInterval in seconds.
     */
    private int refresh = 3600;

    private String host = "yamaha";

    public String getHost() {
        return host;
    }

    public int getRefreshInterval() {
        return refresh;
    }

}
