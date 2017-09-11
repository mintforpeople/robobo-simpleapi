/*******************************************************************************
 *
 *   Copyright 2017 Miguel Vilar <mavilarr@gmail.com>
 *
 *   This file is part of Robobo Simple API.
 *
 *   Robobo Simple API is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Simple API is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Simple API.  If not,
 *   see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package com.mytechia.simpleapi.component;

import android.content.Context;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;



public class SimpleRoboboManager implements IComponentListener {

    private static SimpleRoboboManager instance = null;

    private HashMap<Class, ASimpleAPIComponent> mComponents;
    private HashMap<Class, Boolean> mStartedComponents;
    private HashSet<ISimpleRoboboManagerListener> mListeners;


    private SimpleRoboboManager() {
        this.mComponents = new HashMap<>();
        this.mStartedComponents = new HashMap<>();
        this.mListeners = new HashSet<>();
    }


    private SimpleRoboboManager(Context context, Class... component)
            throws ComponentNotFoundException {
        this();
        this.addComponent(context, component);
    }


    public static SimpleRoboboManager getInstance(Context context, Class... component)
            throws ComponentNotFoundException {
        if (instance == null)
            instance = new SimpleRoboboManager(context, component);
        return instance;
    }


    public static SimpleRoboboManager getInstance() {
        if (instance == null)
            instance = new SimpleRoboboManager();
        return instance;
    }


    public void bind(String robName) {
        for (Map.Entry<Class, ASimpleAPIComponent> entry: mComponents.entrySet())
            entry.getValue().bind(robName);
    }


    public void unbind() {
        for (Map.Entry<Class, ASimpleAPIComponent> entry: mComponents.entrySet()) {
            mStartedComponents.put(entry.getKey(), false);
            entry.getValue().unbind();
        }
    }


    public ASimpleAPIComponent getComponentInstance(Class component) {
        return mComponents.get(component);
    }


    public <T> T getComponent(Class<T> component) {
        return component.cast(mComponents.get(component));
    }


    @Override
    public void onComponentStarted(Class component) {
        boolean current = false;
        mStartedComponents.put(component, true);
        Collection<Boolean> started = mStartedComponents.values();
        Iterator<Boolean> i = started.iterator();
        while (i.hasNext() && (current = i.next()));
        if (current)
            for (ISimpleRoboboManagerListener listener: mListeners)
                listener.onComponentsStarted();
    }


    @Override
    public void onComponentError(Class component, String errorMsg) {
        mStartedComponents.put(component, false);
        for (ISimpleRoboboManagerListener listener: mListeners)
            listener.onComponentsError(errorMsg);
    }


    public void addListener(ISimpleRoboboManagerListener listener) {
        mListeners.add(listener);
    }


    public void removeListener(ISimpleRoboboManagerListener listener) {
        mListeners.remove(listener);
    }


    public void addComponent(Context context, Class... component)
            throws ComponentNotFoundException {
        for (Class c: component)
            addComponent(context, c);
    }


    private void addComponent(Context context, Class component)
            throws ComponentNotFoundException {
        mStartedComponents.put(component, false);
        mComponents.put(component, getComponent(context, component));
    }


    public void removeComponent() {
        unbind();
        mComponents = new HashMap<>();
        mStartedComponents = new HashMap<>();
    }


    public void removeComponent(Class component) {
        ASimpleAPIComponent comp = mComponents.get(component);
        if (comp != null) {
            comp.unbind();
            mComponents.remove(component);
        }
    }


    private ASimpleAPIComponent getComponent(Context context, Class component)
            throws ComponentNotFoundException {
        ASimpleAPIComponent APIComponent;
        try {
            Constructor c = component.getConstructor(Context.class, IComponentListener.class);
            APIComponent = (ASimpleAPIComponent) c.newInstance(context, this);
        } catch (Exception e) {
            throw new ComponentNotFoundException(component.getName() + " component not found");
        }
        return APIComponent;
    }


}
