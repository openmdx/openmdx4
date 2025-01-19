/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation - http://www.apache.org/"
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * http://www.apache.org/
 *
 */

package org.openmdx.uses.org.apache.commons.pool.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.openmdx.uses.org.apache.commons.pool.BaseKeyedObjectPool;
import org.openmdx.uses.org.apache.commons.pool.KeyedObjectPool;
import org.openmdx.uses.org.apache.commons.pool.KeyedPoolableObjectFactory;

/**
 * A simple, {@link java.util.Stack Stack}-based {@link KeyedObjectPool} implementation.
 * <p>
 * Given a {@link KeyedPoolableObjectFactory}, this class will maintain
 * a simple pool of instances.  A finite number of "sleeping"
 * or inactive instances is enforced, but when the pool is
 * empty, new instances are created to support the new load.
 * Hence this class places no limit on the number of "active"
 * instances created by the pool, but is quite useful for
 * re-using <tt>Object</tt>s without introducing
 * artificial limits.
 *
 * @author Rodney Waldhoff
 * @version $Id: StackKeyedObjectPool.java,v 1.7 2011/04/12 15:30:21 hburger Exp $
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class StackKeyedObjectPool extends BaseKeyedObjectPool implements KeyedObjectPool {
    /**
     * Create a new pool using
     * no factory. Clients must first populate the pool
     * using {@link #returnObject(java.lang.Object,java.lang.Object)}
     * before they can be {@link #borrowObject(java.lang.Object) borrowed}.
     */
    public StackKeyedObjectPool() {
        this((KeyedPoolableObjectFactory)null,DEFAULT_MAX_SLEEPING,DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    /**
     * Create a new pool using
     * no factory. Clients must first populate the pool
     * using {@link #returnObject(java.lang.Object,java.lang.Object)}
     * before they can be {@link #borrowObject(java.lang.Object) borrowed}.
     *
     * @param max cap on the number of "sleeping" instances in the pool
     */
    public StackKeyedObjectPool(int max) {
        this((KeyedPoolableObjectFactory)null,max,DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    /**
     * Create a new pool using
     * no factory. Clients must first populate the pool
     * using {@link #returnObject(java.lang.Object,java.lang.Object)}
     * before they can be {@link #borrowObject(java.lang.Object) borrowed}.
     *
     * @param max cap on the number of "sleeping" instances in the pool
     * @param init initial size of the pool (this specifies the size of the container,
     *             it does not cause the pool to be pre-populated.)
     */
    public StackKeyedObjectPool(int max, int init) {
        this((KeyedPoolableObjectFactory)null,max,init);
    }

    /**
     * Create a new <tt>SimpleKeyedObjectPool</tt> using
     * the specified <i>factory</i> to create new instances.
     *
     * @param factory the {@link KeyedPoolableObjectFactory} used to populate the pool
     */
    public StackKeyedObjectPool(KeyedPoolableObjectFactory factory) {
        this(factory,DEFAULT_MAX_SLEEPING);
    }

    /**
     * Create a new <tt>SimpleKeyedObjectPool</tt> using
     * the specified <i>factory</i> to create new instances.
     * capping the number of "sleeping" instances to <i>max</i>
     *
     * @param factory the {@link KeyedPoolableObjectFactory} used to populate the pool
     * @param max cap on the number of "sleeping" instances in the pool
     */
    public StackKeyedObjectPool(KeyedPoolableObjectFactory factory, int max) {
        this(factory,max,DEFAULT_INIT_SLEEPING_CAPACITY);
    }

    /**
     * Create a new <tt>SimpleKeyedObjectPool</tt> using
     * the specified <i>factory</i> to create new instances.
     * capping the number of "sleeping" instances to <i>max</i>,
     * and initially allocating a container capable of containing
     * at least <i>init</i> instances.
     *
     * @param factory the {@link KeyedPoolableObjectFactory} used to populate the pool
     * @param max cap on the number of "sleeping" instances in the pool
     * @param init initial size of the pool (this specifies the size of the container,
     *             it does not cause the pool to be pre-populated.)
     */
    public StackKeyedObjectPool(KeyedPoolableObjectFactory factory, int max, int init) {
        _factory = factory;
        _maxSleeping = (max < 0 ? DEFAULT_MAX_SLEEPING : max);
        _initSleepingCapacity = (init < 1 ? DEFAULT_INIT_SLEEPING_CAPACITY : init);
        _pools = new HashMap();
        _activeCount = new HashMap();
    }

    @Override
    public synchronized Object borrowObject(Object key) throws Exception {
        Object obj = null;
        Stack stack = (Stack)(_pools.get(key));
        if(null == stack) {
            stack = new Stack();
            stack.ensureCapacity( _initSleepingCapacity > _maxSleeping ? _maxSleeping : _initSleepingCapacity);
            _pools.put(key,stack);
        }
        try {
            obj = stack.pop();
            _totIdle--;
        } catch(Exception e) {
            if(null == _factory) {
                throw new NoSuchElementException();
            } else {
                obj = _factory.makeObject(key);
            }
        }
        if(null != obj && null != _factory) {
            _factory.activateObject(key,obj);
        }
        incrementActiveCount(key);
        return obj;
    }

    @Override
    public synchronized void returnObject(Object key, Object obj) throws Exception {
        decrementActiveCount(key);
        if(null == _factory || _factory.validateObject(key,obj)) {
            Stack stack = (Stack)(_pools.get(key));
            if(null == stack) {
                stack = new Stack();
                stack.ensureCapacity( _initSleepingCapacity > _maxSleeping ? _maxSleeping : _initSleepingCapacity);
                _pools.put(key,stack);
            }
            if(null != _factory) {
                try {
                    _factory.passivateObject(key,obj);
                } catch(Exception e) {
                    _factory.destroyObject(key,obj);
                    return;
                }
            }
            if(stack.size() < _maxSleeping) {
                stack.push(obj);
                _totIdle++;
            } else {
                if(null != _factory) {
                    _factory.destroyObject(key,obj);
                }
            }
        } else {
            if(null != _factory) {
                _factory.destroyObject(key,obj);
            }
        }
    }

    @Override
    public synchronized void invalidateObject(Object key, Object obj) throws Exception {
        decrementActiveCount(key);
        if(null != _factory) {
            _factory.destroyObject(key,obj);
        }
        notifyAll(); // _totalActive has changed
    }

    @Override
    public void addObject(Object key) throws Exception {
        Object obj = _factory.makeObject(key);
        synchronized(this) {
            incrementActiveCount(key); // returnObject will decrement this
            returnObject(key,obj);
        }
    }

    @Override
    public int getNumIdle() {
        return _totIdle;
    }

    @Override
    public int getNumActive() {
        return _totActive;
    }

    @Override
    public int getNumActive(Object key) {
        return getActiveCount(key);
    }

    @Override
    public synchronized int getNumIdle(Object key) {
        try {
            return((Stack)(_pools.get(key))).size();
        } catch(Exception e) {
            return 0;
        }
    }

    @Override
    public synchronized void clear() {
        Iterator it = _pools.keySet().iterator();
        while(it.hasNext()) {
            Object key = it.next();
            Stack stack = (Stack)(_pools.get(key));
            destroyStack(key,stack);
        }
        _totIdle = 0;
        _pools.clear();
        _activeCount.clear();
    }

    @Override
    public synchronized void clear(Object key) {
        Stack stack = (Stack)(_pools.remove(key));
        destroyStack(key,stack);
    }

    private synchronized void destroyStack(Object key, Stack stack) {
        if(null == stack) {
            return;
        } else {
            if(null != _factory) {
                Enumeration e = stack.elements();
                while(e.hasMoreElements()) {
                    try {
                        _factory.destroyObject(key,e.nextElement());
                    } catch(Exception exception) {
                        // ignore error, keep destroying the rest
                    }
                }
            }
            _totIdle -= stack.size();
            _activeCount.remove(key);
            stack.clear();
        }
    }

    @Override
    public synchronized String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getClass().getName());
        buf.append(" contains ").append(_pools.size()).append(" distinct pools: ");
        Iterator it = _pools.keySet().iterator();
        while(it.hasNext()) {
            Object key = it.next();
            buf.append(" |").append(key).append("|=");
            Stack s = (Stack)(_pools.get(key));
            buf.append(s.size());
        }
        return buf.toString();
    }

    @Override
    public synchronized void close() throws Exception {
        clear();
        _pools = null;
        _factory = null;
        _activeCount = null;
    }

    @Override
    public synchronized void setFactory(KeyedPoolableObjectFactory factory) throws IllegalStateException {
        if(0 < getNumActive()) {
            throw new IllegalStateException("Objects are already active");
        } else {
            clear();
            _factory = factory;
        }
    }

    private int getActiveCount(Object key) {
        try {
            return ((Integer)_activeCount.get(key)).intValue();
        } catch(NoSuchElementException e) {
            return 0;
        } catch(NullPointerException e) {
            return 0;
        }
    }

    private void incrementActiveCount(Object key) {
        _totActive++;
        Integer old = (Integer)(_activeCount.get(key));
        if(null == old) { 
            _activeCount.put(key,Integer.valueOf(1));
        } else {
            _activeCount.put(key,Integer.valueOf(old.intValue() + 1));
        }
    }

    private void decrementActiveCount(Object key) {
        _totActive--;
        Integer active = (Integer)(_activeCount.get(key));
        if(null == active) {
            // do nothing, either null or zero is OK
        } else if(active.intValue() <= 1) {
            _activeCount.remove(key);
        } else {
            _activeCount.put(key, Integer.valueOf(active.intValue() - 1));
        }
    }

    /** The default cap on the number of "sleeping" instances in the pool. */
    protected static final int DEFAULT_MAX_SLEEPING  = 8;

    /**
     * The default initial size of the pool
     * (this specifies the size of the container, it does not
     * cause the pool to be pre-populated.)
     */
    protected static final int DEFAULT_INIT_SLEEPING_CAPACITY = 4;

    /** My named-set of pools. */
    protected HashMap _pools = null;

    /** My {@link KeyedPoolableObjectFactory}. */
    protected KeyedPoolableObjectFactory _factory = null;

    /** The cap on the number of "sleeping" instances in <i>each</i> pool. */
    protected int _maxSleeping = DEFAULT_MAX_SLEEPING;

    /** The initial capacity of each pool. */
    protected int _initSleepingCapacity = DEFAULT_INIT_SLEEPING_CAPACITY;

    /** Total number of object borrowed and not yet retuened for all pools */
    protected int _totActive = 0;

    /** Total number of objects "sleeping" for all pools */
    protected int _totIdle = 0;

    /** Number of active objects borrowed and not yet returned by pool */
    protected HashMap _activeCount = null;

}
