/*
 * ====================================================================
 * Description: Abstract PersistenceManager
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
 * This product includes or is based on software developed by other 
 * organizations as listed in the NOTICE file.
 */
package org.openmdx.base.persistence.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.openmdx.base.collection.Sets;
import org.openmdx.base.persistence.cci.ConfigurableProperty;

/**
 * Abstract PersistenceManager
 *
 * @since openMDX 2.0
 */
@SuppressWarnings({ "rawtypes" })
public class PersistenceManagers {

    /**
     * Constructor
     */
    private PersistenceManagers() {
        // avoid instantiation
    }

    
    /**
     * The following optional features are supported<ul>
     * <li>javax.jdo.option.TransientTransactional
     * <li>javax.jdo.option.NontransactionalRead
     * <li>javax.jdo.option.NontransactionalWrite
     * <li>javax.jdo.option.RetainValues
     * <li>javax.jdo.option.Optimistic
     * <li>javax.jdo.option.ApplicationIdentity
     * <li>javax.jdo.option.ChangeApplicationIdentity
     * <li>javax.jdo.option.List
     * <li>javax.jdo.option.GetDataStoreConnection
     * </ul>
     * <p>
     * The following optional features are <b>not</b> supported<ul>
     * <li>javax.jdo.option.ArrayList
     * <li>javax.jdo.option.HashMap
     * <li>javax.jdo.option.Hashtable
     * <li>javax.jdo.option.LinkedList
     * <li>javax.jdo.option.TreeMap
     * <li>javax.jdo.option.TreeSet
     * <li>javax.jdo.option.Vector
     * <li>javax.jdo.option.Array
     * <li>javax.jdo.option.NullCollection
     * <li>javax.jdo.option.DatastoreIdentity
     * <li>javax.jdo.option.NonDurableIdentity
     * <li>javax.jdo.option.BinaryCompatibility
     * <li>javax.jdo.option.UnconstrainedQueryVariables
     * </ul>
     */
    private final static Set<String> SUPPORTED_OPTIONS = Collections.unmodifiableSet(
        Sets.asSet(
            ConfigurableProperty.TransientTransactional.qualifiedName(), // "javax.jdo.option.TransientTransactional"
            ConfigurableProperty.NontransactionalRead.qualifiedName(), // "javax.jdo.option.NontransactionalRead"
            ConfigurableProperty.RetainValues.qualifiedName(), // "javax.jdo.option.RetainValues"
            ConfigurableProperty.Optimistic.qualifiedName(), // "javax.jdo.option.Optimistic"
            ConfigurableProperty.ApplicationIdentity.qualifiedName(), // "javax.jdo.option.ApplicationIdentity"
            ConfigurableProperty.GetDataStoreConnection.qualifiedName(), // "javax.jdo.option.GetDataStoreConnection"
            "javax.jdo.option.List" // mandatory since JDO 2
        )
    );

