package de.thkoeln.cvogt.android.opengl_utilities;

/**
 * THIS CLASS MIGHT BE USEFUL IN THE FUTURE TO EXTEND THE LIBRARIES WITH ANIMATION UTILITIES.
 * IT HAS BEEN COPIED FROM THE COMPANION PROJECT FOR 2D PROPERTY ANIMATION AND MUST BE ADAPTED.
 *
 * This class provides some utility classes and methods for animation programming.
 */

public class ZZZAnimationUtilsCV {

    /**
     * Listener to remove an object of class AnimatedGuiObjectCV from the
     * view of class AnimationViewCV where it is registered.
     * The listener is registered with an animation by calling
     * the Animator.addListener() method.
     */

    // public static class EndListener_Delete extends AnimatorListenerAdapter {

    /**
     * Listener to remove an object of class AnimatedGuiObjectCV from the
     * view of class AnimationViewCV where it is registered.
     * The object is removed only if it is no more visible after the
     * animation ends - either because the object size is zero
     * or because the object lies now outside of the display.
     * The listener is registered with an animation by calling
     * the Animator.addListener() method.
     */

    // public static class EndListener_DeleteIfNotVisible extends AnimatorListenerAdapter {

    /** Listener to react upon collisions between objects. */

    // public static class PropertyChangedListener_Collision implements AnimatedGuiObjectCV.OnPropertyChangedListener {

         /** Try to find an object with which the object given by the parameter has collided (= with which it overlaps).
         *
         * @param obj The object for which a colliding object shall be found.
         * @return The object with which obj has collided (if any) or null (if there is no collision)
         */

    //    static public AnimatedGuiObjectCV findCollidingObject(AnimatedGuiObjectCV obj) {

    /** Listeners that relocate an object in accordance with the moving finger of the user. */

    // public static class OnTouchListener_Relocation implements AnimationViewCV.OnTouchListener {

}

