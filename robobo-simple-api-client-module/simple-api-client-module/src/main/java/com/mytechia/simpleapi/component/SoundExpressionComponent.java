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
import android.os.Parcelable;
import android.os.RemoteException;
import android.speech.tts.Voice;
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.robobo.framework.hri.sound.noteGeneration.Note;
import com.mytechia.simpleapi.SoundExpressionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.HashSet;
import java.util.Locale;

import static com.mytechia.simpleapi.ServiceCommand.SOUND_EXPRESSION;
import static com.mytechia.simpleapi.SoundExpressionAction.ADD_NOTE_TO_SEQUENCE;
import static com.mytechia.simpleapi.SoundExpressionAction.GET_STRING_VOICES;
import static com.mytechia.simpleapi.SoundExpressionAction.GET_TTS_VOICES;
import static com.mytechia.simpleapi.SoundExpressionAction.PLAY_NOTE;
import static com.mytechia.simpleapi.SoundExpressionAction.PLAY_SEQUENCE;
import static com.mytechia.simpleapi.SoundExpressionAction.PLAY_SOUND;
import static com.mytechia.simpleapi.SoundExpressionAction.SAY_TEXT;
import static com.mytechia.simpleapi.SoundExpressionAction.SELECT_TTS_VOICE;
import static com.mytechia.simpleapi.SoundExpressionAction.SELECT_VOICE;
import static com.mytechia.simpleapi.SoundExpressionAction.SET_LOCALE;



public class SoundExpressionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "SoundExprComponent";
    private SparseArray<Enum> mActionValues;
    private HashSet<ISoundExpressionListener> listeners;

    private Voice[] ttsVoices;
    private String[] strVoices;


    public SoundExpressionComponent(Context context, IComponentListener listener) {
        super(context, SOUND_EXPRESSION, SoundExpressionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(SoundExpressionAction.values());
        this.listeners = new HashSet<>();
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        SoundExpressionAction action = (SoundExpressionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleHandlerMessage(): " + action.name());
        switch (action) {
            case ON_NOTE_PLAY_END:
                for (ISoundExpressionListener listener: listeners)
                    listener.onNotePlayEnd();
                break;

            case ON_SEQUENCE_PLAY_END:
                for (ISoundExpressionListener listener: listeners)
                    listener.onSequencePlayEnd();
                break;

            case ON_END_OF_SPEECH:
                for (ISoundExpressionListener listener: listeners)
                    listener.onEndOfSpeech();
                break;

            case GET_STRING_VOICES:
                Bundle data = msg.getData();
                strVoices = data.getStringArray(GET_STRING_VOICES.getParameters().getReturnKey());
                break;

            case GET_TTS_VOICES:
                setTtsVoices(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() throws RemoteException {
        sendHandler(GET_STRING_VOICES);
        sendHandler(GET_TTS_VOICES);
    }


    public void addListener(ISoundExpressionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(ISoundExpressionListener listener) {
        listeners.remove(listener);
    }


    public void playEmotionSound(int sound) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(PLAY_SOUND.getParameters().getParamKey(0), sound);
        sendHandler(PLAY_SOUND, params);
    }


    public void playNote(Note note, int timems) throws RemoteException {
        playNote(note.name(), timems);
    }


    public void playNote(com.mytechia.robobo.framework.hri.sound.noteDetection.Note note,
                         int timems) throws RemoteException {
        playNote(note.name(), timems);
    }


    private void playNote(String note, int timems) throws RemoteException {
        Bundle params = new Bundle();
        params.putString(PLAY_NOTE.getParameters().getParamKey(0), note);
        params.putInt(PLAY_NOTE.getParameters().getParamKey(1), timems);
        sendHandler(PLAY_NOTE, params);
    }


    public void addNoteToSequence(Note note, int timems) throws RemoteException {
        addNoteToSequence(note.name(), timems);
    }


    public void addNoteToSequence(com.mytechia.robobo.framework.hri.sound.noteDetection.Note note,
                                  int timems) throws RemoteException {
        addNoteToSequence(note.name(), timems);
    }


    private void addNoteToSequence(String note, int timems) throws RemoteException {
        Bundle params = new Bundle();
        params.putString(ADD_NOTE_TO_SEQUENCE.getParameters().getParamKey(0), note);
        params.putInt(ADD_NOTE_TO_SEQUENCE.getParameters().getParamKey(1), timems);
        sendHandler(ADD_NOTE_TO_SEQUENCE, params);
    }


    public void playSequence() throws RemoteException {
        sendHandler(PLAY_SEQUENCE);
    }


    public void sayText(String text, int priority) throws RemoteException {
        Bundle params = new Bundle();
        params.putString(SAY_TEXT.getParameters().getParamKey(0), text);
        params.putInt(SAY_TEXT.getParameters().getParamKey(1), priority);
        sendHandler(SAY_TEXT, params);
    }


    public void setLocale(Locale newLoc) throws RemoteException {
        Bundle params = new Bundle();
        params.putSerializable(SET_LOCALE.getParameters().getParamKey(0), newLoc);
        sendHandler(SET_LOCALE, params);
    }


    public void selectVoice(String voice) throws RemoteException {
        Bundle params = new Bundle();
        params.putString(SELECT_VOICE.getParameters().getParamKey(0), voice);
        sendHandler(SELECT_VOICE, params);
    }


    public void selectVoice(Voice voice) throws RemoteException {
        Bundle params = new Bundle();
        params.putParcelable(SELECT_TTS_VOICE.getParameters().getParamKey(0), voice);
        sendHandler(SELECT_TTS_VOICE, params);
    }


    public String[] getStringVoices() {
        return strVoices;
    }


    public Voice[] getTtsVoices() {
        return ttsVoices;
    }


    private void setTtsVoices(Bundle data) {
        Parcelable[] vo = data.getParcelableArray(GET_TTS_VOICES.getParameters().getReturnKey());
        Voice[] voices = new Voice[vo.length];
        for (int i = 0; i < voices.length; i++)
            voices[i] = (Voice) vo[i];
        ttsVoices = voices;
    }


}
