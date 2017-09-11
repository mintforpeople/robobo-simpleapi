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

package com.mytechia.simpleapi.test;

import android.os.RemoteException;
import android.util.Log;

import com.mytechia.robobo.framework.hri.sound.emotionSound.IEmotionSoundModule;
import com.mytechia.robobo.framework.hri.sound.noteDetection.Note;
import com.mytechia.simpleapi.component.ISoundExpressionListener;
import com.mytechia.simpleapi.component.ISoundPerceptionListener;
import com.mytechia.simpleapi.component.SoundExpressionComponent;
import com.mytechia.simpleapi.component.SoundPerceptionComponent;

import java.util.Timer;
import java.util.TimerTask;

import static com.mytechia.robobo.framework.hri.sound.noteGeneration.Note.A4;
import static com.mytechia.robobo.framework.hri.sound.noteGeneration.Note.C5;
import static com.mytechia.robobo.framework.hri.sound.noteGeneration.Note.E5;
import static com.mytechia.robobo.framework.hri.sound.noteGeneration.Note.G5;



public class SoundTest implements ISoundExpressionListener, ISoundPerceptionListener {

    private static final String LOG_TAG = "SoundTest";
    private SoundPerceptionComponent soundPerceptionComponent;
    private SoundExpressionComponent soundExpressionComponent;


    public SoundTest(SoundPerceptionComponent soundPerceptionComponent,
                     SoundExpressionComponent soundExpressionComponent) {
        this.soundPerceptionComponent = soundPerceptionComponent;
        this.soundExpressionComponent = soundExpressionComponent;
    }


    public SoundTest(SoundExpressionComponent soundExpressionComponent) {
        this(null, soundExpressionComponent);
    }


    public void test() {
        soundPerceptionComponent.addListener(this);
        soundExpressionComponent.addListener(this);
        try {
            soundExpressionComponent.playEmotionSound(IEmotionSoundModule.ANGRY_SOUND);
            soundExpressionComponent.addNoteToSequence(A4, 250);
            soundExpressionComponent.addNoteToSequence(C5, 250);
            soundExpressionComponent.addNoteToSequence(E5, 250);
            soundExpressionComponent.addNoteToSequence(G5, 250);
        } catch (RemoteException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        Integer delay = 1000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    soundExpressionComponent.playNote(A4, 2000);
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }, delay);
        delay += 4000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    soundExpressionComponent.playSequence();
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }, delay);
        delay += 250*4;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                   soundExpressionComponent.sayText("Hola", 1);
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }, delay);
        delay += 2000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    soundExpressionComponent.playNote(Note.C5, 5000);
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }, delay);
        delay += 6000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    soundExpressionComponent.playNote(C5, 5000);
                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }, delay);
    }


    @Override
    public void onNotePlayEnd() {
        Log.d(LOG_TAG, "onNotePlayEnd()");
    }


    @Override
    public void onSequencePlayEnd() {
        Log.d(LOG_TAG, "onSequencePlayEnd()");
    }


    @Override
    public void voiceNotFound(String errorMsg) {
        Log.d(LOG_TAG, "voiceNotFound(): " + errorMsg);
    }


    @Override
    public void onEndOfSpeech() {
        Log.d(LOG_TAG, "onEndOfSpeech()");
    }


    @Override
    public void onClap(double time) {
        Log.d(LOG_TAG, "onClap(): " + Double.toString(time) + " ms");
    }


    @Override
    public void onPitchDetected(double freq) {
        Log.d(LOG_TAG, "onPitchDetected(): " + Double.toString(freq) + " Hz");
    }


    @Override
    public void onNoteDetected(Note note) {
        Log.d(LOG_TAG, "onNoteDetected(): " + note.name());
    }


    @Override
    public void onNoteEnd(Note note, long time) {
        Log.d(LOG_TAG, "onNoteEnd(): " + note.name() + " " + Long.toString(time) + " ms");
    }


    @Override
    public void onNewNote(Note note) {
        Log.d(LOG_TAG, "onNoteEnd(): " + note.name());
    }


}
