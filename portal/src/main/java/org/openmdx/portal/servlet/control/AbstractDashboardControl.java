/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: AbstractDashboardControl 
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
package org.openmdx.portal.servlet.control;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.naming.Path;
import org.openmdx.portal.servlet.Action;
import org.openmdx.portal.servlet.ApplicationContext;
import org.openmdx.portal.servlet.CssClass;
import org.openmdx.portal.servlet.HtmlEncoder_1_0;
import org.openmdx.portal.servlet.ViewPort;
import org.openmdx.portal.servlet.WebKeys;
import org.openmdx.portal.servlet.component.ShowObjectView;
import org.openmdx.portal.servlet.component.View;

public abstract class AbstractDashboardControl extends Control implements Serializable {
	
	/**
	 * Constructor.
	 * 
	 * @param id
	 * @param locale
	 * @param localeAsIndex
	 */
	public AbstractDashboardControl(
        String id,
        String locale,
        int localeAsIndex
	) {
		super(
			id,
			locale,
			localeAsIndex
		);
	}

	/**
	 * DashletDescr
	 *
	 */
	public static class DashletDescr {

		public DashletDescr(
			String id,
			String name,
			String width,
			String label,
			String orderX,
			String orderY
		) {
			this.id = id;
			this.name = name;
			this.width = width;
			this.label = label;
			this.orderX = orderX;
			this.orderY = orderY;			
		}

		public final String id;
		public final String name;
		public final String width;
		public final String label;
		public final String orderX;
		public final String orderY;
		
	}
	
	/**
	 * Get id suffix.
	 * 
	 * @param p
	 * @return
	 */
	protected abstract String getDashboardIdSuffix(
		ViewPort p
	);
	
	/**
	 * Get style.
	 * 
	 * @return
	 */
	protected abstract String getDashboardStyle(
	);
	
	/**
	 * Get dashboard.
	 * 
	 * @param p
	 * @param dashboardId
	 * @param settings
	 * @param dashletFilter
	 * @param dashboard
	 * @param forEditing
	 */
	protected void getDashboard(
		ViewPort p,
		String dashboardId,
		Properties settings,
		String dashletFilter,
		Map<String,Map<String,DashletDescr>> dashboard,
		boolean forEditing
	) {
		if(settings.getProperty(dashboardId + "." + DASHBOARD_PROPERTY_DASHLETS) != null) {
			String[] dashlets = settings.getProperty(dashboardId + "." + "dashlets").split(";");
			for(String dashlet: dashlets) {
				String orderX = settings.getProperty(dashboardId + "." + dashlet + "." + DASHLET_PROPERTY_ORDERX);
				if(orderX == null) {
					orderX = "9999";
				}
				String orderY = settings.getProperty(dashboardId + "." + dashlet + "." + DASHLET_PROPERTY_ORDERY);
				if(orderY == null) {
					orderY = "9999";
				}
				String width = settings.getProperty(dashboardId + "." + dashlet + "." + DASHLET_PROPERTY_WIDTH);
				if(width == null) {
					width = "1";
				}
				String name = settings.getProperty(dashboardId + "." + dashlet + "." + DASHLET_PROPERTY_NAME);
				if(name == null) {
					name = "DefaultDashlet";
				}
				String label = settings.getProperty(dashboardId + "." + dashlet + "." + DASHLET_PROPERTY_LABEL);
				if(label == null) {
					label = name;
				}
				if(dashletFilter == null || label.startsWith(dashletFilter)) {
					if(dashboard.get(orderY) == null) {
						dashboard.put(
							orderY, 
							new TreeMap<String,DashletDescr>()
						);
					}						
					Map<String,DashletDescr> row = dashboard.get(orderY);
					row.put(
						orderX + "." + dashlet + (dashletFilter == null ? "" : "." + dashletFilter), 
						new DashletDescr(dashlet, name, width, label, orderX, orderY)
					);
				}
			}
		}
	}
	
	/**
	 * Get max for vertical order.
	 * 
	 * @return
	 */
	protected int getMaxVerticalOrder(
	) {
		return 10;
	}
	
	/**
	 * Get max for horizontal order.
	 * 
	 * @return
	 */
	protected int getMaxHorizontalOrder(
	) {
		return 10;
	}
	
