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

package com.theroboboproject.simpleapi.aicomponent;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import static com.mytechia.robobo.framework.hri.touch.TouchGestureDirection.*;


@DesignerComponent(version = 0,
        description = "Robobo Touch Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject
@UsesLibraries(libraries = "robobo-hri-touch-module.jar")
public class RoboboTouchDirection extends AndroidNonvisibleComponent {

    /**
     * Creates a new AndroidNonvisibleComponent.
     *
     * @param container the container that this component will be placed in
     */
    public RoboboTouchDirection(ComponentContainer container) {
        super(container.$form());
    }


    @SimpleProperty
    public int Left() {
        return LEFT.hashCode();
    }


    @SimpleProperty
    public int Right() {
        return RIGHT.hashCode();
    }


    @SimpleProperty
    public int Up() {
        return UP.hashCode();
    }


    @SimpleProperty
    public int Down() {
        return DOWN.hashCode();
    }


}
