package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Represents a device that can be controlled.
 * User: josef
 * Date: 04/07/13
 * Time: 15:10
 */
public interface Actuatable
{
    /**
     * Activates the device. The activity the device was waiting to perform will start.
     */
    public void activate();

    /**
     * (Forcibly) deactivates the device. The activity the device was performing will stop (aborted).
     */
    public void deactivate();

    /**
     * Suspends the device from performing the current activity.
     * The activity can be resumed at a later stage.
     */
    public void suspend();

    /**
     * Resumes a suspended activity.
     * The device will continue performing the activity until it is completed, or the device is suspended again or deactivated.
     */
    public void resume();
}
