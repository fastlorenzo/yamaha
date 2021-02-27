/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal.event;

import java.net.DatagramPacket;

import org.openhab.binding.yamahamusiccast.handler.YamahaMusicCastHandler;
import org.openhab.core.thing.Thing;

/**
 * The {@link YamahaMusicCastEventMediator} is responsible for receiving all the UDP packets and route correctly to
 * each handler.
 *
 * @author Jaime Vaz - Initial contribution
 * @author Hector Rodriguez - Adapted for Musiccast binding
 */
public interface YamahaMusicCastEventMediator {

    /**
     * This method is called by the {@link YamahaMusicCastUpdateReceiverRunnable}, when one new message has been
     * received.
     *
     * @param receivedMessage the {@link SilvercrestWifiSocketResponse} message.
     */
    void processReceivedPacket(final DatagramPacket packet);

    /**
     * Registers a new {@link Thing} and the corresponding {@link SilvercrestWifiSocketHandler}.
     *
     * @param thing the {@link Thing}.
     * @param handler the {@link SilvercrestWifiSocketHandler}.
     */
    void registerThingAndYamahaMusicCastHandler(final Thing thing, final YamahaMusicCastHandler handler);

    /**
     * Unregisters a {@link SilvercrestWifiSocketHandler} by the corresponding {@link Thing}.
     *
     * @param thing the {@link Thing}.
     */
    void unregisterYamahaMusicCastHandlerByThing(final Thing thing);

    /**
     * Returns all the {@link Thing} registered.
     *
     * @returns all the {@link Thing}.
     */
    // Set<Thing> getAllThingsRegistred();

    /**
     * Sets the discovery service to inform the when one new thing has been found.
     *
     * @param discoveryService the discovery service.
     */
    // void setDiscoveryService(SilvercrestWifiSocketDiscoveryService discoveryService);
}
