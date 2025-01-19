/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Maps
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
package org.openmdx.base.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maps
 */
public class Maps {

    /**
     * Constructor 
     */
    protected Maps() {
        // Avoid instantiation
    }

    /**
     * A {@code NULL} value for maps not allowing {@code null} as value.
     */
    public static final Object NULL = new Object();

    /**
     * Create a null masking hash map
     * 
     * @param threadSafetyRequired
     *            defines whether thread safety is required or not
     * 
     * @return a newly created null masking map
     */
    @SuppressWarnings("unchecked")
    public static <K,V> Map<K,V> newMapSupportingNullValues(
        boolean threadSafetyRequired
    ){
        return threadSafetyRequired ? NullMaskingMap.decorate(
            new ConcurrentHashMap<K,Object>(),
            NULL
        ) : new HashMap<>();
    }

    /**
     * Create a concurrent or standard hash map, depending on the argument.
     * {@code null} will neither be supported as key nor as value
     * 
     * @param threadSafetyRequired
     *            defines whether thread safety is required or not
     *            
     * @return a newly created hash map
     */
    public static <K,V> Map<K,V> newMap(
        boolean threadSafetyRequired
    ){
        return threadSafetyRequired ? new ConcurrentHashMap<>() : new HashMap<>();
    }
    
    /**
     * Return the <em>actual</em> value
     * 
     * @param target the concurrent map
     * @param key
     * @param value
     * 
     * @return the <em>new</em> or <em>kept</em> value
     */
    public static <K,V> V putUnlessPresent(
        Map<K,V> target,
        K key,
        V value
    ){
        V concurrent = target.putIfAbsent(key, value);
        return concurrent == null ? value : concurrent;
    }

}
