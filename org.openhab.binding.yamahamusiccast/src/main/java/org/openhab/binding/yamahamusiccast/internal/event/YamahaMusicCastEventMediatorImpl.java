/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal.event;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
//import java.util.Set;
import java.net.DatagramPacket;

import org.eclipse.smarthome.core.thing.Thing;
import org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants;
//import org.openhab.binding.silvercrestwifisocket.internal.discovery.SilvercrestWifiSocketDiscoveryService;
//import org.openhab.binding.silvercrestwifisocket.internal.entities.SilvercrestWifiSocketResponse;
import org.openhab.binding.yamahamusiccast.internal.event.YamahaMusicCastEventReceiver;
import org.openhab.binding.yamahamusiccast.handler.YamahaMusicCastHandler;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link YamahaMusicCastEventMediatorImpl} is responsible for receiving all the UDP packets and route correctly to
 * each handler.
 *
 * @author Jaime Vaz - Initial contribution
 * @author Hector Rodriguez - Adapted for Musiccast binding
 */
@Component(service = YamahaMusicCastEventMediator.class, immediate = true)
public class YamahaMusicCastEventMediatorImpl implements YamahaMusicCastEventMediator {

    private final Logger logger = LoggerFactory.getLogger(YamahaMusicCastEventMediatorImpl.class);

    private final Map<Thing, YamahaMusicCastHandler> handlersRegistredByThing = new HashMap<>();

    private YamahaMusicCastEventReceiver receiver;
    private Thread receiverThread;

//    private SilvercrestWifiSocketDiscoveryService silvercrestDiscoveryService;

    /**
     * Called at the service activation.
     *
     * @param componentContext the componentContext
     */
    @Activate
    protected void activate(final ComponentContext componentContext) {
        logger.debug("Mediator has been activated by OSGI.");
        this.initYamahaMusicCastEventReceiver();
    }

    /**
     * Called at the service deactivation.
     *
     * @param componentContext the componentContext
     */
    @Deactivate
    protected void deactivate(final ComponentContext componentContext) {
        if (this.receiver != null) {
            this.receiver.shutdown();
        }
    }

    /**
     * This method is called by the {@link YamahaMusicCastEventReceiver}, when one new message has been
     * received.
     */
    @Override
    public void processReceivedPacket(final DatagramPacket packet) {
        logger.debug("Received packet from: {} ", packet.getAddress().getHostAddress());//,
//                receivedMessage.getType());

        String message = new String(packet.getData(), 0, packet.getLength());
        YamahaMusicCastHandler handler = this.getHandlerRegistredByHost(packet.getAddress().getHostAddress());

        if (handler != null) {
            // deliver message to handler.
            handler.newReceivedResponseMessage(message);
            logger.debug("Received message delivered with success to handler of host {}",
                    packet.getAddress().getHostAddress());
        } /* else {
            logger.debug("There is no handler registered for mac address:{}", receivedMessage.getMacAddress());
            // notify discovery service of thing found!
            this.silvercrestDiscoveryService.discoveredWifiSocket(receivedMessage.getMacAddress(),
                    receivedMessage.getHostAddress());
        } */
    }

    /**
     * Regists one new {@link Thing} and the corresponding {@link YamahaMusicCastHandler}.
     *
     * @param thing the {@link Thing}.
     * @param handler the {@link YamahaMusicCastHandler}.
     */
    @Override
    public void registerThingAndYamahaMusicCastHandler(final Thing thing, final YamahaMusicCastHandler handler) {
        this.handlersRegistredByThing.put(thing, handler);
    }

    /**
     * Unregists one {@link YamahaMusicCastHandlerYamahaMusicCastHandler} by the corresponding {@link Thing}.
     *
     * @param thing the {@link Thing}.
     */
    @Override
    public void unregisterYamahaMusicCastHandlerByThing(final Thing thing) {
        YamahaMusicCastHandler handler = this.handlersRegistredByThing.get(thing);
        if (handler != null) {
            this.handlersRegistredByThing.remove(thing);
        }
    }

    /**
     * Utilitary method to get the registered thing handler in mediator by the host address.
     *
     * @param macAddress the host address of the thing of the handler.
     * @return {@link YamahaMusicCastHandler} if found.
     */
    private YamahaMusicCastHandler getHandlerRegistredByHost(final String host) {
        YamahaMusicCastHandler searchedHandler = null;
        logger.debug("Looking for handler with IP address: {} ", host);
        for (YamahaMusicCastHandler handler : this.handlersRegistredByThing.values()) {
            logger.debug("Found handler with IP address: {} ", handler.getHost());
            if (host.equals(handler.getHost())) {
                searchedHandler = handler;
                // don't spend more computation. Found the handler.
                break;
            }
        }
        return searchedHandler;
    }

    /**
     * Inits the mediator {@link YamahaMusicCastEventReceiver} thread. This thread is responsible to receive all
     * packets from musiccast devices, and redirect the messages to mediator.
     */
    private void initYamahaMusicCastEventReceiver() {
        // try with handler port if is null
        if ((this.receiver == null) || ((this.receiverThread != null)
                && (this.receiverThread.isInterrupted() || !this.receiverThread.isAlive()))) {
            try {
                this.receiver = new YamahaMusicCastEventReceiver(this,
                        YamahaMusicCastBindingConstants.EVENTS_DEFAULT_PORT);
                this.receiverThread = new Thread(this.receiver);
                this.receiverThread.start();
                logger.debug("Invoked the start of receiver thread.");
            } catch (SocketException e) {
                logger.debug("Cannot start the socket with default port...");
            }
        }
    }

    /**
     * Returns all the {@link Thing} registered.
     *
     * @returns all the {@link Thing}.
     */
/*    @Override
    public Set<Thing> getAllThingsRegistred() {
        return this.handlersRegistredByThing.keySet();
    }

    @Override
    public void setDiscoveryService(final SilvercrestWifiSocketDiscoveryService discoveryService) {
        this.silvercrestDiscoveryService = discoveryService;
    }
*/
}
