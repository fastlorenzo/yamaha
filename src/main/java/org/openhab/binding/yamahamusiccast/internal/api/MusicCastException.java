/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal.api;

/**
 * The {@link UniFiException} represents a binding specific {@link Exception}.
 *
 * @author Matthew Bowman - Initial contribution
 */
public class MusicCastException extends Exception {

    private static final long serialVersionUID = -7422288981644510570L;

    public MusicCastException(String message) {
        super(message);
    }

    public MusicCastException(String message, Throwable cause) {
        super(message, cause);
    }

    public MusicCastException(Throwable cause) {
        super(cause);
    }
}
