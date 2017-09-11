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

import java.io.PrintStream;
import java.io.PrintWriter;



public class ComponentNotFoundException extends Exception implements java.io.Serializable {

    private Exception encapsulatedException = null;


    public ComponentNotFoundException(Exception exception) {
        super((String) null);
        encapsulatedException = exception;
    }


    public ComponentNotFoundException(String message) {
        super(message);
        encapsulatedException = null;
    }


    public ComponentNotFoundException(Exception exception, String message) {
        super(message);
        encapsulatedException = exception;
    }


    @Override
    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        } else {
            if (encapsulatedException != null) {
                return encapsulatedException.getMessage();
            } else {
                return null;
            }
        }
    }


    public Exception getEncapsulatedException() {
        return encapsulatedException;
    }


    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }


    @Override
    public void printStackTrace(PrintStream printStream) {
        super.printStackTrace(printStream);
        if (encapsulatedException != null) {
            printStream.println("***Information about encapsulated exception***");
            encapsulatedException.printStackTrace(printStream);
        }
    }


    @Override
    public void printStackTrace(PrintWriter printWriter) {
        super.printStackTrace(printWriter);
        if (encapsulatedException != null) {
            printWriter.println("***Information about encapsulated exception***");
            encapsulatedException.printStackTrace(printWriter);
        }
    }
}
