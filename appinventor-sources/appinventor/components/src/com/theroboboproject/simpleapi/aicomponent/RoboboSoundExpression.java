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

import android.os.RemoteException;
import android.util.SparseArray;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.mytechia.robobo.framework.hri.sound.noteDetection.Note;
import com.mytechia.simpleapi.component.ISoundExpressionListener;
import com.mytechia.simpleapi.component.SoundExpressionComponent;
import com.mytechia.simpleapi.util.ComActUtil;



@DesignerComponent(version = 0,
        description = "Robobo Sound Expression",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesLibraries(libraries = "robobo-simple-api-client-module.jar," +
        "robobo-simple-api-module.jar," + "robobo-sound.jar," +
        "robobo-speech.jar")
public class RoboboSoundExpression extends RoboboComponent
        implements ISoundExpressionListener {

    private static final String LOG_TAG = "AISoundExprComponent";
    private final SparseArray<Enum> noteValues;

    private SoundExpressionComponent soundComponent;


    public RoboboSoundExpression(ComponentContainer container) {
        super(container.$form(), SoundExpressionComponent.class, LOG_TAG);
        this.noteValues = ComActUtil.enum2HashCode(Note.values());
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
        soundComponent = (SoundExpressionComponent) getComponentInstance();
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


    @SimpleFunction
    public void playEmotionSound(int sound) {
        String functionName = "playEmotionSound";
        try {
            soundComponent.playEmotionSound(sound);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void playNote(int note, int timems) {
        String functionName = "playNote";
        try {
            soundComponent.playNote((Note) noteValues.get(note), timems);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void addNoteToSequence(int note, int timems) {
        String functionName = "addNoteToSequence";
        try {
            soundComponent.addNoteToSequence((Note) noteValues.get(note),
                                                timems);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void playSequence() {
        String functionName = "playSequence";
        try {
            soundComponent.playSequence();
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleFunction
    public void sayText(String text) {
        String functionName = "sayText";
        try {
            soundComponent.sayText(text, 1);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleProperty
    public void selectVoice(String voice) {
        String functionName = "selectVoice";
        try {
            soundComponent.selectVoice(voice);
        } catch (RemoteException e) {
            form.dispatchErrorOccurredEvent(this, functionName,
                    ErrorMessages.ERROR_SIMPLE_API_SERVICE_NOT_AVALIABLE);
        }
    }


    @SimpleEvent
    @Override
    public void onNotePlayEnd() {
        EventDispatcher.dispatchEvent(this, "onNotePlayEnd");
    }


    @SimpleEvent
    @Override
    public void onSequencePlayEnd() {
        EventDispatcher.dispatchEvent(this, "onSequencePlayEnd");
    }


    @SimpleEvent
    @Override
    public void voiceNotFound(String errorMsg) {
        String functionName = "voiceNotFound";
        form.dispatchErrorOccurredEvent(this, functionName,
                ErrorMessages.ERROR_ROBOBO_VOICE_NOT_FOUND, errorMsg);
    }


    @SimpleEvent
    @Override
    public void onEndOfSpeech() {
        EventDispatcher.dispatchEvent(this, "onEndOfSpeech");
    }


}
