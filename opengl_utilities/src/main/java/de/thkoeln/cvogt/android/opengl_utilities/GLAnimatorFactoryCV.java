// This work is provided under GPLv3, the GNU General Public License 3
//   http://www.gnu.org/licenses/gpl-3.0.html

// Prof. Dr. Carsten Vogt
// Technische Hochschule Köln, Germany
// Fakultät für Informations-, Medien- und Elektrotechnik
// carsten.vogt@th-koeln.de
// 17.3.2022

package de.thkoeln.cvogt.android.opengl_utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.util.Log;

/**
 * Class with static methods to easily create animators for shapes. It is still rather rudimentary.
 * <BR>
 * An animator returned by the methods of this class can be added to a shape, i.e. an object of class GLShapeCV, through its method addAnimator().
 * The animator will be started automatically when the shape is assigned to a surface view, i.e. an object of class GLSurfaceViewCV, through its method addShape()
 * and then controlled by the renderer of the surface view.
 * <BR>
 * All methods of the Android Java class ObjectAnimator can be applied to the animators, e.g. setInterpolator() to assign a time interpolator to control the timing of the animator.
 * @see de.thkoeln.cvogt.android.opengl_utilities.GLShapeCV
 */

// TODO WEITERE ANIMATOREN - SIEHE DAZU AUCH METHODEN ADDXXXANIMATOR DER KLASSE ANIMATORGUIOBJECTCV IM PROJEKT UTILITIESPROPANIMCV

public class GLAnimatorFactoryCV {

    /**
     * Makes an animator to let the shape make a rotation around the axis defined by its rotAxis attribute.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param angle The rotation angle.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated.
     * @param reverse true if the animation shall be reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorRot(GLShapeCV shape, float angle, int duration, int repeatCount, boolean reverse) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "rotAngle", angle);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        if (reverse)
            animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape make a rotation around its x axis.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param angle The rotation angle.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated.
     * @param reverse true if the animation shall be reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorRotX(GLShapeCV shape, float angle, int duration, int repeatCount, boolean reverse) {
        shape.setRotAxis(1,0,0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "rotAngle", angle);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        if (reverse)
            animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape make a rotation around its y axis.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param angle The rotation angle.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated.
     * @param reverse true if the animation shall be reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorRotY(GLShapeCV shape, float angle, int duration, int repeatCount, boolean reverse) {
        shape.setRotAxis(0,1,0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "rotAngle", angle);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        if (reverse)
          animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape make a rotation around its z axis.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param angle The rotation angle.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated.
     * @param reverse true if the animation shall be reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorRotZ(GLShapeCV shape, float angle, int duration, int repeatCount, boolean reverse) {
        shape.setRotAxis(0,0,1);
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "rotAngle", angle);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        if (reverse)
          animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape move to a specific x position.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param targetX The target x position of the animation.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated. If repeatCount is greater than 1, the animation is reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorTransX(GLShapeCV shape, float targetX, int duration, int repeatCount) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "transX", targetX);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape move to a specific y position.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param targetY The target y position of the animation.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated. If repeatCount is greater than 1, the animation is reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorTransY(GLShapeCV shape, float targetY, int duration, int repeatCount) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "transY", targetY);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape move to a specific z position.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param targetZ The target z position of the animation.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated. If repeatCount is greater than 1, the animation is reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorTransZ(GLShapeCV shape, float targetZ, int duration, int repeatCount) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(shape, "transZ", targetZ);
        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to let the shape move to a specific (x,y,z) position.
     * Adds the new animator to the animators of the given shape.
     * @param shape The shape to animate.
     * @param targetX The target x position of the animation.
     * @param targetY The target y position of the animation.
     * @param targetZ The target z position of the animation.
     * @param duration The duration of the animation.
     * @param repeatCount The number of times the animation shall be repeated. If repeatCount is greater than 1, the animation is reversed.
     * @return The newly generated animator.
     */

    public static ObjectAnimator addAnimatorTrans(GLShapeCV shape, float targetX, float targetY, float targetZ, int duration, int repeatCount) {

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(shape,PropertyValuesHolder.ofFloat("transX",targetX),
                PropertyValuesHolder.ofFloat("transY",targetY),PropertyValuesHolder.ofFloat("transZ",targetZ));

        animator.setDuration(duration);
        animator.setRepeatCount(repeatCount);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        shape.addAnimator(animator);
        return animator;
    }

    /**
     * Makes an animator to move the shape in a plane with constant z (i.e. a vertical plane) along an arc around a specified center.
     * Adds the new animator to the animators of the given shape.
     *
     * @param centerCircleX The x coordinate of the circle center.
     * @param centerCircleY The y coordinate of the circle center.
     * @param angle The angle of the arc to be traversed.
     * @param clockwise Movement clockwise or anticlockwise?
     * @param duration The duration of the animation (in ms).
     * @param startDelay The start delay of the animation (in ms).
     * @return The new animator
     */

