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

import static com.mytechia.robobo.framework.hri.emotion.Emotion.*;


@DesignerComponent(version = 0,
        description = "Robobo Emotion",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-hri-emotion-module.jar")
public class RoboboEmotion extends AndroidNonvisibleComponent {

    /**
     * Creates a new AndroidNonvisibleComponent.
     *
     * @param container the container that this component will be placed in
     */
    public RoboboEmotion(ComponentContainer container) {
        super(container.$form());
    }


    @SimpleProperty
    public int Happy() {
        return HAPPY.hashCode();
    }


    @SimpleProperty
    public int Sad() {
        return SAD.hashCode();
    }


    @SimpleProperty
    public int Angry() {
        return ANGRY.hashCode();
    }


    @SimpleProperty
    public int Smyling() {
        return SMYLING.hashCode();
    }


    @SimpleProperty
    public int Laughing() {
        return LAUGHING.hashCode();
    }


    @SimpleProperty
    public int Embarrased() {
        return EMBARRASED.hashCode();
    }


    @SimpleProperty
    public int Surprised() {
        return SURPRISED.hashCode();
    }


    @SimpleProperty
    public int InLove() {
        return IN_LOVE.hashCode();
    }


    @SimpleProperty
    public int Normal() {
        return NORMAL.hashCode();
    }


    @SimpleProperty
    public int Sleeping() {
        return SLEEPING.hashCode();
    }


}
