/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: AbstractContainer 
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

import java.util.AbstractCollection;
	import java.util.function.Consumer; 

import org.openmdx.base.accessor.cci.DataObject_1_0;
import org.openmdx.base.accessor.jmi.cci.RefObject_1_0;
import org.openmdx.base.collection.MarshallingConsumer;
import org.openmdx.base.marshalling.Marshaller;
import org.w3c.cci2.AnyTypePredicate;
import org.w3c.cci2.Container;

/**
 * This intermediate class was used for JRE 6/8 support
 */
abstract class AbstractContainer
    extends AbstractCollection<RefObject_1_0>
    implements Container<RefObject_1_0> 
{
    
    protected AbstractContainer(
    	Marshaller marshaller
    ){
        this.marshaller = marshaller;
    }

    protected final Marshaller marshaller;
    
    /* (non-Javadoc)
     * @see org.w3c.cci2.Container#processAll(org.w3c.cci2.AnyTypePredicate, java.util.function.Consumer)
     */
    @Override
    public void processAll(
        AnyTypePredicate predicate, 
        Consumer<RefObject_1_0> consumer
    ){
        refProcessAll(
            predicate, 
            new MarshallingConsumer<>(
                RefObject_1_0.class, 
                consumer, 
                marshaller
            )
        );
    }

    protected abstract void refProcessAll(
        AnyTypePredicate predicate, 
        Consumer<DataObject_1_0> consumer
    );
        
}
