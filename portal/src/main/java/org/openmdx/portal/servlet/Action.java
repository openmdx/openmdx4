/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: Action
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.openmdx.portal.servlet.action.FindObjectAction;

/**
 * Action
 *
 */
public final class Action implements Serializable {

	public static class Parameter implements Serializable {

		public Parameter(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return this.name;
		}

		public String getValue() {
			return this.value;
		}

		private static final long serialVersionUID = 3257572801782296631L;
		private final String name;
		private final String value;
	}

	/**
	 * Constructor.
	 * 
	 * @param event
	 * @param parameters
	 * @param title
	 * @param isEnabled
	 */
	public Action(
		int event,
		Action.Parameter[] parameters,
		String title,
		boolean isEnabled
	) {
		this(event, parameters, title, null, isEnabled);
	}

	/**
	 * Constructor.
	 * 
	 * @param event
	 * @param parameters
	 * @param title
	 * @param iconKey
	 * @param isEnabled
	 */
	public Action(
		int event,
		Action.Parameter[] parameters,
		String title,
		String iconKey,
		boolean isEnabled
	) {
		this(event, parameters, title, null, iconKey, isEnabled);
	}

	/**
	 * Constructor.
	 * 
	 * @param event
	 * @param parameters
	 * @param title
	 * @param toolTip
	 * @param iconKey
	 * @param isEnabled
	 */
	public Action(
		int event,
		Action.Parameter[] parameters,
		String title,
		String toolTip,
		String iconKey,
		boolean isEnabled
	) {
		this.event = event;
		this.title = title;
		this.isEnabled = isEnabled;
		this.parameters = parameters;
		if (toolTip != null) {
			this.toolTip = toolTip;
		}
		if (iconKey != null) {
			this.iconKey = iconKey;
		}
	}

	/**
	 * Get action.
	 * 
	 * @param feature
	 * @param id
	 * @return
	 */
	public static Action getFindObjectAction(
		String feature,
		String id
	) {
		return new Action(
			FindObjectAction.EVENT_ID,
			new Action.Parameter[] {
				new Action.Parameter(Action.PARAMETER_REFERENCE, feature),
				new Action.Parameter(Action.PARAMETER_ID, id)
			},
			"",
			WebKeys.ICON_LOOKUP,
			true
		);
	}

	/**
	 * Returns the value of parameter with specified name. The parameter string is
	 * of the form name:(value)
	 * 
	 * @param parameter
	 * @param name
	 * @return
	 */
	public static String getParameter(
		String parameter,
		String name
	) {
		int start = 0;
		if ((start = parameter.indexOf(name + "*(")) < 0) {
			return "";
		}
		start += name.length() + 2;
		int end = start;
		int nesting = 1;
		while ((end < parameter.length()) && (nesting > 0)) {
			char c = parameter.charAt(end);
			if (c == '(') {
				nesting++;
			} else if (c == ')') {
				nesting--;
			}
			end++;
		}
		return parameter.substring(start, end - 1);
	}

	/**
	 * Get event.
	 * 
	 * @return
	 */
	public int getEvent(
	) {
		return this.event;
	}

	/**
	 * Get title.
	 * 
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Get tooltip.
	 * 
	 * @return
	 */
	public String getToolTip() {
		return this.toolTip;
	}

	/**
	 * Get query parameter.
	 * 
	 * @return
	 */
	public String getParameter(
	) {
		if (this.parameter == null) {
			StringBuilder parameter = new StringBuilder();
			if (this.parameters != null) {
				for(int i = 0; i < this.parameters.length; i++) {
					parameter
						.append((i == 0) ? "" : "*").append(parameters[i].getName()).append("*(")
						.append(parameters[i].getValue()).append(")");
				}
			}
			this.parameter = parameter.toString();
		}
		return this.parameter;
	}

