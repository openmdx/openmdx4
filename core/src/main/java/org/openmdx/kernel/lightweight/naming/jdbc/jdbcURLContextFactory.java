/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: JDBC URL Context Factory
 * Owner:       the original authors.
 * ====================================================================
 *
 * This software is published under the BSD license as listed below.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
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
package org.openmdx.kernel.lightweight.naming.jdbc;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.ObjectFactory;

/**
 * jdbc URL Context Factory supoporting the following two formats:<ul>
 * <li>jdbc:oracle:thin:@localhost:1521:XE?user=scott&password=tiger&driver=oracle.jdbc.OracleDriver
 * <li>jdbc:XADataSource:oracle.jdbc.xa.client.OracleXADataSource?URL=jdbc:oracle:thin:@localhost:1521:XE&user=scott&password=tiger
 * </ul>
 */
public class jdbcURLContextFactory implements ObjectFactory {

	/**
     * The context is lazily initialized
     */
    private static Context dataSourceContext;
	
    /**
     * Return a database connection factory
     */
    public Object getObjectInstance(
       Object obj, 
       Name name, 
       Context nameCtx,
       Hashtable<?,?> environment
    ) throws NamingException {
        Object object = obj;
        if(object instanceof Object[]){
            Object[] urls = (Object[]) object;
            if(urls.length == 0) throw new NoInitialContextException("URL array is empty");
            object = urls[0]; // Just take the first of the equivalent URLs
        }
        if(object == null){
            return dataSourceContext;
        } else if(object instanceof String){
            String url = (String) object;
            if(url.startsWith("jdbc:")) {
                return dataSourceContext.lookup(url);
            } else throw new NoInitialContextException(
                "jdbc URL scheme expected: " + url
            );            
        } else throw new NoInitialContextException(
            "jdbc URL supports String object only: " + object.getClass().getName()
        );
    }

    static void setDataSourceContext(
    	Context initialContextFactorySpecificDataSourceContext	
    ){
    	dataSourceContext = initialContextFactorySpecificDataSourceContext; 
    }
    
}

