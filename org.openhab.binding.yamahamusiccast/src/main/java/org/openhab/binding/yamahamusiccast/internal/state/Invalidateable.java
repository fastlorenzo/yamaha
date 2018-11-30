package org.openhab.binding.yamahamusiccast.internal.state;
/**
 * Represent object whose state can be invalidated.
 *
 * @author Tomasz Maruszak - [yamaha] Tuner band selection and preset feature for dual band models (RX-S601D)
 */
public interface Invalidateable {

    /**
     * Invalidate the object
     */
    void invalidate();
}