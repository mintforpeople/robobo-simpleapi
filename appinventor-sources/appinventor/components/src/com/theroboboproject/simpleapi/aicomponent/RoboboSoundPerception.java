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

import android.util.Log;
import android.util.SparseArray;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.mytechia.robobo.framework.hri.sound.noteDetection.Note;
import com.mytechia.simpleapi.SoundPerceptionAction;
import com.mytechia.simpleapi.component.ISoundPerceptionListener;
import com.mytechia.simpleapi.component.SoundPerceptionComponent;
import com.mytechia.simpleapi.util.ComActUtil;


@DesignerComponent(version = 0,
        description = "Robobo Sound Perception",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject
@UsesLibraries(libraries = "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar," + "robobo-sound.jar," +
        "robobo-speech.jar")
public class RoboboSoundPerception extends RoboboComponent
        implements ISoundPerceptionListener {

    private static final String LOG_TAG = "AISoundPercepComponent";
    private final SparseArray<Enum> noteValues;

    private SoundPerceptionComponent soundComponent;


    public RoboboSoundPerception(ComponentContainer container) {
        super(container.$form(), SoundPerceptionComponent.class, LOG_TAG);
        this.noteValues = ComActUtil.enum2HashCode(SoundPerceptionAction
                .values());
    }


    @SimpleFunction
    @Override
    public void startup(String robName) {
        super.startup(robName);
    }


    @SimpleFunction
    @Override
    public void shutdown() {
        if (soundComponent != null)
            soundComponent.unbind();
        super.shutdown();
    }


    @Override
    public void onComponentsStarted() {
        soundComponent = (SoundPerceptionComponent) getComponentInstance();
        soundComponent.addListener(this);
        super.onComponentsStarted();
    }


    @SimpleEvent
    @Override
    public void ComponentsStarted() {

    }


    @Override
    public void onComponentsError(String errorMsg) {
        if (soundComponent != null) {
            soundComponent.removeListener(this);
            soundComponent = null;
        }
        super.onComponentsError(errorMsg);
    }


    @SimpleEvent
    @Override
    public void ComponentsError(String errorMsg) {

    }


    @SimpleEvent
    @Override
    public void onClap(double duration) {
        Log.d(LOG_TAG, "onClap(): " + Double.toString(duration));
        EventDispatcher.dispatchEvent(this, "onClap", duration);
    }


    @SimpleEvent
    @Override
    public void onPitchDetected(double freq) {
        Log.d(LOG_TAG, "onPitchDetected(): " + Double.toString(freq));
        EventDispatcher.dispatchEvent(this, "onPitchDetected", freq);
    }


    @Override
    public void onNoteDetected(Note note) {
        Log.d(LOG_TAG, "onNoteDetected(): " + note.name());
        EventDispatcher.dispatchEvent(this, "NoteDetected", note.hashCode());
    }


    @SimpleEvent
    public void NoteDetected(int note) {

    }


    @Override
    public void onNoteEnd(Note note, long duration) {
        Log.d(LOG_TAG, "onNoteEnd(): " + note.name() + ": " +
                Long.toString(duration));
        EventDispatcher.dispatchEvent(this, "NoteEnd", note.hashCode(), duration);
    }


    @SimpleEvent
    public void NoteEnd(int note, long duration) {

    }


    @Override
    public void onNewNote(Note note) {
        Log.d(LOG_TAG, "onNewNote(): " + note.name());
        EventDispatcher.dispatchEvent(this, "NewNote", note.hashCode());
    }


    @SimpleEvent
    public void NewNote(int note) {

    }


}
