/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Container_1_0 
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
package org.openmdx.base.accessor.cci;

import java.util.List;
import java.util.Map;

import javax.jdo.FetchPlan;

import org.openmdx.base.persistence.spi.PersistenceCapableCollection;
import org.openmdx.base.rest.cci.FeatureOrderRecord;
import org.openmdx.base.rest.cci.QueryFilterRecord;

import java.util.function.Consumer; 

/**
 * Container_1_0
 */
public interface Container_1_0 
    extends PersistenceCapableCollection, Map<String,DataObject_1_0> 
{

    /**
     * Retrieve the selection's container
     * 
     * @return the selection's container
     */
    Container_1_0 container();

    /**
     * Selects objects matching the filter.
     *
     * @param       filter
     *              The filter to be applied to objects of this container
     *
     * @return      A subset of this container containing the objects
     *              matching the filter.
     * 
     * @exception   ClassCastException
     *              if the class of the specified filter prevents it from
     *              being applied to this container.
     * @exception   IllegalArgumentException
     *              if some aspect of this filter prevents it from being
     *              applied to this container.
     */
    Container_1_0 subMap(
        QueryFilterRecord filter
    );

    /**
     * Applies given criteria to the elements of the container and returns the
     * result as list.
     * 
     * @param fetchPlan 
     * @param       criteria
     *                The criteria to be applied to objects of this container;
     *                or {@code null} for all the container's elements in
     *                      their default order.
     *
     * @return    a list based on the container's elements and the given
     *                      criteria.
     * 
     * @exception   ClassCastException
     *                  if the class of the specified criteria prevents them from
     *                  being applied to this container's elements.
     */
    List<DataObject_1_0> values(
        FetchPlan fetchPlan, 
        FeatureOrderRecord... criteria
    );

    /**
     * Tells whether the collection has been loaded into the cache.
     * 
     * @return {@code true} if the collection has been loaded into the cache.
     */
    boolean isRetrieved();
    
    /**
     * Process all elements selected by the container
     * 
     * @param       @param consumer 
     *              all matching elements are offered to the consumer
     * @param       fetchPlan
     *              The fetch plan to be used to retrieve the elements
     * @param       criteria
     *              The criteria to be applied to objects of this container;
     *              or {@code null} for all the container's elements in
     *              their default order.
     */
    void processAll(
        FetchPlan fetchPlan, 
        FeatureOrderRecord[] criteria,
        Consumer<DataObject_1_0> consumer
    );
    
}
