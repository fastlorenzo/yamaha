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
package org.openhab.binding.yamahamusiccast.internal.discovery;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jupnp.model.meta.RemoteDevice;
import org.openhab.binding.yamahamusiccast.YamahaMusicCastBindingConstants;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.upnp.UpnpDiscoveryParticipant;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link YamahaDiscoveryParticipant} is responsible for processing the
 * results of searched UPnP devices
 *
 * @author David Graeff - Initial contribution
 * @author Tomasz Maruszak - Introduced config object, migrated to newer UPnP api
 */
@Component(immediate = true)
public class YamahaDiscoveryParticipant implements UpnpDiscoveryParticipant {

    private final Logger logger = LoggerFactory.getLogger(YamahaDiscoveryParticipant.class);

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections
            .singleton(YamahaMusicCastBindingConstants.THING_TYPE_SPEAKER);

    @Override
    public Set<ThingTypeUID> getSupportedThingTypeUIDs() {
        return SUPPORTED_THING_TYPES;
    }

    @Override
    public DiscoveryResult createResult(RemoteDevice device) {
        ThingUID uid = getThingUID(device);
        if (uid == null) {
            return null;
        }

        Map<String, Object> properties = new HashMap<>(4);

        String label = "Yamaha MusicCast Speaker ";
        try {
            label += " " + device.getDetails().getModelDetails().getModelName() + " "
                    + device.getDetails().getFriendlyName();

        } catch (Exception e) {
            // ignore and use the default label
        }

        URL url = device.getIdentity().getDescriptorURL();
        properties.put(YamahaMusicCastBindingConstants.Configs.CONFIG_HOST_NAME, url.getHost());
        properties.put(YamahaMusicCastBindingConstants.Configs.CONFIG_UDN,
                device.getIdentity().getUdn().getIdentifierString());
        properties.put(YamahaMusicCastBindingConstants.DESC_FRIENDLY_NAME, device.getDetails().getFriendlyName());
        properties.put(YamahaMusicCastBindingConstants.DESC_MODEL_NAME,
                device.getDetails().getModelDetails().getModelName());
        properties.put(YamahaMusicCastBindingConstants.DESC_SERIAL_NUMBER, device.getDetails().getSerialNumber());

        // The port via UPNP is unreliable, sometimes it is 8080, on some models 49154.
        // But so far the API was always reachable via port 80.
        // We provide the port config therefore, if the user ever needs to adjust the port.
        // Note the port is set in the thing-types.xml to 80 by default.

        DiscoveryResult result = DiscoveryResultBuilder.create(uid).withTTL(MIN_MAX_AGE_SECS).withProperties(properties)
                .withLabel(label).withRepresentationProperty(YamahaMusicCastBindingConstants.Configs.CONFIG_HOST_NAME)
                .build();

        logger.debug("Discovered a Yamaha Receiver '{}' model '{}' thing with UDN '{}'",
                device.getDetails().getFriendlyName(), device.getDetails().getModelDetails().getModelName(),
                device.getIdentity().getUdn().getIdentifierString());

        return result;
    }

    public static ThingUID getThingUID(String manufacturer, String deviceType, String udn) {
        if (manufacturer == null || deviceType == null) {
            return null;
        }

        if (manufacturer/* .toUpperCase() */.contains(YamahaMusicCastBindingConstants.DESC_MANUFACTURER)
                && deviceType.equals(YamahaMusicCastBindingConstants.DESC_DEVICE_TYPE)) {
            return new ThingUID(YamahaMusicCastBindingConstants.THING_TYPE_SPEAKER, udn);
        } else {
            return null;
        }
    }

    @Override
    public ThingUID getThingUID(RemoteDevice device) {
        String manufacturer = device.getDetails().getManufacturerDetails().getManufacturer();
        String deviceType = device.getType().getType();

        // UDN shouldn't contain '-' characters.
        return getThingUID(manufacturer, deviceType,
                device.getIdentity().getUdn().getIdentifierString().replace("-", "_"));
    }
}
