/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Resource Exceptions 
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
package org.openmdx.base.resource.spi;

import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.EISSystemException;

import org.openmdx.kernel.exception.BasicException;

/**
 * ResourceExceptions
 */
public final class ResourceExceptions {

    /**
     * Constructor 
     */
    private ResourceExceptions(){
        // Avoid instantiations
    }
    
    /**
     * Links the exception stack with the resource exception
     */
    public static <T extends ResourceException> T initHolder(
        T resourceException
    ){
        BasicException.initHolder(resourceException).setErrorCode(
            resourceException.getCause().getMessage()
        );
        return resourceException;
    }

    /**
     * Wraps the exception into a system exception
     */
    public static EISSystemException toSystemException(
        Exception exception
    ){
    	if(exception instanceof EISSystemException) {
    		return (EISSystemException) exception;
    	}
        final BasicException basicException = BasicException.toExceptionStack(exception);
        final EISSystemException resourceException = new EISSystemException(
			basicException.getDescription(),
			basicException
		);
        resourceException.setErrorCode(basicException.getMessage());
		return resourceException;
    }

    /**
     * Wraps the exception into a not-supported exception
     */
    public static NotSupportedException toNotSupportedException(
        Exception exception
    ){
    	if(exception instanceof NotSupportedException) {
    		return (NotSupportedException) exception;
    	}
        final BasicException basicException = BasicException.toExceptionStack(exception);
        final NotSupportedException resourceException = new NotSupportedException(
			basicException.getDescription(),
			basicException
		);
        resourceException.setErrorCode(basicException.getMessage());
		return resourceException;
    }
    
    /**
     * Wraps the exception into a resource exception
     */
    public static ResourceException toResourceException(
    	Exception exception
    ){
    	if(exception instanceof ResourceException) {
    		return (ResourceException) exception;
    	}
        BasicException basicException = BasicException.toExceptionStack(exception);
        final ResourceException resourceException = new ResourceException(
			basicException.getDescription(),
			basicException
		);
        resourceException.setErrorCode(basicException.getMessage());
		return resourceException;
    }

}
