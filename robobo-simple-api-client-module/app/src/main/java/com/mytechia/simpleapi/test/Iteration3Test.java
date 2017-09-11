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

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.mytechia.robobo.framework.hri.sound.noteDetection.Note;
import com.mytechia.robobo.framework.hri.speech.production.ISpeechProductionModule;
import com.mytechia.simpleapi.component.ComponentNotFoundException;
import com.mytechia.simpleapi.component.ISimpleRoboboManagerListener;
import com.mytechia.simpleapi.component.ISoundExpressionListener;
import com.mytechia.simpleapi.component.ISoundPerceptionListener;
import com.mytechia.simpleapi.component.SimpleRoboboManager;
import com.mytechia.simpleapi.component.SoundExpressionComponent;
import com.mytechia.simpleapi.component.SoundPerceptionComponent;

import java.util.Timer;
import java.util.TimerTask;

import static com.mytechia.robobo.framework.hri.sound.noteDetection.Note.B5;
import static com.mytechia.robobo.framework.hri.sound.noteDetection.Note.D6;
import static com.mytechia.robobo.framework.hri.sound.noteDetection.Note.G5;



public class Iteration3Test extends Iteration2Test implements ISimpleRoboboManagerListener,
        ISoundExpressionListener, ISoundPerceptionListener {

    private static final String LOG_TAG = "It3Test";
    private Context context;

    private SimpleRoboboManager simpleRoboboManager;
    private SoundExpressionComponent soundExpression;
    private SoundPerceptionComponent soundPerception;


    public Iteration3Test(Context context, SimpleRoboboManager simpleRoboboManager) {
        super(context, simpleRoboboManager);
        this.context = context;
        this.simpleRoboboManager = simpleRoboboManager;
    }


    @Override
    public void start(String robName) throws ComponentNotFoundException {
        Log.d(LOG_TAG, "Starting It_3");
        simpleRoboboManager.addListener(this);
        simpleRoboboManager.addComponent(context, SoundExpressionComponent.class);
        simpleRoboboManager.addComponent(context, SoundPerceptionComponent.class);
        super.start(robName);
        soundExpression = (SoundExpressionComponent) simpleRoboboManager
                .getComponentInstance(SoundExpressionComponent.class);
        soundExpression.addListener(this);
        soundPerception = (SoundPerceptionComponent) simpleRoboboManager
                .getComponentInstance(SoundPerceptionComponent.class);
        soundPerception.addListener(this);
    }


    @Override
    public void stop() {
        Log.d(LOG_TAG, "Stopping It_3");
        soundPerception.removeListener(this);
        soundPerception = null;
        soundExpression.removeListener(this);
        soundExpression = null;
        simpleRoboboManager.removeComponent(SoundPerceptionComponent.class);
        simpleRoboboManager.removeComponent(SoundExpressionComponent.class);
        simpleRoboboManager.removeListener(this);
        super.stop();
    }


    public void sayText(String text) throws RemoteException {
        soundExpression.sayText(text, ISpeechProductionModule.PRIORITY_HIGH);
    }


    @Override
    public void onComponentsStarted() {
        try {
            int noteDur = 250;
            soundExpression.addNoteToSequence(G5, noteDur);
            soundExpression.addNoteToSequence(B5, noteDur);
            soundExpression.addNoteToSequence(D6, noteDur);
            soundExpression.playSequence();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        sayText("Hola! Soy Robobo");
                    } catch (RemoteException e) {
                        Log.d(LOG_TAG, "Error saying hello: " + e.getMessage() +
                                "\n\nStopping test");
                        stop();
                    }
                }
            }, 3 * noteDur + 2000);
            super.onComponentsStarted();
        } catch (RemoteException e) {
            Log.d(LOG_TAG, "Error starting blob tracking: " + e.getMessage() + "\n\nStopping test");
            stop();
        }
    }


    @Override
    public void onClap(double time) {

    }


    @Override
    public void onPitchDetected(double freq) {

    }


    @Override
    public void onNoteDetected(Note note) {

    }


    @Override
    public void onNoteEnd(Note note, long time) {
        try {
            if (note == Note.C4) {
                startMovement();
            } else if (note == Note.C5){
                stopMovement();
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, "Error stopping movement");
        }
    }


    @Override
    public void onNewNote(Note note) {

    }


    @Override
    public void onNotePlayEnd() {

    }


    @Override
    public void onSequencePlayEnd() {

    }


    @Override
    public void voiceNotFound(String errorMsg) {

    }


    @Override
    public void onEndOfSpeech() {

    }


}
