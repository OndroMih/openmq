/*
 * Copyright (c) 2000, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.messaging.jmq.jmsclient;

import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSSecurityException;
import jakarta.jms.JMSSecurityRuntimeException;
import jakarta.jms.XAConnection;
import jakarta.jms.XAConnectionFactory;
import jakarta.jms.XAJMSContext;
import jakarta.jms.XASession;
import javax.transaction.xa.XAResource;

import com.sun.messaging.jms.MQRuntimeException;
import com.sun.messaging.jms.MQSecurityRuntimeException;

public class XAJMSContextImpl extends JMSContextImpl implements XAJMSContext {

    XAConnection xaConnection;
    XASession xaSession;

    public XAJMSContextImpl(XAConnectionFactory connectionFactory, ContainerType containerType, String userName, String password) {
        super();
        this.containerType = containerType;

        // create connection
        try {
            xaConnection = connectionFactory.createXAConnection(userName, password);
            connection = xaConnection;
        } catch (SecurityException e) {
            JMSSecurityRuntimeException jsre = new com.sun.messaging.jms.MQSecurityRuntimeException(e.getMessage(), null, e);
            ExceptionHandler.throwJMSRuntimeException(jsre);
        } catch (JMSSecurityException e) {
            throw new MQSecurityRuntimeException(e);
        } catch (JMSException e) {
            throw new MQRuntimeException(e);
        }
        // create session
        try {
            xaSession = xaConnection.createXASession();
            session = xaSession;
        } catch (JMSException e) {
            try {
                connection.close();
            } catch (JMSException e1) {
            }
            throw new MQRuntimeException(e);
        }
        initializeForNewConnection();
    }

    public XAJMSContextImpl(XAConnectionFactory connectionFactory, ContainerType containerType) {
        super();
        this.containerType = containerType;

        // create connection
        try {
            xaConnection = connectionFactory.createXAConnection();
            connection = xaConnection;
        } catch (SecurityException e) {
            JMSSecurityRuntimeException jsre = new com.sun.messaging.jms.MQSecurityRuntimeException(e.getMessage(), null, e);
            ExceptionHandler.throwJMSRuntimeException(jsre);
        } catch (JMSSecurityException e) {
            throw new MQSecurityRuntimeException(e);
        } catch (JMSException e) {
            throw new MQRuntimeException(e);
        }
        // create session
        try {
            xaSession = xaConnection.createXASession();
            session = xaSession;
        } catch (JMSException e) {
            try {
                connection.close();
            } catch (JMSException e1) {
            }
            throw new MQRuntimeException(e);
        }
        initializeForNewConnection();
    }

    @Override
    public JMSContext getContext() {
        return this;
    }

    @Override
    public XAResource getXAResource() {
        return xaSession.getXAResource();
    }
}
