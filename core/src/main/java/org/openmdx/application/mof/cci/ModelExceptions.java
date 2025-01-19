/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Generated constants for ModelExceptions
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
package org.openmdx.application.mof.cci;

import org.openmdx.kernel.exception.BasicException;

/**
 * The {@code ModelExceptions} class contains exceptions codes used for
 * importing or exporting MOF models.
 */
public class ModelExceptions extends BasicException.Code {

    /**
     * Constructor 
     */
    protected ModelExceptions(){
        // avoid instantiation
    }

    /**
     * The model domain id
     */
    static public final String MODEL_DOMAIN = "ModelDomain";

    /**
     * Invalid format was used to specify a multiplicity
     */
    static public final int INVALID_MULTIPLICITY_FORMAT = 1;

    /**
     * The opposite end of a composite aggregation must have the property 
     * 'aggregation' set to org.omg.model.model1.generic.AggregationKind.NONE
     */
    static public final int INVALID_OPPOSITE_AGGREGATION_FOR_COMPOSITE_AGGREGATION = 3;

    /**
     * The opposite end of a composite aggregation must have the property 
     * 'multiplicity' set to '1..1' or '0..1'
     */
    static public final int INVALID_MULTIPLICITY_FOR_COMPOSITE_AGGREGATION = 4;

    /**
     * Association ends with aggregation 'composite' should be navigable 
     */
    static public final int COMPOSITE_AGGREGATION_NOT_NAVIGABLE = 6;

    /**
     * Only navigable association ends need a unique identifying qualifier 
     */
    static public final int UNNECESSARY_QUALIFIER_FOUND = 7;

    /**
     * At least one model constraint is violated.
     */
    static public final int CONSTRAINT_VIOLATION = 101;

    /**
     * The repository does not contain an element that is required, i.e.
     * the repository is not complete.
     */
    static public final int REFERENCED_ELEMENT_TYPE_NOT_FOUND_IN_REPOSITORY = 102;

    /**
     * The repository does not contain an exception type that is thrown by an
     * operation.
     */
    static public final int EXCEPTION_TYPE_NOT_FOUND_IN_REPOSITORY = 103;

    /**
     * The desired package to externalize does not exist in the model repository.
     */
    static public final int PACKAGE_TO_EXTERNALIZE_DOES_NOT_EXIST = 111;

    /**
     * An alias type must specify exactly one attribute.
     */
    static public final int ALIAS_TYPE_REQUIRES_EXACTLY_ONE_ATTRIBUTE = 121;

    /**
     * An alias type must specify exactly one attribute.
     */
    static public final int INVALID_ALIAS_ATTRIBUTE_NAME = 122;

    /**
     * The name of an association cannot be empty.
     */
    static public final int ASSOCIATION_NAME_IS_EMPTY = 123;

    /**
     * The name of an association end cannot be empty.
     */
    static public final int ASSOCIATION_END_NAME_IS_EMPTY = 124;

    /**
     * Unexpected end of qualifier declaration.
     */
    static public final int UNEXPECTED_END_OF_QUALIFIER_DECLARATION = 125;

    /**
     * A colon is missing in the qualifier declaration.
     */
    static public final int MISSING_COLON_IN_QUALIFIER_DECLARATION = 126;


    /**
     * A semicolon is missing in the qualifier declaration.
     */
    static public final int MISSING_SEMICOLON_IN_QUALIFIER_DECLARATION = 127;

    /**
     * The parameter declaration is invalid.
     */
    static public final int INVALID_PARAMETER_DECLARATION = 128;

    /**
     * No type was specified for an attribute.
     */
    static public final int NO_ATTRIBUTE_TYPE_SPECIFIED = 129;

    /**
     * No type was specified for an attribute.
     */
    static public final int INVALID_ATTRIBUTE_TYPE = 130;

    /**
     * The model element is toplevel and not in a package.
     */
    static public final int MODEL_ELEMENT_NOT_IN_PACKAGE = 131;

    /**
     * Circular alias type definition found.
     */
    static public final int CIRCULAR_ALIAS_TYPE_DEFINITION = 132;

}
