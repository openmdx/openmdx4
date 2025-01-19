/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: XMI Parser Interface
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
package org.openmdx.application.mof.externalizer.xmi;

import java.net.URI;

import org.openmdx.application.mof.externalizer.xmi.uml1.UML1AssociationEnd;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Comment;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1Generalization;
import org.openmdx.application.mof.externalizer.xmi.uml1.UML1TagDefinition;

public interface XMIReferenceResolver {

  public void parse(
      String uri
  ) throws Exception;
    
  /**
   * Get href as URI.
   * 
   * @param href
   * @return
   */
  public URI hrefToURI(
      String href
  );
  
  /**
   * Retrieves the fully qualified name of the model element identified by a 
   * given xmiId
   * @param xmiId the xmi.id that identifies desired model element
   * @return the fully qualified name of the model element
   */
  public String lookupXMIId(
    String xmiId
  );

  /**
   * Retrieves an UMLGeneralization for a given xmiId
   * @param xmiId the xmi.id that identifies the UMLGeneralization
   * @return the UMLGeneralization for the given xmi.id
   */
  public UML1Generalization lookupGeneralization(
    String xmiId
  );
    
  /**
   * Retrieves an UMLComment for a given xmiId
   * @param xmiId the xmi.id that identifies the UMLComment
   * @return the UMLComment for the given xmi.id
   */
  public UML1Comment lookupComment(
    String xmiId
  );

  /**
   * Retrieves an UMLTagDefinition for a given xmiId
   * @param xmiId the xmi.id that identifies the UMLTagDefinition
   * @return the UMLTagDefinition for the given xmi.id
   */
  public UML1TagDefinition lookupTagDefinition(
    String xmiId
  );

  /**
   * Retrieve the association end for a given xmiId
   *  
   * @param xmiId
   * 
   * @return the association end for a given xmiId
   */
  public UML1AssociationEnd lookupAssociationEnd(
    String xmiId
  );
		  
  /**
   * Retrieves the project containing the given model package.
   * @param packageName model package name
   * @result model project name containing the given model package. null in
   *         case the model package is contained in the local model project.
   */
  public String lookupProject(
      String packageName
  );

  /**
   * Returns true if the parsing reported errors.
   */
  public boolean hasErrors();
  
}
