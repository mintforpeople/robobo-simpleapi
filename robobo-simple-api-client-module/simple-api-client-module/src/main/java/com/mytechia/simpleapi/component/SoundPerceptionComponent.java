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
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.robobo.framework.hri.sound.noteDetection.Note;
import com.mytechia.simpleapi.SoundPerceptionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.HashSet;

import static com.mytechia.simpleapi.ServiceCommand.SOUND_PERCEPTION;
import static com.mytechia.simpleapi.SoundPerceptionAction.ON_CLAP;
import static com.mytechia.simpleapi.SoundPerceptionAction.ON_NEW_NOTE;
import static com.mytechia.simpleapi.SoundPerceptionAction.ON_NOTE_DETECTED;
import static com.mytechia.simpleapi.SoundPerceptionAction.ON_NOTE_END;
import static com.mytechia.simpleapi.SoundPerceptionAction.ON_PITCH_DETECTED;



public class SoundPerceptionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "SoundPercepComponent";
    private final SparseArray<Enum> mActionValues;
    private HashSet<ISoundPerceptionListener> listeners;


    public SoundPerceptionComponent(Context context, IComponentListener listener) {
        super(context, SOUND_PERCEPTION, SoundPerceptionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(SoundPerceptionAction.values());
        this.listeners = new HashSet<>();
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        SoundPerceptionAction action = (SoundPerceptionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleClientMessage(): " + action.name());
        switch (action) {
            case ON_CLAP:
                onClap(msg.getData());
                break;

            case ON_PITCH_DETECTED:
                onPitchDetected(msg.getData());
                break;

            case ON_NOTE_DETECTED:
                onNoteDetected(msg.getData());
                break;

            case ON_NOTE_END:
                onNoteEnd(msg.getData());
                break;

            case ON_NEW_NOTE:
                onNewNote(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void addListener(ISoundPerceptionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(ISoundPerceptionListener listener) {
        listeners.remove(listener);
    }


    private void onClap(Bundle data) {
        Double time = data.getDouble(ON_CLAP.getParameters().getParamKey(0));
        for (ISoundPerceptionListener listener: listeners)
            listener.onClap(time);
    }


    private void onPitchDetected(Bundle data) {
        Double freq = data.getDouble(ON_PITCH_DETECTED.getParameters().getParamKey(0));
        for (ISoundPerceptionListener listener: listeners)
            listener.onPitchDetected(freq);
    }


    private void onNoteDetected(Bundle data) {
        Note note = Note.valueOf(data.getString(ON_NOTE_DETECTED.getParameters().getParamKey(0)));
        for (ISoundPerceptionListener listener: listeners)
            listener.onNoteDetected(note);
    }


    private void onNoteEnd(Bundle data) {
        Note note = Note.valueOf(data.getString(ON_NOTE_END.getParameters().getParamKey(0)));
        Long time = data.getLong(ON_NOTE_END.getParameters().getParamKey(1));
        for (ISoundPerceptionListener listener: listeners)
            listener.onNoteEnd(note, time);
    }


    private void onNewNote(Bundle data) {
        Note note = Note.valueOf(data.getString(ON_NEW_NOTE.getParameters().getParamKey(0)));
        for (ISoundPerceptionListener listener: listeners)
            listener.onNewNote(note);
    }


}