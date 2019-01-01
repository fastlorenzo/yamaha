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
 */

public class PlayInfo extends Response {
    public static final String url = "/YamahaExtendedControl/v2/netusb/getPlayInfo";

    protected String albumart_url;

    protected String album;

    protected String track;

    public String getAlbumart_url() {
        return albumart_url;
    }

    public String getAlbum() {
        return album;
    }

    public String getTrack() {
        return track;
    }

}
