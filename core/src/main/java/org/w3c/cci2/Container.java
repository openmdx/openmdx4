/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Container 
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
package org.w3c.cci2;

import java.util.Collection;
import java.util.List;

import java.util.function.Consumer;

/**
 * Container
 */
public interface Container<E>
    extends Collection<E> {

    /**
     * Retrieve all elements for which the predicate evaluates to true
     * 
     * @param predicate
     *            the predicate to be applied to the elements
     * 
     * @return all elements for which the predicate evaluates to true
     */
    List<E> getAll(
        AnyTypePredicate predicate
    );

    /**
     * Process all elements for which the predicate evaluates to true.
     * <p>
     * Processing is terminated prematurely if accept throws a {@code RuntimeException}.
     * 
     * @param predicate
     *            the predicate to be applied to the elements
     * @param consumer
     *            all matching elements are offered to the consumer
     */
    void processAll(
        AnyTypePredicate predicate,
        Consumer<E> consumer
    );

    /**
     * Remove all elements for which the predicate evaluates to true
     * 
     * @param predicate
     *            the predicate to be applied to the elements
     */
    void removeAll(
        AnyTypePredicate predicate
    );

    /**
     * This method should only be used to remove a transient object from its container.
     * <p>
     * <em>Note:<br>
     * It can never be used to remove an object by specifying its qualifier!
     * 
     * @deprecated validate, whether the argument is really the object and not its qualifier!
     */
    @Override
    @Deprecated
    boolean remove(Object o);

}