    public static ObjectAnimator addAnimatorArcPathInVerticalPlane(GLShapeCV shape, float centerCircleX, float centerCircleY, float angle, boolean clockwise, int duration, int startDelay) {
        if (clockwise) angle = -angle;
        float[] center = new float[3];
        center[0] = centerCircleX;
        center[1] = centerCircleY;
        center[2] = shape.getTransZ();
        EvaluatorArcPathInVerticalPlane eval = new EvaluatorArcPathInVerticalPlane(shape,center,angle);
        float[] dummy = new float[3];
        ObjectAnimator anim = ObjectAnimator.ofObject(shape,"trans",eval,dummy,dummy);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        shape.addAnimator(anim);
        return anim;
    }

    /**
     * Class that defines a TypeEvaluator for a movement along an arc in a vertical plane - see method addAnimatorArcPathInVerticalPlane().
     */

    private static class EvaluatorArcPathInVerticalPlane implements TypeEvaluator<float[]> {
        GLShapeCV shape;  // shape to be animated
        float[] center;  // center of the circle - same z value as the shape
        double angle;  // angle of the arc to traverse
        float[] start;  // start position of the shape
        boolean attributeIsValid;

        // The start position of the animated shape is stored when the animation starts.
        // 'attributeIsValid' specifies if this has been done already.
        EvaluatorArcPathInVerticalPlane(GLShapeCV shape, float[] center, double angle) {
            this.shape = shape;
            this.center = center.clone();
            this.angle = angle;
            this.attributeIsValid = false;
        }

        public float[] evaluate(float f, float dummy1[], float[] dummy2) {
            if (!attributeIsValid) {
                start = shape.getTrans();
                attributeIsValid = true;
            }
            float[] retval = new float[3];
            retval[0] = center[0] + (float) ((start[0] - center[0]) * Math.cos(f * angle * Math.PI / 180) - (start[1] - center[1]) * Math.sin(f * angle * Math.PI / 180));
            retval[1] = center[1] + (float) ((start[0] - center[0]) * Math.sin(f * angle * Math.PI / 180) + (start[1] - center[1]) * Math.cos(f * angle * Math.PI / 180));
            retval[2] = start[2];
            return retval;
        }

    }

    /**
     * Makes an animator to move the shape along an arc around an axis that is specified by two points in 3D space.
     * Adds the new animator to the animators of the given shape.
     *
     * @param axisPoint1 The first point defining the axis.
     * @param axisPoint2 The second point defining the axis.
     * @param angle The angle of the arc to be traversed.
     * @param clockwise Movement clockwise or anticlockwise?
     * @param duration The duration of the animation (in ms).
     * @param startDelay The start delay of the animation (in ms).
     * @return The new animator
     */

    public static ObjectAnimator addAnimatorArcPathAroundAxis(GLShapeCV shape, float[] axisPoint1, float[] axisPoint2, float angle, boolean clockwise, int duration, int startDelay) {
        if (clockwise) angle = -angle;
        EvaluatorArcPathAroundAxis eval = new EvaluatorArcPathAroundAxis(shape,axisPoint1,axisPoint2,angle);
        float[] dummy = new float[3];
        ObjectAnimator anim = ObjectAnimator.ofObject(shape,"trans",eval,dummy,dummy);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        shape.addAnimator(anim);
        return anim;
    }

    /**
     * Class that defines a TypeEvaluator for a movement along an arc around an axis - see method addAnimatorArcPathAroundAxis().
     */

    private static class EvaluatorArcPathAroundAxis implements TypeEvaluator<float[]> {
        GLShapeCV shape;  // shape to be animated
        float[] axisPoint1;  // first point on the axis
        float[] axisPoint2;  // second point on the axis
        double angle;  // angle of the arc to traverse
        float[] start;  // start position of the shape
        boolean attributeIsValid;

        // The start position of the animated shape is stored when the animation starts.
        // 'attributeIsValid' specifies if this has been done already.
        EvaluatorArcPathAroundAxis(GLShapeCV shape, float[] axisPoint1, float[] axisPoint2, double angle) {
            this.shape = shape;
            this.axisPoint1 = axisPoint1.clone();
            this.axisPoint2 = axisPoint2.clone();
            this.angle = angle;
            this.attributeIsValid = false;
        }

        public float[] evaluate(float f, float dummy1[], float[] dummy2) {
            if (!attributeIsValid) {
                start = shape.getTrans();
                attributeIsValid = true;
            }
            float[] retval = GraphicsUtilsCV.rotateAroundAxis(start,axisPoint1,axisPoint2,(float)(f*angle));
            return retval;
        }

    }

