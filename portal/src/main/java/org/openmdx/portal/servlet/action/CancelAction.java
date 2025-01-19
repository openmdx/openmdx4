/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: CancelAction 
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
package org.openmdx.portal.servlet.action;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.openmdx.portal.servlet.ViewPort;
import org.openmdx.portal.servlet.ViewsCache;
import org.openmdx.portal.servlet.component.EditObjectView;
import org.openmdx.portal.servlet.component.ObjectView;
import org.openmdx.portal.servlet.component.ShowObjectView;

/**
 * CancelAction
 *
 */
public class CancelAction extends BoundAction {

    public final static int EVENT_ID = 9;

    /* (non-Javadoc)
     * @see org.openmdx.portal.servlet.action.BoundAction#perform(org.openmdx.portal.servlet.view.ObjectView, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String, javax.servlet.http.HttpSession, java.util.Map, org.openmdx.portal.servlet.ViewsCache, org.openmdx.portal.servlet.ViewsCache)
     */
    @Override
    public ActionPerformResult perform(
        ObjectView currentView,
        HttpServletRequest request,
        HttpServletResponse response,        
        String parameter,
        HttpSession session,
        Map<String,String[]> requestParameters,
        ViewsCache editViewsCache,
        ViewsCache showViewsCache      
    ) {
        ObjectView nextView = currentView;
        ViewPort.Type nextViewPortType = null;
    	if(currentView instanceof EditObjectView) {
        	EditObjectView editView = (EditObjectView)currentView;
	        // EVENT_CANCEL
	        {
	            editViewsCache.removeView(
	                editView.getRequestId()
	            );
	            nextView = editView.getPreviousView(showViewsCache);
	            // If the view is embedded paint attribute pane
	            if(editView.isEditMode()) {
	                nextViewPortType = ViewPort.Type.EMBEDDED;
	            }
	        }
	        if(nextView instanceof ShowObjectView) {
	            ((ShowObjectView)nextView).selectReferencePane(
	                editView.getForReference()
	            );
	        }
    	}
        return new ActionPerformResult(
            nextView,
            nextViewPortType
        );
    }
    
}
