/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Description: lab client
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
 * notice, this list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 * 
 * * Neither the name of the openMDX team nor the names of its
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
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
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 */

package org.openmdx.application.mof.externalizer.xmi.uml1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings({"rawtypes"})
public class UML1Association
  extends UML1GeneralizableElement {
    
  public UML1Association(
      String id,
      String name,
      String qualifiedName,
      UML1VisibilityKind visibility,
      boolean isSpecification,
      boolean isRoot,
      boolean isLeaf,
      boolean isAbstract,
      Boolean isDerived
  ) {
      super(id, name, qualifiedName, visibility, isSpecification, isRoot, isLeaf, isAbstract);
      this.isDerived = isDerived;
      connection = new ArrayList();
  }

@SuppressWarnings("unchecked")
public boolean isDerived(
  ) {
      if(this.isDerived == null) {
          // Try to derive isDerived from tagged values (e.g. required for
          // Poseidon for UML
          for(
              Iterator<UML1TaggedValue> it = this.getTaggedValues().iterator();
              it.hasNext();
          ) {
              UML1TaggedValue taggedValue = it.next();
              if("derived".equals(taggedValue.getType().getName()) && "true".equals(taggedValue.getDataValue())) {
                  return true;
              }
          }
          return false; 
      }
      else {
          return this.isDerived.booleanValue();
      }
  }
  
  public List getConnection() {
    return connection;
  }

  public String getExposedEndId(
  ) {
      return this.exposedEndId;
  }
  
  public void setExposedEndId(
      String id
  ) {
      this.exposedEndId = id;
  }
  
  public String getReferencedEndId(
  ) {
      return this.referencedEndId;
  }
  
  public void setReferencedEndId(
      String id
  ) {
      this.referencedEndId = id;
  }

  public void setNavigableOwnedEndIds(
      List<String> id
  ) {
      this.navigableOwnedEndIds = id;
  }
  
  public List<String> getNavigableOwnedEndId(
  ) {
      return this.navigableOwnedEndIds;
  }
  
  private final Boolean isDerived;
  private List connection = null;
  private String exposedEndId = null;
  private String referencedEndId = null;
  private List<String> navigableOwnedEndIds = null;
  
}
