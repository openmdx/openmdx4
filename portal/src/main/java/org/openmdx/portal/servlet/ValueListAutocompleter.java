/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: ValueListAutocompleter 
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
package org.openmdx.portal.servlet;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.kernel.log.SysLog;
import org.openmdx.portal.servlet.attribute.AttributeValue;

/**
 * ValueListAutocompleter
 *
 */
public class ValueListAutocompleter implements Autocompleter_1_0, Serializable {
  
    /**
     * Constructor 
     *
     * @param options
     */
    public ValueListAutocompleter(
        List<? extends Object> options
    ) {
        this.options = options;
    }
    
    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.Autocompleter_1_0#paint(org.openmdx.portal.servlet.ViewPort, java.lang.String, int, java.lang.String, org.openmdx.portal.servlet.attribute.AttributeValue, boolean, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence, java.lang.CharSequence)
     */
    @Override
    public void paint(
        ViewPort p,
        String id,
        int tabIndex,
        String fieldName,
        AttributeValue currentValue,
        boolean numericCompare,
        CharSequence tdTag,
        CharSequence inputFieldDivClass,
        CharSequence inputFieldClass,
        CharSequence imgTag,
        CharSequence onChangeValueScript
    ) throws ServiceException {
    	SysLog.detail("> paint");        
        ApplicationContext app = p.getApplicationContext();
        HtmlEncoder_1_0 htmlEncoder = app.getHtmlEncoder();
        id = (id == null) || id.isEmpty()
            ? fieldName + "[" + tabIndex + "]"
            : id;
        p.write("<select id=\"", id, "\" ", (inputFieldClass == null ? "" : inputFieldClass), " name=\"", id, "\" tabindex=\"", Integer.toString(tabIndex), "\">");
        for(Object option: this.options) {
            String selectedModifier = "";
            if(currentValue != null) {
                if(numericCompare) {
                    BigDecimal d1 = app.parseNumber((String)option);
                    if(d1 == null) {
                    	SysLog.warning("Option for numeric field is not a number", Arrays.asList(new Object[]{fieldName, option, this.options}));
                    }
                    BigDecimal d2 = app.parseNumber((String)currentValue.getValue(p, false));
                    if(d2 == null) {
                    	SysLog.warning("Numeric attribute value can not be parsed as number", Arrays.asList(new Object[]{fieldName, option}));                        
                    }
                    selectedModifier = (d1 != null) && (d2 != null)  
                        ? d1.compareTo(d2) == 0 ? "selected" : ""
                        : option.equals(currentValue.getValue(p, false)) ? "selected" : "";                                                        
                } else {
                	Object optionValue = null;
                	if(option instanceof String[]) {
                		optionValue = ((String[])option)[0];
                	} else {
                		optionValue = option;
                	}
                    selectedModifier = optionValue.equals(currentValue.getValue(p, false)) ? "selected" : "";                    
                }
            }
            if(option instanceof ObjectReference) {
                ObjectReference r = (ObjectReference)option;
                p.write("  <option ", selectedModifier, " value=\"", r.getXRI(), "\">", r.getTitle());
            } else if(option instanceof String[]) {
            	String[] optionValueLabel = (String[])option;
                String valueEncoded = htmlEncoder.encode("" + optionValueLabel[0], false);
                String labelEncoded = htmlEncoder.encode("" + (optionValueLabel.length == 2 ? optionValueLabel[1] : optionValueLabel[0]), false);
                p.write("  <option ", selectedModifier, " value=\"", valueEncoded, "\">", (labelEncoded == null || labelEncoded.isEmpty() ? valueEncoded : valueEncoded + " - " + labelEncoded));                
            } else {
                String optionEncoded = htmlEncoder.encode("" + option, false);
                p.write("  <option ", selectedModifier, " value=\"", optionEncoded, "\">", optionEncoded);                
            }
        }
        p.write("</select>");
    }

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.Autocompleter_1_0#hasFixedSelectableValues()
     */
    @Override
    public boolean hasFixedSelectableValues(
    ) {
        return true;
    }

    //-----------------------------------------------------------------------
    // Members
    //-----------------------------------------------------------------------
    private static final long serialVersionUID = -1138020420475572050L;
    
    private final List<? extends Object> options;
    
}
