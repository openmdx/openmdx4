/*
 * ====================================================================
 * Project:     openMDX/Portal, http://www.openmdx.org/
 * Description: ShowObjectView 
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.openmdx.base.accessor.jmi.cci.RefObject_1_0;
import org.openmdx.kernel.exception.Throwables;
import org.openmdx.kernel.log.SysLog;
import org.openmdx.portal.servlet.Action;
import org.openmdx.portal.servlet.ApplicationContext;
import org.openmdx.portal.servlet.ViewPort;
import org.openmdx.portal.servlet.ViewsCache;
import org.openmdx.portal.servlet.component.ObjectView;
import org.openmdx.portal.servlet.component.ReferencePane;
import org.openmdx.portal.servlet.component.ShowObjectView;

public class SelectLocaleAction extends BoundAction {

	public final static int EVENT_ID = 14;

	@Override
    public ActionPerformResult perform(
        ObjectView view,
        HttpServletRequest request,
        HttpServletResponse response,        
        String parameter,
        HttpSession session,
        Map<String,String[]> requestParameters,
        ViewsCache editViewsCache,
        ViewsCache showViewsCache      
    ) throws IOException, ServletException {
		ShowObjectView currentView = (ShowObjectView)view;
    	ObjectView nextView = currentView;
        ViewPort.Type nextViewPortType = null;
        ApplicationContext app = currentView.getApplicationContext();
        try {
            String locale = Action.getParameter(parameter, Action.PARAMETER_LOCALE);
            SysLog.trace("Setting locale", locale);
            app.setCurrentLocale(locale);
            nextView = new ShowObjectView(
                currentView.getId(),
                currentView.getContainerElementId(),
                (RefObject_1_0)app.getNewPmData().getObjectById(currentView.getObject().refGetPath()),
                app,
                currentView.getHistoryActions(),
                null, // no nextPrevActions
                currentView.getLookupType(),
                null, // do not propagate resourcePathPrefix
                null, // do not propagate navigationTarget
                null // do not propagate isReadOnly
            );
            ShowObjectView showNextView = (ShowObjectView) nextView;
            List<ReferencePane> nextViewReferencePanes = showNextView.getChildren(ReferencePane.class);
            List<ReferencePane> currentViewReferencePanes = currentView.getChildren(ReferencePane.class);
            for (int i = 0; i < currentViewReferencePanes.size(); i++) {
            	nextViewReferencePanes.get(i).selectReference(currentViewReferencePanes.get(i).getSelectedReference());
            }
        } catch (Exception e) {
            Throwables.log(e);
            app.addErrorMessage(
                app.getTexts().getErrorTextCannotSetLocale(),
                new String[] { parameter, e.getMessage() });
        }
        return new ActionPerformResult(
            nextView,
            nextViewPortType
        );
    }
        
}
