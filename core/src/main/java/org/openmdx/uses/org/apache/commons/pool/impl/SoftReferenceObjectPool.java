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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.openmdx.uses.org.apache.commons.pool.BaseObjectPool;
import org.openmdx.uses.org.apache.commons.pool.ObjectPool;
import org.openmdx.uses.org.apache.commons.pool.PoolableObjectFactory;

/**
 * A {@link java.lang.ref.SoftReference SoftReference} based
 * {@link ObjectPool}.
 *
 * @author Rodney Waldhoff
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class SoftReferenceObjectPool extends BaseObjectPool implements ObjectPool {
    public SoftReferenceObjectPool() {
        _pool = new ArrayList();
        _factory = null;
    }

    public SoftReferenceObjectPool(PoolableObjectFactory factory) {
        _pool = new ArrayList();
        _factory = factory;
    }

    public SoftReferenceObjectPool(PoolableObjectFactory factory, int initSize) throws Exception {
        _pool = new ArrayList();
        _factory = factory;
        if(null != _factory) {
            for(int i=0;i<initSize;i++) {
                Object obj = _factory.makeObject();
                _factory.passivateObject(obj);
                _pool.add(new SoftReference(obj));
            }
        }
    }

    @Override
    public synchronized Object borrowObject() throws Exception {        
        assertOpen();
        Object obj = null;
        while(null == obj) {
            if(_pool.isEmpty()) {
                if(null == _factory) {
                    throw new NoSuchElementException();
                } else {
                    obj = _factory.makeObject();
                }
            } else {
                SoftReference ref = (SoftReference)(_pool.remove(_pool.size() - 1));
                obj = ref.get();
            }
        }
        if(null != _factory && null != obj) {
            _factory.activateObject(obj);
        }
        _numActive++;
        return obj;
    }

    @Override
    public void returnObject(Object obj) throws Exception {
        assertOpen();
        boolean success = true;
        if(!(_factory.validateObject(obj))) {
            success = false;
        } else {
            try {
                _factory.passivateObject(obj);
            } catch(Exception e) {
                success = false;
            }
        }

        boolean shouldDestroy = !success;
        synchronized(this) {
            _numActive--;
            if(success) {
                _pool.add(new SoftReference(obj));
            }
            notifyAll(); // _numActive has changed
        }

        if(shouldDestroy) {
            try {
                _factory.destroyObject(obj);
            } catch(Exception e) {
                // ignored
            }
        }

    }

    @Override
    public synchronized void invalidateObject(Object obj) throws Exception {
        assertOpen();
        _numActive--;
        _factory.destroyObject(obj);
        notifyAll(); // _numActive has changed
    }

    /**
     * Create an object, and place it into the pool.
     * addObject() is useful for "pre-loading" a pool with idle objects.
     */
    @Override
    public void addObject() throws Exception {
        Object obj = _factory.makeObject();
        synchronized(this) {
            _numActive++;   // A little slimy - must do this because returnObject decrements it.
            this.returnObject(obj);
        }
    }

    /** Returns an approximation not less than the of the number of idle instances in the pool. */
    @Override
    public int getNumIdle() {
        return _pool.size();
    }

    @Override
    public int getNumActive() {
        return _numActive;
    }

    @Override
    public synchronized void clear() {
        assertOpen();
        if(null != _factory) {
            Iterator iter = _pool.iterator();
            while(iter.hasNext()) {
                try {
                    Object obj = ((SoftReference)iter.next()).get();
                    if(null != obj) {
                        _factory.destroyObject(obj);
                    }
                } catch(Exception e) {
                    // ignore error, keep destroying the rest
                }
            }
        }
        _pool.clear();
    }

    @Override
    synchronized public void close() throws Exception {
        clear();
        _pool = null;
        _factory = null;
        super.close();
    }

    @Override
    synchronized public void setFactory(PoolableObjectFactory factory) throws IllegalStateException {
        assertOpen();
        if(0 < getNumActive()) {
            throw new IllegalStateException("Objects are already active");
        } else {
            clear();
            _factory = factory;
        }
    }

    /** My pool. */
    private List _pool = null;

    /** My {@link PoolableObjectFactory}. */
    private PoolableObjectFactory _factory = null;

    /** Number of active objects. */
    private int _numActive = 0;
}
