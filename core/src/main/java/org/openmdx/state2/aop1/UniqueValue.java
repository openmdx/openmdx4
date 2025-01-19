/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Unique
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
package org.openmdx.state2.aop1;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.kernel.exception.BasicException;
import org.openmdx.kernel.jdo.ReducedJDOHelper;

/**
 * Unique
 */
final class UniqueValue<T> {

    /**
     * Constructor 
     */
    UniqueValue(){
        super();
    }
    
    /**
     * 
     */
    private T value = null;

    /**
     * 
     */
    private boolean empty = true;
    
    /**
     * Process a single state's reply
     * 
     * @param value
     * 
     * @throws ServiceException 
     */
    void set(
        T value
    ) throws ServiceException{
        if(this.empty) {
            this.value = value;
            this.empty = false;
        } else if (this.value == null ? value != null : !this.value.equals(value)) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                "The underlying states have inconsistent values for the given request",
                new BasicException.Parameter("values", ReducedJDOHelper.replaceObjectById(this.value), ReducedJDOHelper.replaceObjectById(value))
            );
        }
    }
    
    /**
     * Retrieve the consolidated reply
     * 
     * @return the value returned by all underlying states
     * 
     * @exception IllegalStateException if there is no underlying state
     */
    T get(
    ) throws ServiceException {
        if(this.empty) {
            throw new ServiceException(
                BasicException.Code.DEFAULT_DOMAIN,
                BasicException.Code.ILLEGAL_STATE,
                "There is no matching state for the given request. "
                + "Consider erroneous behaviour or concurrent modifications as possible reasons."
            );
        } else {
            return this.value;
        }
    }

    /**
     * Tells whether no value has been set
     * 
     * @return {@code true} if no value has been set
     */
    boolean isEmpty(
    ){
        return this.empty;
    }

}