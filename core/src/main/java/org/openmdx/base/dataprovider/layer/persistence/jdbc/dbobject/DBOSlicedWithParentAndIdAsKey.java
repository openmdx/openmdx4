/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: DBOSlicedWithParentAndIdAsKey 
 * Owner:       the original authors.
 * ====================================================================
 *
 * This software is published under the BSD license
 * as listed below.
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

package org.openmdx.base.dataprovider.layer.persistence.jdbc.dbobject;

import java.sql.Connection;
import java.sql.SQLException;

import org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.Database_1_0;
import org.openmdx.base.dataprovider.layer.persistence.jdbc.spi.FastResultSet;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;

/**
 * DBOSlicedWithParentAndIdAsKey
 *
 */
public class DBOSlicedWithParentAndIdAsKey extends DBOSlicedWithIdAsKey
{

    //-------------------------------------------------------------------------
    public DBOSlicedWithParentAndIdAsKey(
        Database_1_0 database,
        Connection conn, 
        DbObjectConfiguration typeConfiguration
    ) {
        super(
            database, 
            conn, 
            typeConfiguration
        );
    }
    
    //-------------------------------------------------------------------------
    public DBOSlicedWithParentAndIdAsKey(
        Database_1_0 database,
        Connection conn,
        DbObjectConfiguration typeConfiguration,
        Path accessPath, 
        boolean isExtent, 
        boolean isQuery
    ) throws ServiceException {
        super(
            database, 
            conn, 
            typeConfiguration, 
            accessPath, 
            isExtent,
            isQuery
        );
        this.objectIdValues.clear();
        this.objectIdValues.add(
            this.database.getObjectId(
                this.getObjectId()
            )
        );
        this.objectIdClause = "(v." + database.getObjectOidColumnName() + " = ?)";
        this.objectIdColumn.clear();
        this.objectIdColumn.add(database.getObjectOidColumnName());
        this.getReferenceValues().clear();
        this.getReferenceValues().add(
            this.database.getObjectId(
                conn,
                this.getReference().getParent()
            )
        );
        if(this.getJoinCriteria() == null) {
            this.referenceClause = 
                "(v." + 
                this.database.getColumnName(
                    conn, 
                    "parent", 
                    0, 
                    false, // indexSuffixIfZero
                    false, // ignoreReservedWords
                    true // markAsPrivate
                ) + 
                " = ?)";            
        } else {
            this.referenceClause = "(vj." + this.getJoinCriteria()[1] + " = ?)";            
        }
        this.getReferenceColumn().clear();
        // non index column for non-indexed sliced DB objects
        this.indexColumn = null;
        this.excludeAttributes.add("objectIdx");        
    }
    
    //-------------------------------------------------------------------------    
    @Override
    public Path getObjectReference(
        FastResultSet frs
    ) throws SQLException, ServiceException {
        String parentId = (String)frs.getObject(this.database.getPrivateAttributesPrefix() + "parent");
        if(parentId == null) {
            throw new SQLException(
                "column p$$parent in result set not found"
            );
        } 
        String typeName = this.dbObjectConfiguration.getTypeName();
        String parentQualifiers = parentId.substring(parentId.indexOf("/"));
        return this.database.getReference(
            this.conn,
            typeName + parentQualifiers
        );
    }

    //---------------------------------------------------------------------------  
    @Override
    public String getObjectId(
        FastResultSet frs
    ) throws SQLException {
      String objectId = (String)frs.getObject("object_id");
      if(objectId == null) {
          throw new SQLException(
              "column object_id in result set not found"
          );
      }
      return objectId;
    }
              
    //-------------------------------------------------------------------------
    // Members
    //-------------------------------------------------------------------------    
    private static final long serialVersionUID = -2584872546809431085L;

}
