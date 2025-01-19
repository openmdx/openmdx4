/*
 * ==================================================================== 
 * Project: openMDX, http://www.openmdx.org
 * Description: Graphviz Layer
 * Owner: the original authors. 
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
package org.openmdx.base.mof.image;

import org.openmdx.base.exception.RuntimeServiceException;
import org.openmdx.kernel.exception.BasicException;

/**
 * Graphviz Layer
 */
class GraphvizLayer {

    GraphvizLayer(GraphvizStyle styleSheet) {
		this.parameters = new GraphvizAttributes(styleSheet, "_class", "layer", "mindist");
	}

    private final GraphvizAttributes parameters;
    private Integer layer;
    private String id;
    
	public GraphvizAttributes getParameters() {
        return this.parameters;
    }

    public Integer getLayer(){
    	if(this.layer == null) {
    		final String layer = this.parameters.getValue("layer");
    		if(layer == null) {
    			throw new RuntimeServiceException(
					BasicException.Code.DEFAULT_DOMAIN,
					BasicException.Code.ASSERTION_FAILURE,
					"Unknown rank. Specify layer with attribute 'layer'"
				);
    		}
    		this.layer = Integer.valueOf(layer);
    	}
		return layer;
	}

	public String getId() {
		if(this.id == null) {
			this.id = "LAYER[" + getLayer() + "]";
		}
		return id;
	}

	public String getMinDist() {
		return this.parameters.getValue("mindist");
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        try {
            this.parameters.setStrictValue("style", "invis");
            this.layer = Integer.valueOf(this.parameters.getValue("layer"));
            final StringBuilder layer = new StringBuilder("rank = \"same\"\n\t\t");
            GraphvizAttributes.appendQuoted(layer, getId());
            this.parameters.appendTo(layer, "\t\t");
            return layer.toString();
        } catch (Exception e) {
            throw new RuntimeServiceException(e);
        }
    }

    
}