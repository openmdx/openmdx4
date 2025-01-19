/*
 * ====================================================================
 * Project:     openmdx, http://www.openmdx.org/
 * Description: openMDX URL Reader
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
 * This product includes software developed by the Apache Software
 * Foundation (http://www.apache.org/).
 */
package org.openmdx.base.text.conversion;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * URL Reader
 * <p>
 * Use ISO-8859-1 as default character set as specified in RFC 2068
 * (http://www.ietf.org/rfc/rfc2068.txt)
 */
public class URLReader extends InputStreamReader {

	/**
	 * Constructor
	 * 
	 * @param url the URL to read from
	 * 
	 * @throws IOException 
	 */
	public URLReader(
		URL url
	) throws IOException {
		this(url.openConnection());
	}

	/**
	 * Constructor
	 * 
	 * @param urlConnection the URL connection to read from
	 * 
	 * @throws IOException 
	 */
	protected URLReader(
		URLConnection urlConnection
	) throws IOException{
		super(
			urlConnection.getInputStream(),
			getEncoding(urlConnection)
		);
	}

	/**
	 * Determine an URL connection's character set.
	 * <p>
	 * Use ISO-8859-1 as default value as specified in RFC 2068
	 * (http://www.ietf.org/rfc/rfc2068.txt)
	 * 
	 * @param urlConnection
	 * 
	 * @return the charcater set
	 * 
	 * @throws IOException
	 */
	public static String getEncoding(
		URLConnection urlConnection
	) throws IOException {
		String contentType = urlConnection.getContentType();
		if(contentType == null){
			return DEFAULT_ENCODING;
		} else {
			contentType = contentType.toUpperCase();
			int charset = contentType.indexOf("CHARSET=");
			return charset < 0 ? DEFAULT_ENCODING : contentType.substring(charset+8);
		}
	}

	/**
	 * Use ISO Latin 1 as default text encoding
	 */
	public static final String DEFAULT_ENCODING = "ISO-8859-1";

}
