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
import org.openmdx.base.accessor.view.ObjectView_1_0;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;
import org.openmdx.base.query.Filter;
import org.openmdx.state2.spi.LegacyPlugInHelper;

/**
 * Legacy {@code StateCapable} container
 */
public class LegacyStateCapableContainer_1 extends StateCapableContainer_1 {

	/**
	 * Constructor
	 * 
	 * @param parent
	 * @param container
	 * @param type
	 * 
	 * @throws ServiceException
	 */
	public LegacyStateCapableContainer_1(
		ObjectView_1_0 parent,
		Container_1_0 container, 
		String type
	) throws ServiceException {
		super(parent, container, type);
	}

	/**
	 * Implements {@code Serializable}
	 */
	private static final long serialVersionUID = 6629852248986498333L;

	/**
     * Derive the filter from the state context
     * 
     * @param parent
     * @param containerId
     * @param type 
     * 
     * @return the corresponding filter
     * 
     * @throws ServiceException 
     */
	@Override
    protected Filter getFilter(
        ObjectView_1_0 parent,
        Path containerId, 
        String type
    ) throws ServiceException {
        return LegacyPlugInHelper.isValidTimeUnique(parent, containerId) ? 
            null : 
            super.getFilter(parent, containerId, type);
    }

}