    /**
     * Makes an animator to move the shape in a spiral around a specified center.
     * The spiral is centered around a vertical line that goes through a "spiral center" with given x and z coordinates.
     * Adds the new animator to the animators of the given shape.
     *
     * @param centerSpiralX The x coordinate of the circle center
     * @param centerSpiralZ The z coordinate of the circle center
     * @param height        The height of the spiral, i.e. the maximum y to be reached
     * @param noTurns       The number of turns of the spiral.
     * @param clockwise     Spiral clockwise or anticlockwise?
     * @param duration      The duration of the animation (in ms)
     * @param startDelay    The start delay of the animation (in ms)
     * @return The new animator
     */

    public static ObjectAnimator addAnimatorSpiralPath(GLShapeCV shape, float centerSpiralX, float centerSpiralZ, float height, int noTurns, boolean clockwise, int duration, int startDelay) {
        float angle = noTurns*360f;
        if (!clockwise) angle = -angle;
        float[] center = new float[3];
        center[0] = centerSpiralX;
        center[1] = shape.getTransY();
        center[2] = centerSpiralZ;
        EvaluatorSpiralPath eval = new EvaluatorSpiralPath(shape, center, height, angle);
        float[] dummy = new float[3];
        ObjectAnimator anim = ObjectAnimator.ofObject(shape, "trans", eval, dummy, dummy);
        anim.setDuration(duration);
        anim.setStartDelay(startDelay);
        shape.addAnimator(anim);
        return anim;
    }

    /**
     * Class that defines a TypeEvaluator for a movement along a spiral - see method addAnimatorSpiralPath().
     */

    private static class EvaluatorSpiralPath implements TypeEvaluator<float[]> {

        GLShapeCV shape;  // shape to be animated
        float[] center;   // center of the spiral - the same y value as the initial y value of the shape
        float[] start;    // start position of the shape
        float height;     // height of the spiral to traverse
        float angle;      // angle of the spiral to traverse
        boolean attributeIsValid;

        // The start position of the animated shape is stored when the animation starts.
        // 'attributeIsValid' specifies if this has been done already.
        EvaluatorSpiralPath(GLShapeCV shape, float[] center, float height, float angle) {
            this.shape = shape;
            this.center = center.clone();
            this.height = height;
            this.angle = angle;
            this.attributeIsValid = false;
        }

        public float[] evaluate(float f, float dummy1[], float[] dummy2) {
            if (!attributeIsValid) {
                start = shape.getTrans();
                attributeIsValid = true;
            }
            float[] retval = new float[3];
            retval[0] = center[0] + (float) ((start[0] - center[0]) * Math.cos(f * angle * Math.PI / 180) - (start[2] - center[2]) * Math.sin(f * angle * Math.PI / 180));
            retval[1] = start[1] + f * height;
            retval[2] = center[2] + (float) ((start[0] - center[0]) * Math.sin(f * angle * Math.PI / 180) + (start[2] - center[2]) * Math.cos(f * angle * Math.PI / 180));
            return retval;
        }

    }

    /**
     * Class for listeners that shall be executed when an animation ends
     * and that will remove the animated shape from the surface view.
     */

    public static class EndListenerRemove extends AnimatorListenerAdapter {
        private GLShapeCV shape;
        private GLSurfaceViewCV surfaceView;
        public EndListenerRemove(GLShapeCV shape, GLSurfaceViewCV surfaceView) {
            this.shape = shape;
            this.surfaceView = surfaceView;
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            surfaceView.removeShape(shape);
        }
    }

  /*
    public static ObjectAnimator makeAnimatorTrans(GLShapeCV shape, float[] transStart, float[] transEnd, int duration, int repeatCount) {

        // MIT ofMultiFloat FUNKTIONIERT ES NICHT: DER LISTENER RUFT DIE METHODE setTrans() NICHT AUF

        // Log.v("DEMO","makeAnimatorTrans: "+transStart[0]+" "+transStart[1]+" "+transStart[2]+" "+transEnd[0]+" "+transEnd[1]+" "+transEnd[2]);

        float startEnd[][] = new float[2][];
        startEnd[0] = transStart;
        startEnd[1] = transEnd;
        // ObjectAnimator animator = ObjectAnimator.ofMultiFloat(shape, "trans", startEnd);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(shape,PropertyValuesHolder.ofFloat("transX",2),
                PropertyValuesHolder.ofFloat("transY",10),PropertyValuesHolder.ofFloat("transZ",-10));

        // ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(shape,PropertyValuesHolder.ofFloat("transZ",-4));
        animator.setDuration(duration);

        // animator.setRepeatCount(repeatCount);
        // animator.setRepeatMode(ValueAnimator.REVERSE);
        // Log.v("DEMO","makeAnimatorTrans: "+startEnd[0][0]+" "+startEnd[0][1]+" "+startEnd[0][2]+"  "+startEnd[1][0]+" "+startEnd[1][1]+" "+startEnd[1][2]+" ");
        return animator;
    }

   */

}
