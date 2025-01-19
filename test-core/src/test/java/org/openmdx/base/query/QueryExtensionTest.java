/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Query Extension Test
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
package org.openmdx.base.query;

import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_BOOLEAN_PARAM;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_CLASS;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_CLAUSE;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_DATETIME_PARAM;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_DATE_PARAM;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_DECIMAL_PARAM;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_INTEGER_PARAM;
import static org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_Attributes.QUERY_EXTENSION_STRING_PARAM;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jmi.reflect.RefPackage;
import javax.naming.NamingException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openmdx.application.dataprovider.cci.FilterProperty;
import org.openmdx.base.accessor.cci.SystemAttributes;
import org.openmdx.base.accessor.jmi.cci.RefQuery_1_0;
import org.openmdx.base.jmi1.BasePackage;
import org.openmdx.base.jmi1.Provider;
import org.openmdx.base.naming.Path;
import org.openmdx.base.persistence.cci.PersistenceHelper;
import org.openmdx.base.rest.cci.QueryExtensionRecord;
import org.openmdx.base.rest.cci.QueryFilterRecord;
import org.openmdx.junit5.OpenmdxTestCoreStandardExtension;

import test.openmdx.clock1.cci2.SegmentQuery;
import test.openmdx.clock1.jmi1.Clock1Package;

/**
 * Query Extension Test
 */
@ExtendWith(OpenmdxTestCoreStandardExtension.class)
public class QueryExtensionTest {

    static final String[] expectedAttributes = new String[]{
        QUERY_EXTENSION_DATE_PARAM,
        QUERY_EXTENSION_DECIMAL_PARAM,
        QUERY_EXTENSION_CLAUSE,
        QUERY_EXTENSION_BOOLEAN_PARAM,
        QUERY_EXTENSION_INTEGER_PARAM,
        QUERY_EXTENSION_DATETIME_PARAM,
        QUERY_EXTENSION_STRING_PARAM
    };

    static final List<?>[] expectedValues = {
        Collections.EMPTY_LIST,
        Collections.EMPTY_LIST,
        Collections.singletonList("SELECT something FROM somewhere WHERE b0 = ? AND i0 = ? AND i1 = ? AND i2 = ?"),
        Collections.singletonList(Boolean.TRUE),
        Arrays.asList(new Integer[]{Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)}),
        Collections.EMPTY_LIST,
        Collections.EMPTY_LIST
    };

    private static PersistenceManagerFactory entityManagerFactory;
    private Provider provider;
    private Clock1Package clock1;

    static private final String PROVIDER_PATH = "xri://@openmdx*test.openmdx.clock1/provider/Java";

    @BeforeEach
    public void setUp(
    ) throws Exception {
        PersistenceManager entityManager = entityManagerFactory.getPersistenceManager();
        this.provider = entityManager.getObjectById(
            Provider.class,
            PROVIDER_PATH
        );
        RefPackage rootPkg = this.provider.refOutermostPackage();
        this.clock1 = (Clock1Package)rootPkg.refPackage(
            "test:openmdx:clock1"
        );
        BasePackage base = (BasePackage) rootPkg.refPackage(
            "org:openmdx:base"
        );
        this.provider = base.getProvider().getProvider(
            new Path(PROVIDER_PATH)
        );
    }

    @Test
    public void testQueryFilter(
    ) throws Exception {
        SegmentQuery query = this.clock1.createSegmentQuery();
        QueryExtensionRecord extension = PersistenceHelper.newQueryExtension(query);
        extension.setClause(
            "SELECT something FROM somewhere WHERE b0 = ? AND i0 = ? AND i1 = ? AND i2 = ?"
        );
        extension.setBooleanParam(
            new boolean[]{true}
        );
        extension.setIntegerParam(
            new int[]{1, 2, 3}
        );
        Assertions.assertTrue(query instanceof RefQuery_1_0, "Query instance of RefFilter_1_0");
        QueryFilterRecord filter = ((RefQuery_1_0)query).refGetFilter();
        System.out.println(filter);
        Assertions.assertEquals(1,  filter.getCondition().size(), "Filter condition count");
        Assertions.assertEquals(new IsInstanceOfCondition(
		    "test:openmdx:clock1:Segment"
		),  filter.getCondition().get(0), "Instance of");
        List<FilterProperty> filterProperties = FilterProperty.getFilterProperties(filter);
        Assertions.assertEquals(2 + expectedAttributes.length,  filterProperties.size(), "Filter property count");
        FilterProperty firstCondition = filterProperties.get(1);
        String firstFeature = firstCondition.name();
        String namespace = firstFeature.substring(
            0, 
            firstFeature.lastIndexOf(':') + 1
        );
        Assertions.assertEquals(new FilterProperty(
		    Quantifier.codeOf(null),
		    namespace + SystemAttributes.OBJECT_CLASS,
		    ConditionType.codeOf(null),
		    QUERY_EXTENSION_CLASS
		),  filterProperties.get(1), "Context object class");
        int processedAttributes = 0;
        for(int i = 2; i < filterProperties.size(); i++) {
            FilterProperty p = filterProperties.get(i);
            Assertions.assertNull(Quantifier.valueOf(p.quantor()), "Piggy back quantifier");
            Expected: for(
                int j = 0;
                j < expectedAttributes.length;
                j++
            ){
                if(p.name().equals(namespace + expectedAttributes[j])) {
                    Assertions.assertEquals(expectedValues[j],  p.values(), expectedAttributes[j]);
                    processedAttributes++;
                    continue Expected;
                }
            }
        }
        Assertions.assertEquals(expectedAttributes.length,  processedAttributes, "Expected attributes");
    }

    @BeforeAll
    public static void deploy() throws NamingException{
        entityManagerFactory = JDOHelper.getPersistenceManagerFactory("test-Clock-EntityManagerFactory");
    }

    @AfterAll
    public static void close(
    ) throws IOException{
        entityManagerFactory.close();
    }

}
