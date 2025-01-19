/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Jmi1PackageInvocationHandler 
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import javax.jmi.reflect.RefBaseObject;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefFeatured;

import org.openmdx.base.accessor.jmi.cci.RefPackage_1_0;
import org.openmdx.base.exception.ServiceException;

/**
 * Jmi1ClassInvocationHandler
 */
public class Jmi1ClassInvocationHandler implements InvocationHandler {

    /**
     * Constructor 
     *
     * @param qualifiedClassName
     * @param immediatePackage
     * 
     * @throws ServiceException
     */
    public Jmi1ClassInvocationHandler(
        String qualifiedClassName,
        RefPackage_1_0 immediatePackage
    ) throws ServiceException {
        this.delegate = new RefClass_1(
            qualifiedClassName,
            immediatePackage
        );
    }

    //-----------------------------------------------------------------------
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(
        Object proxy, 
        Method method, 
        Object[] args
    ) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();
        String methodName = method.getName();
        if(Object.class == declaringClass) {
            //
            // Object methods
            //
            if("toString".equals(methodName)) {
                return proxy.getClass().getName() + " delegating to " + this.delegate;
            } 
            else if ("hashCode".equals(methodName)) {
                return Integer.valueOf(this.delegate.hashCode());
            } 
            else if ("equals".equals(methodName)) {
                if(Proxy.isProxyClass(args[0].getClass())) {
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(args[0]);
                    if(invocationHandler instanceof Jmi1ClassInvocationHandler) {
                        return Boolean.valueOf(
                            this.delegate.equals(
                                ((Jmi1ClassInvocationHandler)invocationHandler).delegate
                            )
                        );
                    }
                }
                return Boolean.FALSE;
            }
        } else {
            this.delegate.assertOpen();
            if(
                declaringClass == Jmi1Class_1_0.class || 
                declaringClass == RefClass.class ||
                declaringClass == RefFeatured.class ||
                declaringClass == RefBaseObject.class
            ){
                //
                // RefObject API
                //
                if("refCreateInstance".equals(methodName) && args.length == 1) {
                    return this.delegate.refCreateInstance(
                        (List<?>)args[0], // arguments
                        (Jmi1Class_1_0)proxy
                    );
                } else try {
                    return method.invoke(
                        this.delegate, 
                        args
                    );
                } catch(InvocationTargetException e) {
                    throw e.getTargetException();
                }
            } else if(
                args == null || 
                args.length == 0 ||
                methodName.startsWith("create")
            ) {
                //
                // Creators
                //
                return this.delegate.refCreateInstance(
                    null, // arguments
                    (Jmi1Class_1_0)proxy
                );            
            } else if(
                args == null || 
                args.length == 1 ||
                methodName.startsWith("get")
            ) {
                //
                // Creators
                //
                return this.delegate.refCreateInstance(
                    Arrays.asList(args), 
                    (Jmi1Class_1_0)proxy
                );            
            }        
        }
        throw new UnsupportedOperationException(method.getName());
    }
    
    //-----------------------------------------------------------------------
    // Members
    //-----------------------------------------------------------------------
    protected final RefClass_1 delegate;
    
}
