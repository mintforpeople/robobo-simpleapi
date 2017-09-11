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

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import static com.mytechia.robobo.framework.hri.sound.noteDetection.Note.*;

/**
 * RoboboNoteGenerationModule provides notes for the RoboboNoteGenerationModule.
 */
@DesignerComponent(version = 0,
        description = "A component that provides notes for the " +
                "SoundExpressionComponent",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-sound.jar")
public class RoboboNote extends AndroidNonvisibleComponent {

    public RoboboNote(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleProperty(description = "C3")
    public int C3() { return C3.hashCode(); }

    @SimpleProperty(description = "C#3")
    public int Cs3() { return Cs3.hashCode(); }

    @SimpleProperty(description = "D3")
    public int D3() { return D3.hashCode(); }

    @SimpleProperty(description = "D#3")
    public int Ds3() { return Ds3.hashCode(); }

    @SimpleProperty(description = "E3")
    public int E3() { return E3.hashCode(); }

    @SimpleProperty(description = "F3")
    public int F3() { return F3.hashCode(); }

    @SimpleProperty(description = "F#3")
    public int Fs3() { return Fs3.hashCode(); }

    @SimpleProperty(description = "G3")
    public int G3() { return G3.hashCode(); }

    @SimpleProperty(description = "G#3")
    public int Gs3() { return Gs3.hashCode(); }

    @SimpleProperty(description = "A3")
    public int A3() { return A3.hashCode(); }

    @SimpleProperty(description = "A#3")
    public int As3() { return As3.hashCode(); }

    @SimpleProperty(description = "B3")
    public int B3() { return B3.hashCode(); }

    @SimpleProperty(description = "C4")
    public int C4() { return C4.hashCode(); }

    @SimpleProperty(description = "C#4")
    public int Cs4() { return Cs4.hashCode(); }

    @SimpleProperty(description = "D4")
    public int D4() { return D4.hashCode(); }

    @SimpleProperty(description = "D#4")
    public int Ds4() { return Ds4.hashCode(); }

    @SimpleProperty(description = "E4")
    public int E4() { return E4.hashCode(); }

    @SimpleProperty(description = "F4")
    public int F4() { return F4.hashCode(); }

    @SimpleProperty(description = "F#4")
    public int Fs4() { return Fs4.hashCode(); }

    @SimpleProperty(description = "G4")
    public int G4() { return G4.hashCode(); }

    @SimpleProperty(description = "G#4")
    public int Gs4() { return Gs4.hashCode(); }

    @SimpleProperty(description = "A4")
    public int A4() { return A4.hashCode(); }

    @SimpleProperty(description = "A#4")
    public int As4() { return As4.hashCode(); }

    @SimpleProperty(description = "B4")
    public int B4() { return B4.hashCode(); }

    @SimpleProperty(description = "C5")
    public int C5() { return C5.hashCode(); }

    @SimpleProperty(description = "C#5")
    public int Cs5() { return Cs5.hashCode(); }

    @SimpleProperty(description = "D5")
    public int D5() { return D5.hashCode(); }

    @SimpleProperty(description = "D#5")
    public int Ds5() { return Ds5.hashCode(); }

    @SimpleProperty(description = "E5")
    public int E5() { return E5.hashCode(); }

    @SimpleProperty(description = "F5")
    public int F5() { return F5.hashCode(); }

    @SimpleProperty(description = "F#5")
    public int Fs5() { return Fs5.hashCode(); }

    @SimpleProperty(description = "G5")
    public int G5() { return G5.hashCode(); }

    @SimpleProperty(description = "G#5")
    public int Gs5() { return Gs5.hashCode(); }

    @SimpleProperty(description = "A5")
    public int A5() { return A5.hashCode(); }

    @SimpleProperty(description = "A#5")
    public int As5() { return As5.hashCode(); }

    @SimpleProperty(description = "B5")
    public int B5() { return B5.hashCode(); }

    @SimpleProperty(description = "C6")
    public int C6() { return C6.hashCode(); }

    @SimpleProperty(description = "C#6")
    public int Cs6() { return Cs6.hashCode(); }

    @SimpleProperty(description = "D6")
    public int D6() { return D6.hashCode(); }

    @SimpleProperty(description = "D#6")
    public int Ds6() { return Ds6.hashCode(); }

    @SimpleProperty(description = "E6")
    public int E6() { return E6.hashCode(); }

    @SimpleProperty(description = "F6")
    public int F6() { return F6.hashCode(); }

    @SimpleProperty(description = "F#6")
    public int Fs6() { return Fs6.hashCode(); }

    @SimpleProperty(description = "G6")
    public int G6() { return G6.hashCode(); }

    @SimpleProperty(description = "G#6")
    public int Gs6() { return Gs6.hashCode(); }

    @SimpleProperty(description = "A6")
    public int A6() { return A6.hashCode(); }

    @SimpleProperty(description = "A#6")
    public int As6() { return As6.hashCode(); }

    @SimpleProperty(description = "B6")
    public int B6() { return B6.hashCode(); }
}
