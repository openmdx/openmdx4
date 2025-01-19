/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: TextValue 
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
 *
 * This product includes yui, the Yahoo! UI Library
 * (License - based on BSD).
 *
 */
package org.openmdx.portal.servlet.attribute;

import java.io.Serializable;

import org.openmdx.portal.servlet.ApplicationContext;
import org.openmdx.portal.servlet.ViewPort;

/**
 * TextValue
 *
 */
public class TextValue extends AttributeValue implements Serializable {

    /**
     * Create text value.
     * 
     * @param object
     * @param fieldDef
     * @param isPassword
     * @param maxLength
     * @param app
     * @return
     */
    public static AttributeValue createTextValue(
        Object object,
        FieldDef fieldDef,
        boolean isPassword,
        int maxLength,
        ApplicationContext app    
    ) {
        // Return user defined attribute value class or TextValue as default
        String valueClassName = (String)app.getMimeTypeImpls().get(fieldDef.mimeType);
        AttributeValue attributeValue = valueClassName == null ? null : 
        	AttributeValue.createAttributeValue(
                valueClassName,
                object,
                fieldDef,
                app
        	);
        return attributeValue != null ? attributeValue : 
        	new TextValue(
                object,
                fieldDef,
                isPassword,
                maxLength,
                app
            );
    }
    
    /**
     * Constructor 
     *
     * @param object
     * @param fieldDef
     * @param isPassword
     * @param maxLength
     * @param application
     */
    protected TextValue(
        Object object,
        FieldDef fieldDef,
        boolean isPassword,
        int maxLength,
        ApplicationContext application    
    ) {
        super(
            object, 
            fieldDef,
            application
        );
        this.isPassword = isPassword;
        this.maxLength = maxLength;
    }

    /**
     * Return true if text field is a password field.
     * 
     * @return
     */
    public boolean isPassword(
    ) {
        return this.isPassword;
    }

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getDefaultValue()
     */
    @Override
    public Object getDefaultValue(
    ) {      
        return this.fieldDef.defaultValue;
    }

    /**
     * Get configured max length.
     * 
     * @return
     */
    public int getMaxLength(
    ) {      
        return this.maxLength;
    }

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getStringifiedValueInternal(org.openmdx.portal.servlet.ViewPort, java.lang.Object, boolean, boolean, boolean)
     */
    @Override
    protected String getStringifiedValueInternal(
        ViewPort p, 
        Object v,
        boolean multiLine,
        boolean forEditing,
        boolean shortFormat
    ) {
    	String raw = v.toString().trim();
        return this.app.getHtmlEncoder().encode(raw, forEditing);
    }

    //-------------------------------------------------------------------------
    // Members
    //-------------------------------------------------------------------------
    private static final long serialVersionUID = 3258688819002619449L;

    private final boolean isPassword;
    private final int maxLength;

}

//--- End of File -----------------------------------------------------------
