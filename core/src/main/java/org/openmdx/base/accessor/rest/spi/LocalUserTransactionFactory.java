/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: User Transaction Factory 
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
package org.openmdx.base.accessor.rest.spi;

import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.base.transaction.LocalUserTransaction;
import org.openmdx.kernel.loading.Classes;
import org.openmdx.kernel.loading.Factory;

/**
 * Local User Transaction Factory
 */
class LocalUserTransactionFactory implements Factory<LocalUserTransaction> {
    
    /**
     * Constructor 
     *
     * @param instanceClassName
     */
    LocalUserTransactionFactory(
        String instanceClassName
    ) {
        this.instanceClassName = instanceClassName;
    }

    /**
     * The user transaction class name
     */
    private final String instanceClassName;

    /**
     * The lazily acquired user transaction class
     */
    private Class<? extends LocalUserTransaction> instanceClass;

    /* (non-Javadoc)
     * @see org.openmdx.kernel.loading.Factory#instantiate()
     */
    @Override
    public LocalUserTransaction instantiate() {
        try {
            return getInstanceClass().newInstance();
        } catch (InstantiationException exception) {
            throw new RuntimeServiceException(exception);
        } catch (IllegalAccessException exception) {
            throw new RuntimeServiceException(exception);
        }
    }

    /* (non-Javadoc)
     * @see org.openmdx.kernel.loading.Factory#getInstanceClass()
     */
    @Override
    public Class<? extends LocalUserTransaction> getInstanceClass() {
        if(this.instanceClass == null) {
            try {
                this.instanceClass = Classes.getApplicationClass(this.instanceClassName);
            } catch (ClassNotFoundException exception) {
                throw new RuntimeServiceException(exception);
            }
        }
        return this.instanceClass;
    }

}