    /**
     * Evict All
     * 
     * @param pm
     * @param pcs
     */
    public static void evictAll(
        PersistenceManager pm,
        Collection<?> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.evict(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Eviction failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Refresh All
     * 
     * @param pm
     * @param pcs
     */
    public static void refreshAll(
        PersistenceManager pm,
        Collection<?> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.refresh(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Refresh failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Delete All
     * 
     * @param pm
     * @param pcs
     */
    public static void deletePersistentAll(
        PersistenceManager pm,
        Object... pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.deletePersistent(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Delete persistent failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Refresh All
     * 
     * @param pm
     * @param pcs
     */
    public static void refreshAll(
        PersistenceManager pm,
        Object... pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.refresh(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Refresh failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Refresh All
     * 
     * @param pm
     * @param jdoe
     */
    public static void refreshAll(
        PersistenceManager pm,
        JDOException jdoe
    ) {
        Throwable[] throwables = jdoe.getNestedExceptions();
        if (throwables != null) {
            List<Object> objects = new ArrayList<Object>();
            for (Throwable throwable : throwables) {
                if (throwable instanceof JDOException) {
                    Object object = ((JDOException) throwable).getFailedObject();
                    if (object != null)
                        objects.add(object);
                }
            }
            refreshAll(pm, objects);
        }
    }

    /**
     * Make Persistent All
     */
    public static <T> T[] makePersistentAll(
        PersistenceManager pm,
        T[] pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.makePersistent(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions == null) {
            return pcs;
        } else {
            throw new JDOException(
                "Make persistent failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
        }
    }

    /**
     * Make Persistent All
     * 
     * @param pm
     * @param pcs
     * 
     * @return
     */
    public static <T> Collection<T> makePersistentAll(
        PersistenceManager pm,
        Collection<T> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.makePersistent(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions == null) {
            return pcs;
        } else {
            throw new JDOException(
                "Make persistent failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
        }
    }

    /**
     * Delete Persistent All
     * 
     * @param pm
     * @param pcs
     */
    public static void deletePersistentAll(
        PersistenceManager pm,
        Collection<?> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.deletePersistent(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Delete persistent failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    public static void makeTransientAll(
        PersistenceManager pm,
        Collection<?> pcs,
        boolean useFetchPlan
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.makeTransient(pc, useFetchPlan);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Make transient failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Make Transient All
     * 
     * @param pm
     * @param useFetchPlan
     * @param pcs
     */
    public static void makeTransientAll(
        PersistenceManager pm,
        boolean useFetchPlan,
        Object... pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.makeTransient(pc, useFetchPlan);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Make transient failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Make Transactional All
     * 
     * @param pm
     * @param pcs
     */
    public static void makeTransactionalAll(
        PersistenceManager pm,
        Collection<?> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.makeTransactional(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Make transactional failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Make Non-Transactional All
     * 
     * @param pm
     * @param pcs
     */
    public static void makeNontransactionalAll(
        PersistenceManager pm,
        Collection<?> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.makeNontransactional(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Make non-transactional failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Retrieve All
     * 
     * @param pm
     * @param useFetchPlan
     * @param pcs
     */
    public static void retrieveAll(
        PersistenceManager pm,
        boolean useFetchPlan,
        Collection<?> pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.retrieve(pc, useFetchPlan);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Retrieve failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Retrieve All
     * 
     * @param pm
     * @param useFetchPlan
     * @param pcs
     */
    public static void retrieveAll(
        PersistenceManager pm,
        boolean useFetchPlan,
        Object... pcs
    ) {
        List<JDOException> exceptions = null;
        for (Object pc : pcs)
            try {
                pm.retrieve(pc, useFetchPlan);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions != null)
            throw new JDOException(
                "Retrieve failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Detach Copy All
     * 
     * @param pm
     * @param pcs
     * 
     * @return
     */
    public static <T> Collection<T> detachCopyAll(
        PersistenceManager pm,
        Collection<T> pcs
    ) {
        List<JDOException> exceptions = null;
        List<T> objects = new ArrayList<T>(pcs.size());
        for (T pc : pcs)
            try {
                objects.add(pm.detachCopy(pc));
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions == null) {
            return objects;
        } else
            throw new JDOException(
                "Detach copy failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.jdo.PersistenceManager#detachCopyAll(java.lang.Object[])
     */
    public static <T> T[] detachCopyAll(
        PersistenceManager pm,
        T[] pcs
    ) {
        List<JDOException> exceptions = null;
        T[] objects = pcs.clone();
        int i = 0;
        for (T pc : pcs)
            try {
                objects[i++] = pm.detachCopy(pc);
            } catch (JDOException exception) {
                (exceptions == null ? exceptions = new ArrayList<JDOException>() : exceptions).add(exception);
            }
        if (exceptions == null) {
            return objects;
        } else
            throw new JDOException(
                "Detach copy failure",
                exceptions.toArray(new JDOException[exceptions.size()])
            );
    }

    /**
     * Get Objects by Id
     * 
     * @param pm
     * @param oids
     * @param validate
     * 
     * @return
     */
    public static Collection getObjectsById(
        PersistenceManager pm,
        boolean validate,
        Collection<?> oids
    ) {
        Collection<Object> objects = new ArrayList<Object>(oids.size());
        for (Object oid : oids) {
            objects.add(pm.getObjectById(oid, validate));
        }
        return objects;
    }

    /**
     * Get Objects by Id
     * 
     * @param pm
     * @param oids
     * @param validate
     * 
     * @return
     */
    public static Object[] getObjectsById(
        PersistenceManager pm,
        boolean validate,
        Object... oids
    ) {
        Object[] objects = new Object[oids.length];
        for (int i = 0; i < oids.length; i++) {
            objects[i] = pm.getObjectById(oids[i], validate);
        }
        return objects;
    }

    /**
     * Convert the user name representing a principal chain into a principal chain
     * 
     * @param a
     *            user name representing a principal chain
     * 
     * @return the principal chain represented by the user name
     */
    public static List<String> toPrincipalChain(
        String username
    ) {
        if (username == null || username.length() == 0) {
            return Collections.emptyList();
        } else if ((username.startsWith("[") && username.endsWith("]")) ||
            (username.startsWith("{") && username.endsWith("}"))) {
            List<String> principalChain = new ArrayList<String>();
            for (String principal : username.substring(1, username.length() - 1).split(",")) {
                principal = principal.trim();
                if (!"".equals(principal)) {
                    principalChain.add(principal);
                }
            }
            return principalChain;
        } else {
            return Collections.singletonList(username);
        }
    }

    public static Map<String, Object> getProperties(
        PersistenceManager persistenceManager
    ) {
        final Map<String, Object> properties = new HashMap<>();
        put(properties, ConfigurableProperty.CopyOnAttach, persistenceManager.getCopyOnAttach());
        put(properties, ConfigurableProperty.DatastoreReadTimeoutMillis, persistenceManager.getDatastoreReadTimeoutMillis());
        put(properties, ConfigurableProperty.DatastoreWriteTimeoutMillis, persistenceManager.getDatastoreWriteTimeoutMillis());
        put(properties, ConfigurableProperty.DetachAllOnCommit, persistenceManager.getDetachAllOnCommit());
        put(properties, ConfigurableProperty.IgnoreCache, persistenceManager.getIgnoreCache());
        put(properties, ConfigurableProperty.Multithreaded, persistenceManager.getMultithreaded());
        final PersistenceManagerFactory persistenceManagerFactory = persistenceManager.getPersistenceManagerFactory();
        put(properties, ConfigurableProperty.NontransactionalRead, persistenceManagerFactory.getNontransactionalRead());
        put(properties, ConfigurableProperty.NontransactionalWrite, persistenceManagerFactory.getNontransactionalWrite());
        put(properties, ConfigurableProperty.Optimistic, persistenceManagerFactory.getOptimistic());
        put(properties, ConfigurableProperty.ReadOnly, persistenceManagerFactory.getReadOnly());
        put(properties, ConfigurableProperty.RestoreValues, persistenceManagerFactory.getRestoreValues());
        put(properties, ConfigurableProperty.RetainValues, persistenceManagerFactory.getRetainValues());
        return properties;
    }

    private static void put(
        Map<String, Object> target,
        ConfigurableProperty key,
        boolean value
    ) {
        target.put(key.qualifiedName(), String.valueOf(value));
    }
    
    private static void put(
        Map<String, Object> target,
        ConfigurableProperty key,
        Object value
    ) {
        target.put(key.qualifiedName(), value == null ? null : value.toString());
    }
    
    public static void setProperty(
        PersistenceManager persistenceManager,
        String propertyName,
        Object value
    ) {
        final Optional<ConfigurableProperty> property = ConfigurableProperty.fromQualifiedName(propertyName);
        if(property.isPresent()) {
            switch(property.get()) {
                case CopyOnAttach: 
                    persistenceManager.setCopyOnAttach(toFlag(value));
                    break;
                case DatastoreReadTimeoutMillis:
                    persistenceManager.setDatastoreReadTimeoutMillis(toMilliseconds(value));
                    break;
                case DatastoreWriteTimeoutMillis:
                    persistenceManager.setDatastoreWriteTimeoutMillis(toMilliseconds(value));
                    break;
                case DetachAllOnCommit:
                    persistenceManager.setDetachAllOnCommit(toFlag(value));
                    break;
                case IgnoreCache:
                    persistenceManager.setIgnoreCache(toFlag(value));
                    break;
                case Multithreaded:
                    persistenceManager.setMultithreaded(toFlag(value));
                    break;
                default:
                    throw new JDOUserException ("The property " + property.get().qualifiedName()
                        + "can be set at factory level only");
            }
        }
    }


    private static boolean toFlag(Object value) {
        return value instanceof Boolean ? 
            ((Boolean)value).booleanValue() :
            Boolean.parseBoolean((String)value);
    }
    
    private static Integer toMilliseconds(Object value) {
        return 
            value == null ? null :
            value instanceof Integer ? (Integer)value :
            Integer.valueOf((String)value);
    }
    
    /**
     * Retrieve the supported properties.
     *
     * @return Returns the supported properties.
     */
    public static Set<String> getSupportedProperties() {
        return SUPPORTED_OPTIONS;
    }
    
}
