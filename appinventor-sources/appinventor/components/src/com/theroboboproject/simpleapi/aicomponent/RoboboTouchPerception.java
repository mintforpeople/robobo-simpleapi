package com.theroboboproject.simpleapi.aicomponent;

import android.util.Log;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.mytechia.robobo.framework.hri.touch.TouchGestureDirection;
import com.mytechia.simpleapi.component.ITouchPerceptionListener;
import com.mytechia.simpleapi.component.TouchPerceptionComponent;

/**
 * Created by watxinango on 7/19/17.
 */
@DesignerComponent(version = 0,
        description = "Robobo Touch Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject
@UsesLibraries(libraries = "robobo-hri-touch-module.jar," +
        "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar"
)
public class RoboboTouchPerception extends RoboboComponent
        implements ITouchPerceptionListener {

    private static final String LOG_TAG = "AITouchPerception";
    private TouchPerceptionComponent touchComponent;


    public RoboboTouchPerception(ComponentContainer container) {
        super(container, TouchPerceptionComponent.class, LOG_TAG);
    }


    @SimpleFunction
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    public void shutdown() {
        if (touchComponent != null)
            touchComponent.unbind();
        super.shutdown();
    }


    @Override
    public void onComponentsStarted() {
        touchComponent = (TouchPerceptionComponent) getComponentInstance();
        touchComponent.addListener(this);
        super.onComponentsStarted();
        //EventDispatcher.dispatchEvent(this, "onComponentsStarted");
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }



    @Override
    public void onComponentsError(String errorMsg) {
        if (touchComponent != null) {
            touchComponent.removeListener(this);
            touchComponent = null;
        }
        super.onComponentsError(errorMsg);
        //EventDispatcher.dispatchEvent(this, "onComponentsError", errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleEvent
    @Override
    public void tap(int x, int y) {
        Log.d(LOG_TAG, "tap()");
        EventDispatcher.dispatchEvent(this, "tap", x, y);
    }


    @SimpleEvent
    @Override
    public void touch(int x, int y) {
        Log.d(LOG_TAG, "touch()");
        EventDispatcher.dispatchEvent(this, "touch", x, y);
    }


    @SimpleEvent
    public void Fling(int touchDirection, double angle, long time,
                      double distance) {
        EventDispatcher.dispatchEvent(this, "Fling",
                touchDirection, angle, time, distance);
    }


    @Override
    public void fling(TouchGestureDirection touchDirection,
                      double angle, long time, double distance) {
        Log.d(LOG_TAG, "fling()");
        Fling(touchDirection.hashCode(), angle, time, distance);
    }


    @SimpleEvent
    public void Caress(int touchDirection) {
        EventDispatcher.dispatchEvent(this, "Caress", touchDirection);
    }


    @Override
    public void caress(TouchGestureDirection touchDirection) {
        Log.d(LOG_TAG, "caress()");
        Caress(touchDirection.hashCode());
    }


}