	/* (non-Javadoc)
	 * @see org.openmdx.portal.servlet.control.Control#paint(org.openmdx.portal.servlet.ViewPort, boolean)
	 */
	@Override
    public void paint(
    	ViewPort p, 
    	boolean forEditing
    ) throws ServiceException {
		ApplicationContext app = p.getApplicationContext();
		HtmlEncoder_1_0 htmlEncoder = app.getHtmlEncoder();
		View view = p.getView();		
		if(view instanceof ShowObjectView) {
			ShowObjectView showView = (ShowObjectView)view;			
			String dashboardId = this.getClass().getSimpleName() + "." + this.getDashboardIdSuffix(p);
			Map<String,Map<String,DashletDescr>> dashboard = new TreeMap<String,Map<String,DashletDescr>>();
			this.getDashboard(
				p,
				dashboardId, 
				app.getSettings(), 
				null, // dashletFilter
				dashboard,
				forEditing
			);
			Path userHomeAdminIdentity = app.getUserHomeIdentity() == null ?
				null :
					app.getUserHomeIdentityAsPath().getParent().getDescendant(
						app.getPortalExtension().getAdminPrincipal(app.getCurrentSegment())
					);
			// Merge dashlets from segment administrator. 
			// Only merge dashlets with label prefix "Public."
			// Do not merge in edit mode.
			boolean isAdmin = 
				(app.getUserHomeIdentity() == null) || 
				(app.getUserHomeIdentityAsPath().equals(userHomeAdminIdentity));
			if(!forEditing && !isAdmin) {
				try {
					Properties settings = app.getUserSettings(userHomeAdminIdentity);
	    			this.getDashboard(
	    				p,
	    				dashboardId, 
	    				settings, 
	    				SHARED_DASHLET_MARKER, // dashletFilter
	    				dashboard,
	    				forEditing
	    			);	                
				} catch(Exception e) {}
			}
			if(!dashboard.isEmpty()) {
				p.write("<div id=\"", this.id, "\">");
				p.write("  <table " + this.getDashboardStyle() + ">");
				for(String y: dashboard.keySet()) {
					Map<String,DashletDescr> row = dashboard.get(y);						
					p.write("    <tr>");
					for(String x: row.keySet()) {
						DashletDescr dashletDescr = row.get(x);
						String dashletId = 
							(x.endsWith(SHARED_DASHLET_MARKER) ? SHARED_DASHLET_MARKER : "") +
							dashboardId + "." + dashletDescr.id;
						Action action = new Action(
							Action.EVENT_INVOKE_WIZARD,
							new Action.Parameter[]{
								new Action.Parameter(Action.PARAMETER_OBJECTXRI, showView.getObject().refGetPath().toXRI()),
								new Action.Parameter(Action.PARAMETER_ID, dashletId)
							},
							"",
							true
						);
						CharSequence dashletHRef = p.getEvalHRef(action).toString().replace(WebKeys.SERVLET_NAME, "wizards/Dashboard/" + dashletDescr.name + ".jsp");
						p.write("      <td class=\"", CssClass.dashlet.toString(), "\" colspan=\"", dashletDescr.width,"\" height=\"100%\" valign=\"top\">");
						p.write("        <table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">");
						if(dashletDescr.label != null && dashletDescr.label.length() > 0) {
							p.write("          <tr>");
							p.write("            <td class=\"", CssClass.dashletTitle.toString(), "\" title=\"V:", dashletDescr.orderY, ", H:", dashletDescr.orderX, ", W:", dashletDescr.width, "\">", dashletDescr.label, "</td>");
							p.write("          </tr>");
						}
						p.write("          <tr>");
						p.write("            <td>");
						p.write("              <div id=\"", dashletId, "\">");
						if(forEditing) {
							p.write("                <p>");
							p.write("                <table width=\"100%\">");
							p.write("                  <tr>");
							p.write("                    <td>");
							p.write("                      <table>");
							// Id
							p.write("                        <tr>");
			                p.write("                          <td><label>Id:</label></td>");
			                p.write("                          <td><input type=\"text\" id=\"", dashletId, ".", DASHLET_PROPERTY_ID, "\" name=\"", dashletId, ".", DASHLET_PROPERTY_ID, "\" readonly style=\"\" value=\"", htmlEncoder.encode(dashletDescr.id, false), "\"/></td>");							
							p.write("                        </tr>");
							// Type
							p.write("                        <tr>");
			                p.write("                          <td><label>Name:</label></td>");
			                p.write("                          <td><input type=\"text\" id=\"", dashletId, ".", DASHLET_PROPERTY_NAME, "\" name=\"", dashletId, ".", DASHLET_PROPERTY_NAME, "\" readonly style=\"\" value=\"", htmlEncoder.encode(dashletDescr.name, false), "\"/></td>");							
							p.write("                        </tr>");
							// Label
							p.write("                        <tr>");
			                p.write("                          <td><label>Label:</label></td>");
			                p.write("                          <td><input type=\"text\" id=\"", dashletId, ".", DASHLET_PROPERTY_LABEL, "\" name=\"", dashletId, ".", DASHLET_PROPERTY_LABEL, "\" style=\"\" value=\"", htmlEncoder.encode(dashletDescr.label, false), "\"/></td>");							
							p.write("                        </tr>");
							// orderY
							p.write("                        <tr>");
			                p.write("                          <td><label>Vertical order:</label></td>");
			                p.write("                          <td>");							
			                p.write("                            <select id=\"", dashletId, ".", DASHLET_PROPERTY_ORDERY, "\" name=\"", dashletId, ".", DASHLET_PROPERTY_ORDERY, "\" style=\"\" />");							
			                for(int i = 0; i < this.getMaxVerticalOrder(); i++) {
			                	p.write("                              <option ", (i == Integer.valueOf(dashletDescr.orderY) ? " selected" : ""), " value=\"", Integer.toString(i), "\">", Integer.toString(i), "</option>");
			                }
			                p.write("                          </td>");							
							p.write("                        </tr>");
							// orderX
							p.write("                        <tr>");
			                p.write("                          <td><label>Horizontal order:</label></td>");
			                p.write("                          <td>");							
			                p.write("                            <select id=\"", dashletId, ".", DASHLET_PROPERTY_ORDERX, "\" name=\"", dashletId, ".", DASHLET_PROPERTY_ORDERX, "\" style=\"\" />");
			                for(int i = 0; i < this.getMaxHorizontalOrder(); i++) {
			                	p.write("                              <option ", (i == Integer.valueOf(dashletDescr.orderX) ? " selected" : ""), " value=\"", Integer.toString(i), "\">", Integer.toString(i), "</option>");
			                }
			                p.write("                          </td>");							
							p.write("                        </tr>");
							// Width
							p.write("                        <tr>");
			                p.write("                          <td><label>Width:</label></td>");
			                p.write("                          <td>");							
			                p.write("                            <select id=\"", dashletId, ".", DASHLET_PROPERTY_WIDTH, "\" name=\"", dashletId, ".", DASHLET_PROPERTY_WIDTH, "\" style=\"\" />");							
			                for(int i = 1; i <= 10; i++) {
			                	p.write("                              <option ", (i == Integer.valueOf(dashletDescr.width) ? " selected" : ""), " value=\"", Integer.toString(i), "\">", Integer.toString(i), "</option>");
			                }
			                p.write("                          </td>");							
							p.write("                        </tr>");
							p.write("                      </table>");
							p.write("                    </td>");
							p.write("                    <td style=\"horizontal-align:right;vertical-align:bottom;\">");
							p.write("                      <input type=\"submit\" class=\"", CssClass.btn.toString(), " ", CssClass.btn_light.toString(), "\" name=\"Delete.", dashletId, "\" value=\"-\" onclick=\"javascript:$('Command').value=this.name;\" />");
							p.write("                    </td>");
							p.write("                  </tr>");
							p.write("                </table>");
							p.write("                <p>");
						} else {
							p.write("                <script language=\"javascript\" type=\"text/javascript\">");
	                        p.write("                  jQuery.ajax({type: 'get', url: ", dashletHRef, ", dataType: 'html', success: function(data){$('", dashletId, "').innerHTML=data;evalScripts(data);}});");							
							p.write("                </script>");
						}
						p.write("              </div>");
						p.write("            </td>");
						p.write("          </tr>");
						p.write("        </table>");
						p.write("      </td>");
					}
					p.write("    </tr>");
				}
				p.write("  </table>");
				p.write("</div>");
			}
		}
    }

	//-----------------------------------------------------------------------
	// Members
	//-----------------------------------------------------------------------
	private static final long serialVersionUID = -380329879327788318L;

	public static final String SHARED_DASHLET_MARKER = "*";
	public static final String DASHBOARD_PROPERTY_DASHLETS = "dashlets";
	public static final String DASHLET_PROPERTY_ID = "id";
	public static final String DASHLET_PROPERTY_NAME = "name";
	public static final String DASHLET_PROPERTY_LABEL = "label";
	public static final String DASHLET_PROPERTY_ORDERX = "orderX";
	public static final String DASHLET_PROPERTY_ORDERY = "orderY";
	public static final String DASHLET_PROPERTY_WIDTH = "width";
	
}
