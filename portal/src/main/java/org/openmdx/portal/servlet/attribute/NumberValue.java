/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: NumberValue
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.jmi.reflect.RefObject;

import org.openmdx.base.accessor.jmi.cci.RefObject_1_0;
import org.openmdx.base.exception.ServiceException;
import org.openmdx.kernel.log.SysLog;
import org.openmdx.portal.servlet.ApplicationContext;
import org.openmdx.portal.servlet.Autocompleter_1_0;
import org.openmdx.portal.servlet.CssClass;
import org.openmdx.portal.servlet.HtmlEncoder_1_0;
import org.openmdx.portal.servlet.ViewPort;
import org.openmdx.portal.servlet.control.EditInspectorControl;

/**
 * NumberValue
 *
 */
public class NumberValue extends AttributeValue implements Serializable {
  
    /**
     * Create number attribute value.
     * 
     * @param object
     * @param fieldDef
     * @param hasThousandsSeparator
     * @param minValue
     * @param maxValue
     * @param application
     * @return
     */
    public static AttributeValue createNumberValue(
        Object object,
        FieldDef fieldDef,
        boolean hasThousandsSeparator,
        BigDecimal minValue,
        BigDecimal maxValue,
        ApplicationContext application
    ) {
        // Return user defined attribute value class or NumberValue as default
        String valueClassName = (String)application.getMimeTypeImpls().get(fieldDef.mimeType);
        AttributeValue attributeValue = valueClassName == null
            ? null
            : AttributeValue.createAttributeValue(
                valueClassName,
                object,
                fieldDef,
                application
              );
        return attributeValue != null
            ? attributeValue
            : new NumberValue(
                object,
                fieldDef,
                hasThousandsSeparator,
                minValue,
                maxValue,
                application
            );
    }
    
    /**
     * Constructor.
     * 
     * @param object
     * @param fieldDef
     * @param hasThousandsSeparator
     * @param minValue
     * @param maxValue
     * @param application
     */
    protected NumberValue(
        Object object,
        FieldDef fieldDef,
        boolean hasThousandsSeparator,
        BigDecimal minValue,
        BigDecimal maxValue,
        ApplicationContext application
    ) {
        super(
            object, 
            fieldDef,
            application
        );
        this.defaultValue = fieldDef.defaultValue == null
            ? null
            : new BigDecimal(fieldDef.defaultValue);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.hasThousandsSeparator = hasThousandsSeparator;
    }

    /**
     * Get locale by name.
     * 
     * @param localeAsString
     * @return
     */
    protected Locale getLocaleByName(
        String localeAsString
    ) {
        return availableLocales.get(localeAsString);
    }
    
    /**
     * Get decimal formatter.
     * 
     * @return
     */
    private DecimalFormat getDecimalFormat(
    ) {
        if(this.decimalFormat == null) {
            Locale userLocale = this.getLocaleByName(this.app.getCurrentLocaleAsString());
            this.decimalFormat = userLocale == null
                ? (DecimalFormat)DecimalFormat.getInstance()
                : (DecimalFormat)DecimalFormat.getInstance(userLocale);            
            this.decimalFormat.setMinimumFractionDigits(this.fieldDef.decimalPlaces);
            this.decimalFormat.setMaximumFractionDigits(this.fieldDef.decimalPlaces);
            this.decimalFormat.setDecimalSeparatorAlwaysShown(
                this.fieldDef.decimalPlaces > 0
            );
            this.decimalFormat.setGroupingUsed(this.hasThousandsSeparator);
            this.decimalFormat.setGroupingSize(3);
        }
        return this.decimalFormat;            
    }
  
