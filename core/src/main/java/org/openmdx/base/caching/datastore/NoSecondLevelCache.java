/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: No Second Level Cache 
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

package org.openmdx.base.caching.datastore;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.jdo.datastore.DataStoreCache.EmptyDataStoreCache;

import org.openmdx.base.naming.Path;
import org.openmdx.base.rest.cci.ObjectRecord;


/**
 * No Second Level Cache
 * 
 * @since openMDX 2.17
 */
public class NoSecondLevelCache extends EmptyDataStoreCache implements CacheAdapter {

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#containsKey(org.openmdx.base.naming.Path)
     */
    @Override
    public boolean containsKey(Path key) {
        return false; // there is no cached object
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#offer(org.openmdx.base.rest.cci.ObjectRecord)
     */
    @Override
    public void offer(ObjectRecord objectRecord) {
        // Do not cache the object
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#get(org.openmdx.base.naming.Path)
     */
    @Override
    public ObjectRecord get(Path key) {
        return null; // there is no cached object
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#getAll(java.util.Set)
     */
    @Override
    public Map<Path, ObjectRecord> getAll(Set<Path> keys) {
        return Collections.emptyMap(); // There are no cached objects
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#evictAll(java.util.function.Predicate)
     */
    @Override
    public void evictAll(Predicate<ObjectRecord> filter) {
        // There are no cached objects to be evicted
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#pinAll(java.util.function.Predicate)
     */
    @Override
    public void pinAll(Predicate<ObjectRecord> filter) {
        // No objects are pinned
    }

    /* (non-Javadoc)
     * @see org.openmdx.base.caching.datastore.CacheAdapter#unpinAll(java.util.function.Predicate)
     */
    @Override
    public void unpinAll(Predicate<ObjectRecord> filter) {
        // No objects are pinned
    }

    /* (non-Javadoc)
     * @see org.openmdx.kernel.jdo.JDODataStoreCache#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> clazz) {
        throw new IllegalArgumentException(
            clazz.getName() + " is not supprted by " + getClass().getName()
        );
    }

}
