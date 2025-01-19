/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Set Record 
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

import java.util.Collection;

/**
 * {@code "set"} Record
 */
@SuppressWarnings("rawtypes")
final class SetRecord 
    extends VariableSizeIndexedRecord 
    implements org.openmdx.base.resource.cci.SetRecord 
{

    SetRecord() {
        super(NAME);
    }

    /**
     * Constructor 
     *
     * @param initialContent the values to be added initially
     */
    public SetRecord(
        Collection<?> initialContent
    ) {
        super(NAME);
        addAll(initialContent);
    }

    /**
     * Implements {@code Serializable}
     */
    private static final long serialVersionUID = 2526822916360414211L;

    /* (non-Javadoc)
     * @see java.util.AbstractList#add(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean add(Object e) {
        return !contains(e) && super.add(e);
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.resource.spi.VariableSizeIndexedRecord#add(int, java.lang.Object)
     */
    @Override
    public void add(
        int index,
        Object element
    ) {
        addToSet(index, element); 
    }

    private boolean addToSet(int index, Object element) {
       final boolean modified = !contains(element);
       super.add(index, element);
       return modified;
    }
    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection c) {
        boolean modified = false;
        for(Object e : c) {
            modified |= add(e);
        }
        return modified;
    }

    /* (non-Javadoc)
     * @see java.util.AbstractList#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(
        final int index,
        final Collection c
    ) {
        int current = index;
        boolean modified = false;
        for(Object e : c) {
            if(addToSet(current, e)) {
                modified = true;
                current++;
            }
        }
        return modified;
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.resource.spi.VariableSizeIndexedRecord#set(int, java.lang.Object)
     */
    @Override
    public Object set(
        int index,
        Object element
    ) {
        final int currentIndex = super.indexOf(element);
        if(currentIndex >= 0 && currentIndex != index) {
            super.set(currentIndex, null); // Avoid duplicate entries
        }
        return super.set(index, element);
    }

}
