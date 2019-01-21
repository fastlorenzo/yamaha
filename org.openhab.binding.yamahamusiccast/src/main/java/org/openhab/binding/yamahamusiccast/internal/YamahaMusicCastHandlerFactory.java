/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahamusiccast.internal;

import java.util.Collections;
import java.util.Set;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.audio.AudioHTTPServer;
import org.eclipse.smarthome.core.audio.AudioSink;
import org.eclipse.smarthome.core.net.HttpServiceUtil;
import org.eclipse.smarthome.core.net.NetworkAddressService;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.io.transport.upnp.UpnpIOService;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants;
import org.openhab.binding.yamahamusiccast.handler.YamahaMusicCastHandler;
import org.openhab.binding.yamahamusiccast.internal.event.YamahaMusicCastEventMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link YamahaMusicCastHandlerFactory} is responsible for creating things and thing 
 * handlers.
 * 
 * @author Frank Zimmer - Initial contribution
 * @author Hector Rodriguez Medina - Add event mediator
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.yamahamusiccast")
public class YamahaMusicCastHandlerFactory extends BaseThingHandlerFactory {
    
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(YamahaMusicCastBindingConstants.THING_TYPE_SPEAKER);
    private final Logger logger = LoggerFactory.getLogger(YamahaMusicCastHandlerFactory.class);
    private YamahaMusicCastEventMediator mediator;
    private Map<String, ServiceRegistration<AudioSink>> audioSinkRegistrations = new ConcurrentHashMap<>();
    private UpnpIOService upnpIOService;
    private AudioHTTPServer audioHTTPServer;
    private NetworkAddressService networkAddressService;
    private String callbackUrl = null;

    @Override
    protected void activate(ComponentContext componentContext) {
        super.activate(componentContext);
        Dictionary<String, Object> properties = componentContext.getProperties();
        callbackUrl = (String) properties.get("callbackUrl");
    };

    /**
     * Used by OSGI to inject the mediator in the handler factory.
     *
     * @param mediator the mediator
     */
    @Reference
    public void setMediator(final YamahaMusicCastEventMediator mediator) {
        logger.debug("Mediator has been injected on handler factory service.");
        this.mediator = mediator;
    }

    /**
     * Used by OSGI to unsets the mediator from the handler factory.
     *
     * @param mediator the mediator
     */
    public void unsetMediator(final YamahaMusicCastEventMediator mitsubishiMediator) {
        logger.debug("Mediator has been unsetted from discovery service.");
        this.mediator = null;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(final Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(YamahaMusicCastBindingConstants.THING_TYPE_SPEAKER)) {
            String callbackUrl = createCallbackUrl();
            YamahaMusicCastHandler handler;
            logger.debug("Creating a new YamahaMusicCastHandler...");
            try {
                handler = new YamahaMusicCastHandler(thing, upnpIOService, audioHTTPServer, callbackUrl);
                if (callbackUrl != null) {
                    @SuppressWarnings("unchecked")
                    ServiceRegistration<AudioSink> reg = (ServiceRegistration<AudioSink>) bundleContext
                            .registerService(AudioSink.class.getName(), handler, new Hashtable<>());
                    audioSinkRegistrations.put(thing.getUID().toString(), reg);
                }
                logger.debug("YamahaMusicCastEventMediator will register the handler.");
                if (this.mediator != null) {
                    this.mediator.registerThingAndYamahaMusicCastHandler(thing, handler);
                } else {
                    logger.error(
                            "The mediator is missing on Handler factory. Without one mediator the handler cannot work!");
                    return null;
                }
                return handler;
            } catch (Exception e) {
                logger.debug("The mac address passed to WifiSocketHandler by configurations is not valid.");
            }
        }
        return null;
    }

    private String createCallbackUrl() {
        if (callbackUrl != null) {
            return callbackUrl;
        } else {
            final String ipAddress = networkAddressService.getPrimaryIpv4HostAddress();
            if (ipAddress == null) {
                logger.warn("No network interface could be found.");
                return null;
            }

            // we do not use SSL as it can cause certificate validation issues.
            final int port = HttpServiceUtil.getHttpServicePort(bundleContext);
            if (port == -1) {
                logger.warn("Cannot find port of the http service.");
                return null;
            }

            return "http://" + ipAddress + ":" + port;
        }
    }

    @Override
    public void unregisterHandler(final Thing thing) {
        if (this.mediator != null) {
            this.mediator.unregisterYamahaMusicCastHandlerByThing(thing);
        }
        ServiceRegistration<AudioSink> reg = audioSinkRegistrations.get(thing.getUID().toString());
        if (reg != null) {
            reg.unregister();
        }
        super.unregisterHandler(thing);
    }

    @Reference
    protected void setUpnpIOService(UpnpIOService upnpIOService) {
        this.upnpIOService = upnpIOService;
    }

    protected void unsetUpnpIOService(UpnpIOService upnpIOService) {
        this.upnpIOService = null;
    }

    @Reference
    protected void setAudioHTTPServer(AudioHTTPServer audioHTTPServer) {
        this.audioHTTPServer = audioHTTPServer;
    }

    protected void unsetAudioHTTPServer(AudioHTTPServer audioHTTPServer) {
        this.audioHTTPServer = null;
    }

    @Reference
    protected void setNetworkAddressService(NetworkAddressService networkAddressService) {
        this.networkAddressService = networkAddressService;
    }

    protected void unsetNetworkAddressService(NetworkAddressService networkAddressService) {
        this.networkAddressService = null;
    }
}

