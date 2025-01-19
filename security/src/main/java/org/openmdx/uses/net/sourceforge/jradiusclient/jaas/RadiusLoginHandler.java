/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Description: RadiusLoginHandler
 * Owner:       the original authors.
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * * This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 * * This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 * * You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Neither the name of the openMDX team nor the names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * ------------------
 * 
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 * 
 * This library BASED on Java Radius Client 2.0.0
 * (http://http://jradius-client.sourceforge.net/),
 * but it's namespace and content has been MODIFIED by the openMDX team
 * in order to integrate it into the openMDX framework.
 */
package org.openmdx.uses.net.sourceforge.jradiusclient.jaas;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Copyright:    Copyright (c) 2003
 * @author <a href="mailto:bloihl@users.sourceforge.net">Robert J. Loihl</a>
 */
public class RadiusLoginHandler implements CallbackHandler {

    public static final String JAAS_MODULE_KEY = "JRadiusClientLoginModule";

    /* fields for storing credentials */
    private String name;
    private String password;
    private String clientIP;
	private String hostName;
	private String sharedSecret;
	private int authPort;
	private int acctPort;
	private String callingStationID;
	private int numRetries;
	private int timeout;

    /**
     * Constructor
     * @throws java.lang.IllegalArgumentException if any param is null
     */
    public RadiusLoginHandler(final String name, 
						final String password, 
						final String clientIP,
						final String callingStationID,
						final String radiusHostname,
						final String sharedSecret,
						final int authPort,
						final int acctPort,
						final int retries,
						final int timeout) {
        if (name == null || password == null || clientIP == null) {
            throw new IllegalArgumentException("Arguments cannont be null");
        }
        this.name = name;
        this.password = password;
        this.clientIP = clientIP;
		this.callingStationID = callingStationID;
		this.hostName = radiusHostname;
		this.sharedSecret = sharedSecret;
		this.authPort = authPort;
		this.acctPort = acctPort;
		this.numRetries = retries;
		this.timeout = timeout;
    }

    /**
     * If this method returns the login was successfull, but if it throws an
     * exception it failed.  There are subclasses of LoginException for the
     * specific kinds of failures.
     */
    public void login() throws LoginException {
        LoginContext loginContext = new LoginContext(JAAS_MODULE_KEY, this);
        loginContext.login();
    }

    /**
     * Callback Handler for the login service
     * @param javax.security.auth.callback.Callback array of callback objects to
     * fill in with requested data
     * @throws java.io.IOException
     * @throws javax.security.auth.callback.UnsupportedCallbackException
     */
    public void handle(Callback[] callback) throws IOException, UnsupportedCallbackException {
        for(int i = 0; i < callback.length; i++){
            handle(callback[i]);
        }
    }

    protected void handle(Callback callback) throws UnsupportedCallbackException {
        if (callback instanceof NameCallback) {
            ((NameCallback)callback).setName(this.name);
        }else if (callback instanceof PasswordCallback) {
            ((PasswordCallback)callback).setPassword(this.password.toCharArray());
        }else if (callback instanceof TextInputCallback) {
            // this code assumes that there will only be one TextInputCallback and it is used for the client IP
            ((TextInputCallback)callback).setText(this.clientIP);
        }else if (callback instanceof RadiusCallback) {
			RadiusCallback radiusCallback = (RadiusCallback)callback;
			radiusCallback.setHostName(this.hostName);
			radiusCallback.setSharedSecret(this.sharedSecret);
			radiusCallback.setAuthPort(this.authPort);
			radiusCallback.setAcctPort(this.acctPort);
			radiusCallback.setCallingStationID(this.callingStationID);
			radiusCallback.setNumRetries(this.numRetries);
			radiusCallback.setTimeout(this.timeout);
		}else {
            throw new UnsupportedCallbackException(callback);
        }
    }
}

