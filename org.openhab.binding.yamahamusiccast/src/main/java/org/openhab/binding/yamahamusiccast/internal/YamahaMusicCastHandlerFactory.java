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

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
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
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.yamahamusiccast")
public class YamahaMusicCastHandlerFactory extends BaseThingHandlerFactory {
    
    private final static Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections
            .singleton(YamahaMusicCastBindingConstants.THING_TYPE_SPEAKER);
    private final Logger logger = LoggerFactory.getLogger(YamahaMusicCastHandlerFactory.class);
    private YamahaMusicCastEventMediator mediator;

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

/*     @Override
    protected ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_SPEAKER)) {
            return new YamahaMusicCastHandler(thing);
        }

        return null;
    }
 */
    @Override
    protected ThingHandler createHandler(final Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(YamahaMusicCastBindingConstants.THING_TYPE_SPEAKER)) {
            YamahaMusicCastHandler handler;
            logger.debug("Creating a new YamahaMusicCastHandler...");
            try {
                handler = new YamahaMusicCastHandler(thing);
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

    @Override
    public void unregisterHandler(final Thing thing) {
        if (this.mediator != null) {
            this.mediator.unregisterYamahaMusicCastHandlerByThing(thing);
        }
        super.unregisterHandler(thing);
    }

}

