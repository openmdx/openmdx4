/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: JMI 1 Package Interface
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
package org.openmdx.base.accessor.jmi.spi;

import javax.jdo.PersistenceManagerFactory;
import javax.jmi.reflect.RefObject;

import org.openmdx.base.accessor.jmi.cci.RefPackage_1_0;
import org.openmdx.base.naming.Path;

/**
 * JMI 1 Package Interface
 */
public interface Jmi1Package_1_0 extends RefPackage_1_0 {

    /**
     * Retrieves the JDO Persistence Manager Factory.
     * 
     * @return the JDO Persistence Manager Factory configured according to this package
     */
    PersistenceManagerFactory refPersistenceManagerFactory();

    /**
     * Tells whether the RefPackage's delegate is an ObjectFactory_1_0 or
     * a PersistenceManager.
     * 
     * @return {@code true} if the RefPackage's delegate is an 
     * PersistenceManager_1_1 instance, {@code false} if the RefPackage's 
     * delegate is a PersistenceManager instance
     * 
     * @see DataObjectManager_1_0
     * @see #refDelegate()
     */
    boolean isTerminal();
    
    /**
     * Retrieve the associated implementation mapper
     * 
     * @return the associated implementation mapper
     */
    Mapping_1_0 refMapping(
    );
    
    /**
     * Get object with the given object id. 
     *  
     * @param objectId unique id of RefObject.
     * 
     * @return RefObject
     */
    RefObject refObject(
      Path objectId
    );

    /**
     * Avoid outermost RefPackage validation
     * 
     * @param source
     * 
     * @return the unmarshaled object
     */
    Object unmarshalUnchecked(
    	Object source
    );

}
