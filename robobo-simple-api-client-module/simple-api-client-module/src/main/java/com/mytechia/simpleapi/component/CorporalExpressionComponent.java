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
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.mytechia.robobo.rob.IRSensorStatus;
import com.mytechia.robobo.rob.MoveMTMode;
import com.mytechia.robobo.util.Color;
import com.mytechia.simpleapi.CorporalExpressionAction;
import com.mytechia.simpleapi.util.ComActUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.mytechia.simpleapi.CorporalExpressionAction.INTERNAL_ERROR;
import static com.mytechia.simpleapi.CorporalExpressionAction.MOVE_MOTOR_DEGREES;
import static com.mytechia.simpleapi.CorporalExpressionAction.MOVE_MOTOR_TIME;
import static com.mytechia.simpleapi.CorporalExpressionAction.MOVE_PAN;
import static com.mytechia.simpleapi.CorporalExpressionAction.MOVE_TILT;
import static com.mytechia.simpleapi.CorporalExpressionAction.RESET_PAN_TILT;
import static com.mytechia.simpleapi.CorporalExpressionAction.SET_LED_COLOR;
import static com.mytechia.simpleapi.CorporalExpressionAction.SET_ROB_STATUS_PERIOD;
import static com.mytechia.simpleapi.CorporalExpressionAction.STATUS_IRS;
import static com.mytechia.simpleapi.ServiceCommand.CORPORAL_EXPRESSION;



public class CorporalExpressionComponent extends ASimpleAPIComponent {

    private static final String LOG_TAG = "CorporalComponent";
    private SparseArray<Enum> mActionValues;

    private List<IRSensorStatus> irStatus;
    private HashSet<ICorporalExpressionListener> listeners;


    public CorporalExpressionComponent(Context context, IComponentListener listener) {
        super(context, CORPORAL_EXPRESSION, CorporalExpressionComponent.class, LOG_TAG, listener);
        this.mActionValues = ComActUtil.enum2HashCode(CorporalExpressionAction.values());
        this.listeners = new HashSet<>();
    }


    @Override
    protected void handleHandlerMessage(Message msg) {
        CorporalExpressionAction action = (CorporalExpressionAction) mActionValues.get(msg.what);
        Log.d(LOG_TAG, "handleHandlerMessage(): " + action.name());
        switch (action) {
            case INTERNAL_ERROR:
                internalError(msg.getData());
                break;

            case STATUS_IRS:
                statusIRs(msg.getData());
                break;

            default:
                Log.e(LOG_TAG, "Unrecognized action");
        }
    }


    @Override
    protected void onStartUp() {

    }


    public void setLEDColor(int led, int color) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_LED_COLOR.getParameters().getParamKey(0), led);
        params.putInt(SET_LED_COLOR.getParameters().getParamKey(1), color);
        sendHandler(SET_LED_COLOR, params);
    }


    public void setLEDColor(int led, int alpha, int red, int green, int blue)
            throws RemoteException {
        int color = 0;
        color |= (alpha << 24);
        color |= (red << 16);
        color |= (green << 8);
        color |= blue;
        setLEDColor(led, color);
    }


    public void setLEDColor(int led, Color color) throws RemoteException {
        setLEDColor(led, color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }


    public void moveMTDegrees(MoveMTMode mode, int angVelR, int angleR, int angVelL, int angleL)
            throws RemoteException {
        moveMTDegrees(mode.getMode(), angVelR, angleR, angVelL, angleL);
    }


    public void moveMTDegrees(int angVelR, int angleR, int angVelL, int angleL)
            throws RemoteException {
        moveMTDegrees((byte) -1, angVelR, angleR, angVelL, angleL);
    }


    private void moveMTDegrees(byte mode, int angVelR, int angleR, int angVelL, int angleL)
            throws RemoteException {
        Bundle params = new Bundle();
        params.putByte(MOVE_MOTOR_DEGREES.getParameters().getParamKey(0), mode);
        params.putInt(MOVE_MOTOR_DEGREES.getParameters().getParamKey(1), angVelR);
        params.putInt(MOVE_MOTOR_DEGREES.getParameters().getParamKey(2), angleR);
        params.putInt(MOVE_MOTOR_DEGREES.getParameters().getParamKey(3), angVelL);
        params.putInt(MOVE_MOTOR_DEGREES.getParameters().getParamKey(4), angleL);
        sendHandler(MOVE_MOTOR_DEGREES, params);
    }


    public void moveMTTime(MoveMTMode mode, int angVelR, int angVelL, long time)
            throws RemoteException {
        moveMTTime(mode.getMode(), angVelR, angVelL, time);
    }


    public void moveMTTime(int angVelR, int angVelL, long time) throws RemoteException {
        moveMTTime((byte) -1, angVelR, angVelL, time);
    }


    private void moveMTTime(byte mode, int angVelR, int angVelL, long time) throws RemoteException {
        Bundle params = new Bundle();
        params.putByte(MOVE_MOTOR_DEGREES.getParameters().getParamKey(0), mode);
        params.putInt(MOVE_MOTOR_TIME.getParameters().getParamKey(1), angVelR);
        params.putInt(MOVE_MOTOR_TIME.getParameters().getParamKey(2), angVelL);
        params.putLong(MOVE_MOTOR_TIME.getParameters().getParamKey(3), time);
        sendHandler(MOVE_MOTOR_TIME, params);
    }


    public void movePan(int angVel, int angle) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(MOVE_PAN.getParameters().getParamKey(0), angVel);
        params.putInt(MOVE_PAN.getParameters().getParamKey(1), angle);
        sendHandler(MOVE_PAN, params);
    }


    public void moveTilt(int angVel, int angle) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(MOVE_TILT.getParameters().getParamKey(0), angVel);
        params.putInt(MOVE_TILT.getParameters().getParamKey(1), angle);
        sendHandler(MOVE_TILT, params);
    }


    public void resetPanTilt() throws RemoteException {
        sendHandler(RESET_PAN_TILT);
    }


    public void setRobStatusPeriod(int period) throws RemoteException {
        Bundle params = new Bundle();
        params.putInt(SET_ROB_STATUS_PERIOD.getParameters().getParamKey(0), period);
        sendHandler(SET_ROB_STATUS_PERIOD, params);
    }


    public List<IRSensorStatus> getLastStatusIRs() {
        return irStatus;
    }


    public void addListener(ICorporalExpressionListener listener) {
        listeners.add(listener);
    }


    public void removeListener(ICorporalExpressionListener listener) {
        listeners.remove(listener);
    }


    private void internalError(Bundle data) {
        String errorMsg = data.getString(INTERNAL_ERROR.name());
        for (ICorporalExpressionListener listener: listeners)
            listener.internalError(errorMsg);
    }


    private void statusIRs(Bundle data) {
        int[] irStatusArray = data.getIntArray(STATUS_IRS.getParameters().getParamKey(0));
        IRSensorStatus.IRSentorStatusId[] irId = IRSensorStatus.IRSentorStatusId.values();
        irStatus = new ArrayList<>();
        for (int i = 0; i < irStatusArray.length; i++) {
            IRSensorStatus currentIRStatus = new IRSensorStatus(irId[i]);
            currentIRStatus.setDistance((short) irStatusArray[i]);
            irStatus.add(currentIRStatus);
        }
        for (ICorporalExpressionListener listener: listeners)
            listener.statusIRs(irStatus);
    }


}
