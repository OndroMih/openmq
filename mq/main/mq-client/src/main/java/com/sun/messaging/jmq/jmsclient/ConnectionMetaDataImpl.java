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

import jakarta.jms.*;
import com.sun.messaging.*;

import java.util.Enumeration;
import java.util.Vector;

import com.sun.messaging.jmq.Version;

/*
 * ConnectionMetaData provides information describing the Connection.
 */

public class ConnectionMetaDataImpl implements ConnectionMetaData {
    protected static final Version version = new Version();
    protected static final String JMSVersion = "3.0";
    protected static final int JMSMajorVersion = 3;
    protected static final int JMSMinorVersion = 0;
    public static final String JMSProviderName = version.getProductName();
    public static final String providerVersion = "6.0";
    protected static final int providerMajorVersion = 6;
    protected static final int providerMinorVersion = 0;

    public static final String JMSXGroupID = "JMSXGroupID";
    public static final String JMSXGroupSeq = "JMSXGroupSeq";

    public static final String JMSXAppID = "JMSXAppID";
    public static final String JMSXConsumerTXID = "JMSXConsumerTXID";
    public static final String JMSXProducerTXID = "JMSXProducerTXID";
    public static final String JMSXRcvTimestamp = "JMSXRcvTimestamp";
    public static final String JMSXUserID = "JMSXUserID";
    public static final String JMSXDeliveryCount = "JMSXDeliveryCount";

    protected boolean setJMSXAppID = false;
    protected boolean setJMSXConsumerTXID = false;
    protected boolean setJMSXProducerTXID = false;
    protected boolean setJMSXRcvTimestamp = false;
    protected boolean setJMSXUserID = false;

    protected Vector supportedProperties = new Vector(7);

    protected ConnectionImpl connection = null;

    protected ConnectionMetaDataImpl(ConnectionImpl connection) {
        // current connection
        this.connection = connection;
        init();
    }

    private void init() {
        String tmp = null;

        /*
         * the following three properties are supported by default.
         */
        // set by client apps (optional)
        supportedProperties.addElement(JMSXGroupID);
        // set by client apps (optional)
        supportedProperties.addElement(JMSXGroupSeq);
        // set by JMS provider (JMS2.0 mandatory)
        supportedProperties.addElement(JMSXDeliveryCount);

        // test if set app id requested
        tmp = connection.getProperty(ConnectionConfiguration.imqSetJMSXAppID);
        if (Boolean.parseBoolean(tmp)) {
            setJMSXAppID = true;
            supportedProperties.addElement(JMSXAppID);
        }

        // test if set consumer TX ID requested
        tmp = connection.getProperty(ConnectionConfiguration.imqSetJMSXConsumerTXID);
        if (Boolean.parseBoolean(tmp)) {
            setJMSXConsumerTXID = true;
            supportedProperties.addElement(JMSXConsumerTXID);
        }

        // test if set producer TX id requested
        tmp = connection.getProperty(ConnectionConfiguration.imqSetJMSXProducerTXID);
        if (Boolean.parseBoolean(tmp)) {
            setJMSXProducerTXID = true;
            supportedProperties.addElement(JMSXProducerTXID);
        }

        // test if set receive time stamp requested
        tmp = connection.getProperty(ConnectionConfiguration.imqSetJMSXRcvTimestamp);
        if (Boolean.parseBoolean(tmp)) {
            setJMSXRcvTimestamp = true;
            supportedProperties.addElement(JMSXRcvTimestamp);
        }

        // test if set user id requested
        tmp = connection.getProperty(ConnectionConfiguration.imqSetJMSXUserID);
        if (Boolean.parseBoolean(tmp)) {
            setJMSXUserID = true;
            supportedProperties.addElement(JMSXUserID);
        }

    }

    /**
     * Get the JMS version.
     *
     * @return the JMS version.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public String getJMSVersion() throws JMSException {
        return JMSVersion;
    }

    /**
     * Get the JMS major version number.
     *
     * @return the JMS major version number.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public int getJMSMajorVersion() throws JMSException {
        return JMSMajorVersion;
    }

    /**
     * Get the JMS minor version number.
     *
     * @return the JMS minor version number.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public int getJMSMinorVersion() throws JMSException {
        return JMSMinorVersion;
    }

    /**
     * Get the JMS provider name.
     *
     * @return the JMS provider name.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public String getJMSProviderName() throws JMSException {
        return JMSProviderName;
    }

    /**
     * Get the JMS provider version.
     *
     * @return the JMS provider version.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public String getProviderVersion() throws JMSException {
        return providerVersion;
    }

    /**
     * Get the JMS provider major version number.
     *
     * @return the JMS provider major version number.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public int getProviderMajorVersion() throws JMSException {
        return providerMajorVersion;
    }

    /**
     * Get the JMS provider minor version number.
     *
     * @return the JMS provider minor version number.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the meta-data retrieval.
     */

    @Override
    public int getProviderMinorVersion() throws JMSException {
        return providerMinorVersion;
    }

    /**
     * Get an enumeration of JMSX Property Names.
     *
     * @return an Enumeration of JMSX PropertyNames.
     *
     * @exception JMSException if some internal error occurs in JMS implementation during the property names retrieval.
     */

    @Override
    public Enumeration getJMSXPropertyNames() throws JMSException {
        return supportedProperties.elements();
    }

}