    /**
     * Test whether s is a number.
     * 
     * @param s
     * @return
     */
    protected boolean isNumber(
    	String s
    ) {
    	try {
    		new BigDecimal(s);
    		return true;
    	} catch(Exception e) {
    		return false;
    	}
    }
    
    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getValue(boolean)
     */
    @Override
    public Object getValue(
    	ViewPort p,    		
    	boolean shortFormat
    ) {
    	Object value = super.getValue(p, shortFormat);
    	DecimalFormat formatter = this.getDecimalFormat();        
    	if(value == null) {
    		return "";
    	} else if(value instanceof Collection) {
    		List<String> values = new ArrayList<String>();
    		for(Iterator<?> i = ((Collection<?>)value).iterator(); i.hasNext(); ) {
    			Object number = i.next();
    			if(number instanceof Number) {
    				values.add(formatter.format(number));
    			} else {
    				SysLog.error(
						"Collection contains non Number values", 
						(this.object instanceof RefObject ? "object=" + ((RefObject)this.object).refMofId() + "; " : "" ) +  
						"feature=" + this.fieldDef.qualifiedFeatureName + "; values=" + value + "; element=" + number + "; element class=" + (number == null ? null : number.getClass().getName())
					);
    			}          
    		}
    		return values;
    	} else if(value instanceof Number) {
    		return formatter.format(value);
    	} else if(value instanceof String && this.isNumber((String)value)) {
    		return value;
    	} else {
    		SysLog.error(
				"Attribute value is not a Number", 
				(this.object instanceof RefObject ? "object=" + ((RefObject)this.object).refMofId() + "; " : "" ) + 
				"feature=" + this.fieldDef.qualifiedFeatureName + "; value=" + value + "; value class=" + (value == null ? null : value.getClass().getName())
			);
    		return "#ERR";
    	}
    }

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#getDefaultValue()
     */
    @Override
    public Object getDefaultValue(
    ) {      
        DecimalFormat formatter = this.getDecimalFormat();
        return this.defaultValue == null
            ? null
            : formatter.format(this.defaultValue);
    }
  
    /**
     * Get min value.
     * 
     * @return
     */
    public BigDecimal getMinValue(
    ) {      
        return this.minValue;
    }