	/**
	 * Encode all characters which are not encoded by encodeURI
	 * 
	 * @return
	 */
	public String getParameterEncoded() {
		String s = this.getParameter();
		StringBuilder t = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '+') {
				t.append("%2B");
			} else if (c == '=') {
				t.append("%3D");
			} else if (c == '$') {
				t.append("%24");
			} else if (c == ',') {
				t.append("%2C");
			} else if (c == '/') {
				t.append("%2F");
			} else if (c == '?') {
				t.append("%3F");
			} else if (c == ':') {
				t.append("%3A");
			} else if (c == '@') {
				t.append("%40");
			} else if (c == '&') {
				t.append("%26");
			} else if (c == '#') {
				t.append("%23");
			} else {
				t.append(c);
			}
		}
		return t.toString();
	}

	/**
	 * Returns parameter value of specified parameter. Parameters are ; separated
	 * and of the form name=value.
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		if (this.parameters == null) {
			return "";
		}
		for (int i = 0; i < this.parameters.length; i++) {
			if (name.equals(this.parameters[i].getName())) {
				return this.parameters[i].getValue();
			}
		}
		return "";
	}

	/**
	 * Get parameters.
	 * 
	 * @return
	 */
	public Action.Parameter[] getParameters() {
		return this.parameters;
	}

	/**
	 * Get icon key.
	 * 
	 * @return
	 */
	public String getIconKey() {
		return this.iconKey;
	}

	/**
	 * Set icon key.
	 * 
	 * @param iconKey
	 */
	public void setIconKey(
		String iconKey
	) {
		this.iconKey = iconKey;
	}

	/**
	 * return true if enabled.
	 * 
	 * @return
	 */
	public boolean isEnabled(
	) {
		return this.isEnabled;
	}

	@Override
	public String toString(
	) {
		return "Action={event=" + this.event + ", " + ", parameter=" + this.getParameter() + ", title=" + this.title + "}";
	}

	/**
	 * Get encoded href.
	 * 
	 * @return
	 */
	public String getEncodedHRef(
	) {
		return this.getEncodedHRef(null);
	}

	/**
	 * Get encoded href.
	 * 
	 * @param requestId
	 * @return
	 */
	public String getEncodedHRef(
		String requestId
	) {
		String[] components = this.getHRef(requestId);
		StringBuilder href = new StringBuilder(components[0]);
		for(int i = 1; i < components.length; i += 2) {
			try {
				href.append(i == 1 ? "?" : "&")
					.append(components[i]).append("=")
					.append(URLEncoder.encode(components[i + 1], "UTF-8"));
			} catch (UnsupportedEncodingException ignore) {}
		}
		return href.toString();
	}

	/**
	 * Get href.
	 * 
	 * @return
	 */
	public String[] getHRef(
	) {
		return this.getHRef(null);
	}

	/**
	 * Get href.
	 * 
	 * @param requestId
	 * @return
	 */
	public String[] getHRef(
		String requestId
	) {
		String actionParameter = this.getParameter();
		int n = 3;
		if(requestId != null) {
			n += 2;
		}
		if(!actionParameter.isEmpty()) {
			n += 2;
		}
		String[] components = new String[n];
		n = 0;
		// Servlet name
		// Return download URLs as SERVLET_NAME/<file name>?params. This is
		// an IE workaround. Setting the reply header fields is not sufficient.
		// Also see http://ppewww.ph.gla.ac.uk/~flavell/www/content-type.html
		StringBuilder href = new StringBuilder(WebKeys.SERVLET_NAME);
		if(
			(this.getEvent() == Action.EVENT_DOWNLOAD_FROM_LOCATION) || 
			(this.getEvent() == Action.EVENT_DOWNLOAD_FROM_FEATURE)
		) {
			href.append("/");
			try {
				href.append(
					URLEncoder.encode(this.getParameter(Action.PARAMETER_NAME), "UTF-8")
				);
			} catch(Exception ignore) {}
		}
		components[n++] = href.toString();
		// REQUEST_ID
		if(requestId != null) {
			components[n++] = WebKeys.REQUEST_ID;
			components[n++] = requestId;
		}
		// REQUEST_EVENT
		components[n++] = WebKeys.REQUEST_EVENT;
		components[n++] = Integer.toString(this.getEvent());
		// Parameter name
		if(!actionParameter.isEmpty()) {
			components[n++] = WebKeys.REQUEST_PARAMETER;
			components[n++] = actionParameter;
		}
		return components;
	}

	/**
	 * Get script which evaluates href.
	 * 
	 * @return
	 */
	public String getEvalHRef(
	) {
		return this.getEvalHRef(null);
	}

	/**
	 * Get script which evaluates href.
	 * 
	 * @param requestId
	 * @return
	 */
	public String getEvalHRef(
		String requestId
	) {
		String[] components = this.getHRef(requestId);
		StringBuilder href = new StringBuilder("getEncodedHRef([");
		for(int i = 0; i < components.length; i++) {
			(i > 0 ? href.append(", ") : href).append("'")
				.append(components[i])
				.append("'");
		}
		return href.append("])").toString();
	}

	// -------------------------------------------------------------------------
	// Variables
	// -------------------------------------------------------------------------
	private static final long serialVersionUID = 3616453392827103289L;

	private final int event;
	private String title = "N/A";
	private String toolTip = "N/A";
	private String parameter = null;
	private final Action.Parameter[] parameters;
	private String iconKey = WebKeys.ICON_DEFAULT;
	private final boolean isEnabled;

	// -------------------------------------------------------------------------
	// Parameters
	// -------------------------------------------------------------------------
	public static final String PARAMETER_PANE = "pane";
	public static final String PARAMETER_PAGE = "page";
	public static final String PARAMETER_REFERENCE = "reference";
	public static final String PARAMETER_REFERENCE_NAME = "referenceName";
	public static final String PARAMETER_FOR_CLASS = "forClass";
	public static final String PARAMETER_FOR_REFERENCE = "forReference";
	public static final String PARAMETER_NAME = "name";
	public static final String PARAMETER_TYPE = "type";
	public static final String PARAMETER_ID = "id";
	public static final String PARAMETER_TARGETXRI = "targetXri";
	public static final String PARAMETER_ROW_ID = "rowId";
	public static final String PARAMETER_REQUEST_ID = "requestId";
	public static final String PARAMETER_OBJECTXRI = "xri";
	public static final String PARAMETER_TAB = "tab";
	public static final String PARAMETER_LOCALE = "locale";
	public static final String PARAMETER_MIME_TYPE = "mimeType";
	public static final String PARAMETER_LOCATION = "location";
	public static final String PARAMETER_FEATURE = "feature";
	public static final String PARAMETER_STATE = "state";
	public static final String PARAMETER_POSITION = "position";
	public static final String PARAMETER_FORMAT = "format";
	public static final String PARAMETER_SIZE = "size";
	public static final String PARAMETER_MODE = "mode";
	public static final String PARAMETER_ORIGIN = "origin";
	public static final String PARAMETER_VIEW_PORT = "viewport";
	public static final String PARAMETER_FILTER_BY_FEATURE = "filterByFeature";
	public static final String PARAMETER_FILTER_BY_TYPE = "filterByType";
	public static final String PARAMETER_FILTER_BY_QUERY = "filterByQuery";
	public static final String PARAMETER_FILTER_OPERATOR = "filterOperator";
	public static final String PARAMETER_ORDER_BY_FEATURE = "orderByFeature";
	public static final String PARAMETER_SWAP_GRID_COLUMNS = "swapGridColumns";

	// -------------------------------------------------------------------------
	// Events
	// -------------------------------------------------------------------------
	public final static int EVENT_NONE = 0;
	public final static int EVENT_DOWNLOAD_FROM_LOCATION = 23;
	public final static int EVENT_DOWNLOAD_FROM_FEATURE = 35;
	public final static int EVENT_INVOKE_WIZARD = 41;
	public final static int EVENT_SET_ROLE = 47;

	// -----------------------------------------------------------------------
	// Macro types
	// -----------------------------------------------------------------------
	public static final int MACRO_TYPE_NA = 0;
	public static final int MACRO_TYPE_JAVASCRIPT = 1;

}
