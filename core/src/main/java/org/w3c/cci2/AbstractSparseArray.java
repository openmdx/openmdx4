/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Abstract Sparse Array 
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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.openmdx.kernel.collection.InternalizedKeys;

/**
 * Abstract Sparse Array
 */
public abstract class AbstractSparseArray<E> implements SparseArray<E> {

    /**
     * Constructor
     */
    protected AbstractSparseArray() {
        super();
    }

    /**
     * This member caches the sparse array's list view
     */
    private transient List<E> list = null;

    /**
     * This member caches the sparse array's entry set view
     */
    private transient Set<Map.Entry<Integer, E>> set = null;

    /**
     * The sorted map by which the sparse array will is backed
     */
    protected abstract SortedMap<Integer, E> delegate();

    /**
     * Create a sub-array of the same concrete type
     * 
     * @param delegate the delegate
     * 
     * @return a concrete sub-array
     */
    protected abstract SparseArray<E> subArray(
        SortedMap<Integer, E> delegate
    );
    
    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractMap#entrySet()
     */
    public Set<Map.Entry<Integer, E>> entrySet() {
        return this.set == null ? this.set = new AbstractSet<Map.Entry<Integer, E>>() {

            @Override
            public int size() {
                return AbstractSparseArray.this.delegate().size();
            }

            @Override
            public Iterator<Map.Entry<Integer, E>> iterator() {
                return new Iterator<Map.Entry<Integer, E>>() {

                    final Iterator<Map.Entry<Integer, E>> iterator = AbstractSparseArray.this.delegate().entrySet().iterator();

                    public boolean hasNext() {
                        return this.iterator.hasNext();
                    }

                    public Map.Entry<Integer, E> next() {
                        return new Map.Entry<Integer, E>() {

                            private final Map.Entry<Integer, E> entry = iterator.next();

                            public Integer getKey() {
                                return this.entry.getKey();
                            }

                            public E getValue() {
                                return this.entry.getValue();
                            }

                            public E setValue(E value) {
                                if (value == null) {
                                    E old = this.entry.getValue();
                                    iterator.remove();
                                    return old;
                                } else {
                                    return this.entry.setValue(value);
                                }
                            }

                        };
                    }

                    public void remove() {
                        this.iterator.remove();
                    }

                };
            }

        } : this.set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
     */
    public E put(
        Integer key,
        E value
    ) {
        return value == null ? this.delegate().remove(key) : this.delegate().put(InternalizedKeys.internalize(key), value);
    }

    /**
     * A list backed up by the sparse array:
     * <ul>
     * <li>its size() is the sparse array's {@code lastKey() + 1}
     * <li>the sparse array's un-populated positions are represented as {@code null</null> values
     * <li>get(int) and set(int,E) operations are allowed for any index >= 0
     * <li>clear() and add(E) are supported
     * <li>remove(int) and add(int,E) are not supported
     * </ul>
     *  
     * &#64;return a {@code List} representing this {@code SparseArray}
     */
    public List<E> asList() {
        return this.list == null ? this.list = new AsList<E>(this) : this.list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedMap#comparator()
     */
    public Comparator<? super Integer> comparator() {
        return this.delegate().comparator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedMap#firstKey()
     */
    public Integer firstKey() {
        return this.delegate().firstKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.SortedMap#lastKey()
     */
    public Integer lastKey() {
        return this.delegate().lastKey();
    }

    /**
    * Returns a view of the portion of this sparse array whose keys range from
    * <tt>fromKey</tt>, inclusive, to <tt>toKey</tt>, exclusive. (If
    * <tt>fromKey</tt> and <tt>toKey</tt> are equal, the returned sparse array
    * is empty.) The returned sparse array is backed by this sparse array, so
    * changes in the returned sparse array are reflected in this sparse array,
    * and vice-versa. The returned Map supports all optional map operations
    * that this sparse array supports.
    * <p>
    *
    * The map returned by this method will throw an
    * <tt>IllegalArgumentException</tt> if the user attempts to insert a key
    * outside the specified range.
    * <p>
    *
    * Note: this method always returns a <i>half-open range</i> (which
    * includes its low endpoint but not its high endpoint). If you need a
    * <i>closed range</i> (which includes both endpoints), and the key type
    * allows for calculation of the successor a given key, merely request the
    * subrange from <tt>lowEndpoint</tt> to <tt>successor(highEndpoint)</tt>.
    * Sparse arrays are map whose keys are integers.
    * The following idiom obtains a view containing all of the key-value
    * mappings in <tt>m</tt> whose keys are between <tt>low</tt> and
    * <tt>high</tt>, inclusive:
    * 
    * <pre>
    * 
    * Map sub = m.subMap(low, high + 1);
    * </pre>
    * 
    * A similarly technique can be used to generate an <i>open range</i>
    * (which contains neither endpoint). The following idiom obtains a
    * view containing all of the key-value mappings in <tt>m</tt> whose keys
    * are between <tt>low</tt> and <tt>high</tt>, exclusive:
    * 
    * <pre>
    * 
    * Map sub = m.subMap(low + 1, high);
    * </pre>
    *
    * @param fromKey
    *            low endpoint (inclusive) of the subMap.
    * @param toKey
    *            high endpoint (exclusive) of the subMap.
    * @return a view of the specified range within this sparse array.
    * 
    * @throws ClassCastException
    *             if <tt>fromKey</tt> and <tt>toKey</tt>
    *             cannot be compared to one another using this map's comparator
    *             (or, if the map has no comparator, using natural ordering).
    *             Implementations may, but are not required to, throw this
    *             exception if <tt>fromKey</tt> or <tt>toKey</tt>
    *             cannot be compared to keys currently in the map.
    * @throws IllegalArgumentException
    *             if <tt>fromKey</tt> is greater than
    *             <tt>toKey</tt>; or if this map is itself a subMap, headMap,
    *             or tailMap, and <tt>fromKey</tt> or <tt>toKey</tt> are not
    *             within the specified range of the subMap, headMap, or tailMap.
    * @throws NullPointerException
    *             if <tt>fromKey</tt> or <tt>toKey</tt> is
    *             <tt>null</tt> and this sparse array does not tolerate
    *             <tt>null</tt> keys.
    */
    public SparseArray<E> subMap(
        Integer fromKey,
        Integer toKey
    ) {
        return subArray(
            delegate().subMap(fromKey, toKey)
        );
    }

    /**
    * Returns a view of the portion of this sparse array whose keys are greater
    * than or equal to <tt>fromKey</tt>. The returned sparse array is backed
    * by this sparse array, so changes in the returned sparse array are reflected
    * in this sparse array, and vice-versa. The returned map supports all
    * optional map operations that this sparse array supports.
    * <p>
    *
    * The map returned by this method will throw an
    * <tt>IllegalArgumentException</tt> if the user attempts to insert a key
    * outside the specified range.
    * <p>
    *
    * Note: this method always returns a view that contains its (low)
    * endpoint. If you need a view that does not contain this endpoint, and
    * the element type allows for calculation of the successor a given value,
    * merely request a tailMap bounded by <tt>successor(lowEndpoint)</tt>.
    * Sparse arrays are map whose keys are integers.
    * The following idiom obtains a view containing all of the
    * key-value mappings in <tt>m</tt> whose keys are strictly greater than
    * <tt>low</tt>:
    * 
    * <pre>
    * 
    * Map tail = m.tailMap(low + 1);
    * </pre>
    *
    * @param fromKey
    *            low endpoint (inclusive) of the tailMap.
    * @return a view of the specified final range of this sparse array.
    * @throws ClassCastException
    *             if <tt>fromKey</tt> is not compatible
    *             with this map's comparator (or, if the map has no comparator,
    *             if <tt>fromKey</tt> does not implement <tt>Comparable</tt>).
    *             Implementations may, but are not required to, throw this
    *             exception if <tt>fromKey</tt> cannot be compared to keys
    *             currently in the map.
    * @throws IllegalArgumentException
    *             if this map is itself a subMap,
    *             headMap, or tailMap, and <tt>fromKey</tt> is not within the
    *             specified range of the subMap, headMap, or tailMap.
    * @throws NullPointerException
    *             if <tt>fromKey</tt> is <tt>null</tt> and
    *             this sparse array does not tolerate <tt>null</tt> keys.
    */
    public SparseArray<E> tailMap(Integer fromKey) {
        return subArray(delegate().tailMap(fromKey));
    }

    /**
    * Returns a view of the portion of this sparse array whose keys are
    * strictly less than toKey. The returned sparse array is backed by this
    * sparse array, so changes in the returned sparse array are reflected in this
    * sparse array, and vice-versa. The returned map supports all optional map
    * operations that this sparse array supports.
    * <p>
    *
    * The map returned by this method will throw an IllegalArgumentException
    * if the user attempts to insert a key outside the specified range.
    * <p>
    *
    * Note: this method always returns a view that does not contain its
    * (high) endpoint. If you need a view that does contain this endpoint,
    * and the key type allows for calculation of the successor a given
    * key, merely request a headMap bounded by successor(highEndpoint).
    * Sparse arrays are map whose keys are integers.
    * The following idiom obtains a view containing all of the
    * key-value mappings in <tt>m</tt> whose keys are less than or equal to
    * <tt>high</tt>:
    * 
    * <pre>
    * 
    * Map head = m.headMap(high + 1);
    * </pre>
    *
    * @param toKey
    *            high endpoint (exclusive) of the subMap.
    * @return a view of the specified initial range of this sparse array.
    * @throws ClassCastException
    *             if <tt>toKey</tt> is not compatible
    *             with this map's comparator (or, if the map has no comparator,
    *             if <tt>toKey</tt> does not implement <tt>Comparable</tt>).
    *             Implementations may, but are not required to, throw this
    *             exception if <tt>toKey</tt> cannot be compared to keys
    *             currently in the map.
    * @throws IllegalArgumentException
    *             if this map is itself a subMap,
    *             headMap, or tailMap, and <tt>toKey</tt> is not within the
    *             specified range of the subMap, headMap, or tailMap.
    * @throws NullPointerException
    *             if <tt>toKey</tt> is <tt>null</tt> and
    *             this sparse array does not tolerate <tt>null</tt> keys.
    */

    public SparseArray<E> headMap(Integer toKey) {
        return subArray(delegate().headMap(toKey));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<E> iterator() {
        return this.delegate().values().iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.w3c.cci2.SparseArray#populationIterator()
     */
    public ListIterator<E> populationIterator() {
        return new ListIterator<E>() {

            private final List<Integer> list = new ArrayList<Integer>(keySet());
            private final ListIterator<Integer> iterator = list.listIterator();

            Integer current = null;

            public boolean hasNext() {
                return this.iterator.hasNext();
            }

            public E next() {
                return get(
                    current = this.iterator.next()
                );
            }

            public void remove() {
                if (current == null) {
                    throw new IllegalStateException(ILLEGAL_STATE);
                } else {
                    this.iterator.remove();
                    put(this.current, null);
                    this.current = null;
                }
            }

            public void add(E o) {
                if (current == null) {
                    throw new IllegalStateException(ILLEGAL_STATE);
                } else {
                    Integer i = Integer.valueOf(current.intValue() + 1);
                    E e = get(i);
                    if (e == null) {
                        current = i;
                        put(i, o);
                        this.iterator.add(i);
                    } else {
                        throw new IllegalStateException(NO_SPACE);
                    }
                }
            }

            public boolean hasPrevious() {
                return this.iterator.hasPrevious();
            }

            public int nextIndex() {
                int i = this.iterator.nextIndex();
                return i < list.size() ? this.list.get(i).intValue() : this.list.get(i - 1).intValue() + 1;
            }

            public E previous() {
                return get(
                    current = this.iterator.previous()
                );
            }

            public int previousIndex() {
                int i = this.iterator.previousIndex();
                return i < 0 ? -1 : this.list.get(i).intValue();
            }

            public void set(E o) {
                if (this.current == null) {
                    throw new IllegalStateException(ILLEGAL_STATE);
                } else {
                    put(this.current, o);
                }
            }

        };
    }

    private static final String ILLEGAL_STATE = "Population iterator has no current element";

    private static final String NO_SPACE = "Population iterator has no room to add an element";

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#clear()
     */
    public void clear() {
        this.delegate().clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return this.delegate().containsKey(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
        return this.delegate().containsValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#get(java.lang.Object)
     */
    public E get(Object key) {
        return this.delegate().get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#keySet()
     */
    public Set<Integer> keySet() {
        return this.delegate().keySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends Integer, ? extends E> t) {
        for (Map.Entry<? extends Integer, ? extends E> entry : t.entrySet()) {
            put(entry.getKey(), entry.getValue()); // Internalizing or removing keys
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#remove(java.lang.Object)
     */
    public E remove(Object key) {
        return this.delegate().remove(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#size()
     */
    public int size() {
        return this.delegate().size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Map#values()
     */
    public Collection<E> values() {
        return this.delegate().values();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(
        Object obj
    ) {
        if (obj instanceof AbstractSparseArray<?>) {
            AbstractSparseArray<?> that = (AbstractSparseArray<?>) obj;
            return this.delegate().equals(that.delegate());
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.delegate().toString();
    }

    
    //------------------------------------------------------------------------
    // Class AsList
    //------------------------------------------------------------------------

    /**
     * AsList
     * <p>
     * A list backed up by the sparse array:
     * <ul>
     * <li>its size() is the sparse array's {@code lastKey() + 1}
     * <li>the sparse array's un-populated positions are represented as {@code null</null> values
     * <li>get() and set() operations are allowed for any index >= 0
     * <li>clear() and add(E) are supported
     * <li>remove(int) and add(int,E) are not supported
     * </ul>
     */
    private final static class AsList<E>
        extends AbstractList<E>
        implements Serializable {

        /**
         * Constructor
         * @param delegate
         */
        AsList(
            final SortedMap<Integer, E> delegate
        ) {
            this.delegate = delegate;
        }

        /**
         * Implements {@code Serializable}
         */
        private static final long serialVersionUID = -7591560889642014116L;

        /**
         * 
         */

        private final SortedMap<Integer, E> delegate;

        /*
         * (non-Javadoc)
         * 
         * @see java.util.AbstractList#get(int)
         */

        @Override
        public E get(int index) {
            return this.delegate.get(Integer.valueOf(index));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.AbstractCollection#size()
         */
        @Override
        public int size() {
            return this.delegate.isEmpty() ? 0 : this.delegate.lastKey().intValue() + 1;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.AbstractList#set(int, java.lang.Object)
         */
        @Override
        public E set(
            int index,
            E element
        ) {
            return this.delegate.put(
                Integer.valueOf(index),
                element
            );
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.AbstractList#indexOf(java.lang.Object)
         */
        @Override
        public int indexOf(Object o) {
            if (o == null) {
                int i = -1;
                for (Map.Entry<Integer, E> e : this.delegate.entrySet()) {
                    if (++i != e.getKey().intValue())
                        return i;
                }
            } else {
                for (Map.Entry<Integer, E> e : this.delegate.entrySet()) {
                    if (o.equals(e.getValue()))
                        return e.getKey().intValue();
                }
            }
            return -1;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.AbstractList#add(java.lang.Object)
         */
        @Override
        public boolean add(E o) {
            if (o == null) {
                return false;
            } else {
                if (this.delegate.isEmpty()) {
                    try {
                        this.delegate.put(0, o);
                    } catch (IllegalArgumentException keyRangeException) {
                        throw (IndexOutOfBoundsException) new IndexOutOfBoundsException(
                            "Unable to add a value to an empty SparseArray backed up by a SortedMap disallowing the index 0"
                        ).initCause(keyRangeException);
                    }
                } else {
                    final int index = this.delegate.lastKey().intValue() + 1;
                    try {
                        this.delegate.put(Integer.valueOf(index), o);
                    } catch (IllegalArgumentException keyRangeException) {
                        throw (IndexOutOfBoundsException) new IndexOutOfBoundsException(
                            "Unable to add a value to a full SparseArray backed up by a SortedMap disallowing the index " + index
                        ).initCause(keyRangeException);
                    }
                }
                return true;
            }
        }

        /**
         * Removes from this list all of the elements whose index is between
         * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.
         * Shifts any succeeding elements to the left (reduces their index). This
         * call shortens the ArrayList by <tt>(toIndex - fromIndex)</tt>
         * elements. (If <tt>toIndex==fromIndex</tt>, this operation has no
         * effect.)
         * <p>
         *
         * This method is called by the <tt>clear</tt> operation on this list
         * and its subLists. Overriding this method to take advantage of
         * the internals of the list implementation can <i>substantially</i>
         * improve the performance of the <tt>clear</tt> operation on this list
         * and its subLists.
         * <p>
         *
         * This implementation gets a list iterator positioned before
         * <tt>fromIndex</tt>, and repeatedly calls <tt>ListIterator.next</tt>
         * followed by <tt>ListIterator.remove</tt> until the entire range has
         * been removed. <b>Note: if <tt>ListIterator.remove</tt> requires linear
         * time, this implementation requires quadratic time.</b>
         *
         * @param fromIndex
         *            index of first element to be removed.
         * @param toIndex
         *            index after last element to be removed.
         */
        @Override
        protected void removeRange(
            int fromIndex,
            int toIndex
        ) {
            this.delegate.subMap(
                Integer.valueOf(fromIndex),
                Integer.valueOf(toIndex)
            ).clear();
        }

    }

}