    /**
     * Get max value.
     * 
     * @return
     */
    public BigDecimal getMaxValue(
    ) {      
        return this.maxValue;
    }

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.attribute.AttributeValue#paint(org.openmdx.portal.servlet.attribute.Attribute, org.openmdx.portal.servlet.ViewPort, java.lang.String, java.lang.String, org.openmdx.base.accessor.jmi.cci.RefObject_1_0, int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void paint(
        Attribute attribute,
        ViewPort p,
        String id,
        String label,
        RefObject_1_0 lookupObject,
        int nCols,
        int tabIndex,
        String gapModifier,
        String styleModifier,
        String widthModifier,
        String rowSpanModifier,
        String readonlyModifier,
        String lockedModifier,
        String stringifiedValue,
        boolean forEditing
    ) throws ServiceException {
        HtmlEncoder_1_0 htmlEncoder = p.getApplicationContext().getHtmlEncoder();       
        label = this.getLabel(attribute, p, label);
        String title = this.getTitle(attribute, label);
        if(forEditing && readonlyModifier.isEmpty()) {
            String feature = this.getName();
            id = (id == null) || (id.length() == 0) ? 
                feature + "[" + Integer.toString(tabIndex) + "]" : 
                id;            
            p.write("<td class=\"", CssClass.fieldLabel.toString(), "\" title=\"", (title == null ? "" : htmlEncoder.encode(title, false)), "\"><span class=\"", CssClass.nw.toString(), "\">", htmlEncoder.encode(label, false), "</span></td>");            
            if(this.isSingleValued()) {
                p.write("<td ", rowSpanModifier, ">");
                Autocompleter_1_0 autocompleter = this.getAutocompleter(
                    lookupObject
                );
                // Predefined, selectable values only allowed for single-valued attributes with spanRow == 1
                // Show drop-down instead of input field
                if(autocompleter != null && readonlyModifier.isEmpty()) {
                    autocompleter.paint(
                        p,
                        id,
                        tabIndex,
                        feature,
                        null,
                        false,
                        null,
                        null,
                        "class=\"" + CssClass.valueL + " " + CssClass.valueAC + "\"",
                        null, // imgTag
                        null // onChangeValueScript
                    );
                } else {
                    BigDecimal minValue = this.getMinValue();
                    String minValueModifier = minValue.compareTo(new BigDecimal(Long.MIN_VALUE)) <= 0 ? 
                        "" : 
                        "if (parseInt(removeThousandsSeparator(this.value))<" + Long.toString(minValue.longValue()) + ") {this.value=" + Long.toString(minValue.longValue()) + ";};";
                    BigDecimal maxValue = this.getMaxValue();
                    String maxValueModifier = maxValue.compareTo(new BigDecimal(Long.MAX_VALUE)) >= 0 ? 
                        "" : 
                        "if (parseInt(removeThousandsSeparator(this.value))>" + maxValue + ") {this.value=" + maxValue + ";};";
                    String classModifier = this.isMandatory() 
                    	? CssClass.valueR + " " + CssClass.mandatory 
                    	: CssClass.valueR.toString();
                    p.debug("  <!-- " + minValue + " | " + maxValue + " | " + new BigDecimal(Long.MIN_VALUE) + " | " + new BigDecimal(Long.MAX_VALUE) + " | -->");
                    p.write("  <input id=\"", id, "\" name=\"", id, "\" type=\"text\" class=\"", classModifier, lockedModifier, "\" ", readonlyModifier, " tabindex=\"" + tabIndex, "\" value=\"", stringifiedValue, "\" onkeypress=\"javascript: var kc = null; if (window.event) {kc = window.event.keyCode;} else {kc = event.which;}; if (!(((kc>=37) && (kc<=40)) || ((kc>=44) && (kc<=46)) || ((kc>=48) && (kc<=57)) || (kc==0) || (kc==8) || (kc==9) || (kc==13))) {if (window.event) {window.event.returnValue=false;} else {event.preventDefault();}}\" onchange=\"javascript: ", minValueModifier, " ", maxValueModifier, ";\"");
                    p.writeEventHandlers("    ", attribute.getEventHandler());
                    p.write("  >");
                }
                p.write("</td>");
                p.write("<td class=\"", CssClass.addon.toString(), "\" ", rowSpanModifier, "></td>");
            } else {
                p.write("<td ", rowSpanModifier, ">");
                p.write("  <textarea id=\"", id, "\" name=\"", id, "\" class=\"", CssClass.multiStringLocked.toString(), "\" rows=\"" + attribute.getSpanRow(), "\" cols=\"20\" readonly tabindex=\"" + tabIndex, "\">", stringifiedValue, "</textarea>");
                p.write("</td>");
                p.write("<td class=\"", CssClass.addon.toString(), "\" ", rowSpanModifier, ">");
                if(readonlyModifier.isEmpty()) {
    				p.write("<a role=\"button\" data-toggle=\"modal\" href=\"#popup_", EditInspectorControl.EDIT_NUMBERS, "\" onclick=\"javascript:multiValuedHigh=", this.getUpperBound("1..10"), "; ", EditInspectorControl.EDIT_NUMBERS, "_showPopup(event, this.id, popup_", EditInspectorControl.EDIT_NUMBERS, ", 'popup_", EditInspectorControl.EDIT_NUMBERS, "', $('", id, "'), new Array());\">");
    				p.write("    ", p.getImg("class=\"", CssClass.popUpButton.toString(), "\" id=\"", id, ".popup\" border=\"0\" alt=\"Click to edit\" src=\"", p.getResourcePath("images/edit"), p.getImgType(), "\" "));
    				p.write("</a>");
                }
                p.write("</td>");
            }
        } else {
            super.paint(
                attribute,
                p,
                id,
                label,
                lookupObject,
                nCols,
                tabIndex,
                gapModifier,
                styleModifier,
                widthModifier,
                rowSpanModifier,
                readonlyModifier,
                lockedModifier,
                stringifiedValue,
                forEditing
            );
        }
    }

    //-------------------------------------------------------------------------
    // Members
    //-------------------------------------------------------------------------
    private static final long serialVersionUID = 3256439222591238964L;
    
    private static Map<String,Locale> availableLocales = new HashMap<String,Locale>();

    static {        
        Locale[] locales = DecimalFormat.getAvailableLocales();
        for(int i = 0; i < locales.length; i++) {
            NumberValue.availableLocales.put(
                locales[i].toString(),
                locales[i]
            );
        }                    
    }
        
    private final BigDecimal defaultValue;
    private final BigDecimal minValue;
    private final BigDecimal maxValue;
    private final boolean hasThousandsSeparator;
    private transient DecimalFormat decimalFormat = null;
  
}

//--- End of File -----------------------------------------------------------
