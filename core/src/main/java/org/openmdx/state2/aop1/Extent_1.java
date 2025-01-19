/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: State Object Container
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

import org.openmdx.base.accessor.cci.Container_1_0;
import org.openmdx.base.accessor.cci.SystemAttributes;
import org.openmdx.base.accessor.view.ObjectView_1_0;
import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.query.ConditionType;
import org.openmdx.base.query.Quantifier;
import org.openmdx.base.rest.cci.ConditionRecord;
import org.openmdx.base.rest.cci.QueryFilterRecord;

/**
 * org::openmdx::state2 aware extent
 */
public abstract class Extent_1 extends org.openmdx.base.aop1.Extent_1 {

    /**
     * Constructor 
     *
     * @param parent
     * @param container
     */
    protected Extent_1(
        ObjectView_1_0 parent,
        Container_1_0 container
    ) throws ServiceException {
        super(parent,container);
    }

    /**
     * An org::openmdx::state2 aware container
     */
    private transient Container_1_0 stateCapableContainer;
    
    /**
     * Implements {@code Serializable}
     */
    private static final long serialVersionUID = -7540665569698526423L;

    abstract Container_1_0 newStateCapableContainer(
	    ObjectView_1_0 parent,
	    Container_1_0 container, 
	    String type 
	) throws ServiceException;
    
    /**
     * Retrieve the state capable container
     */
    private Container_1_0 getStateCapableContainer(
    ) throws ServiceException{
        if(this.stateCapableContainer == null) {
            this.stateCapableContainer = newStateCapableContainer(
                this.parent,
                this.container, 
                null
            );
        }
        return this.stateCapableContainer;
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.collection.FilterableMap#subMap(java.lang.Object)
     */
    @Override
    public Container_1_0 subMap(QueryFilterRecord filter) {
        if(filter == null) {
            return null;
        } else try {
            for(ConditionRecord filterProperty : filter.getCondition()){
                if(
                    Quantifier.THERE_EXISTS == filterProperty.getQuantifier() &&
                    SystemAttributes.OBJECT_INSTANCE_OF.equals(filterProperty.getFeature()) &&
                    ConditionType.IS_IN == filterProperty.getType() &&
                    filterProperty.getValue().length == 1 &&
                    this.getModel().isSubtypeOf(filterProperty.getValue(0), "org:openmdx:state2:BasicState")
                ){
                    return this.getStateCapableContainer().subMap(filter);
                }
            }
            return super.subMap(filter);
        } catch (ServiceException exception) {
            throw new RuntimeServiceException(exception);
        }
    }
    
}
