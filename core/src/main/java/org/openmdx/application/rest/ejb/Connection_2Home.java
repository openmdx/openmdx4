/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Connection 2 Home Interface
 * Owner:       the original authors.
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 *   contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
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
 * This product includes software developed by other organizations as
 * listed in the NOTICE file.
 */
package org.openmdx.application.rest.ejb;

import java.rmi.RemoteException;

import jakarta.ejb.CreateException;
import jakarta.ejb.EJBHome;
import jakarta.resource.cci.ConnectionSpec;

/**
 * Connection 2 Home Interface
 */
public interface Connection_2Home extends EJBHome {

  /**
   * This method corresponds to the ejbCreate method in the bean.
   * The parameter sets of the two methods are identical. When the client
   * calls {@code Connection_2Home.create()}, the container
   * allocates an instance of the EJBean and calls {@code ejbCreate()}.
   *
   * @param         connectionSpec the REST {@code ConnectionSpec} 
   * 
   * @return        a Connection_2_0Remote object
   *
   * @exception     RemoteException
   *                if there is a communications or systems failure
   * @exception     CreateException
   *                if there is a problem creating the bean
   */
  Connection_2_0Remote create(
      ConnectionSpec connectionSpec
  ) throws CreateException, RemoteException;
  
}
