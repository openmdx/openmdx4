/*
 * ====================================================================
 * Project:     openMDX/Core, http://www.openmdx.org/
 * Description: Base64 Input Stream
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
 * This product includes software developed by other organizations as
 * listed in the NOTICE file.
 */
package org.openmdx.base.io;

import java.io.ByteArrayInputStream;

import org.openmdx.base.exception.ServiceException;
import org.openmdx.base.text.conversion.Base64;
import org.openmdx.kernel.exception.BasicException;

/**
 * Base64 Input Stream
 */
public class Base64InputStream extends ByteArrayInputStream {

	/**
	 * Constructor
	 * 
	 * @param data BASE64 encoded data
	 */
	public Base64InputStream(
		String data
	) {
		super(Base64.decode(data));
	}

	/**
	 * Constructor
	 * 
	 * @param data BASE64 encoded data
	 * @param offset
	 * @param length
	 */
	public Base64InputStream(
		char[] data,
		int offset,
		int length
	) {
		super(Base64.decode(data, offset, length));
	}

	/**
	 * Convenience method to decode a single object
	 * 
	 * @param data a base64 encoded object
	 * 
	 * @return the deserialized object
	 * 
	 * @throws ServiceException
	 */
	public static Object decode(
		String data
	) throws ServiceException{
		try {
			return data == null  || "".equals(data) ? null : new java.io.ObjectInputStream(
				new Base64InputStream(data)
			).readObject();
		} catch (Exception exception) {
			throw new ServiceException(
				exception,
				BasicException.Code.DEFAULT_DOMAIN,
				BasicException.Code.TRANSFORMATION_FAILURE,
				"Base-64 decoding failed",
				new BasicException.Parameter("encoded", data)
			);
		}
	}
	
}