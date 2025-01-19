/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: RadiusAttribute
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
package org.openmdx.uses.net.sourceforge.jradiusclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.openmdx.uses.net.sourceforge.jradiusclient.exception.InvalidParameterException;
/**
 * Released under the LGPL<BR>
 * The most basic representation of an attribute to be used in the request attributes
 * section of the outgoing RadiusPackets. (see known direct known subclasses)
 * @author <a href="mailto:bloihl@users.sourceforge.net">Robert J. Loihl</a>
 */
public class RadiusAttribute {
    private static final int HEADER_LENGTH = 2;
    private byte[] packetBytes;
    /**
     * Construct a basic RadiusAttribute
     * @param type - the type of this attribute see RadiusAttributeValues class for possible types
     * @param value - the byte array representation of a specific value for this instance.
     * @throws InvalidParameterException if the type is not a valid Radius Attribute Type see RFCs 2865 and 2866
     */
    public RadiusAttribute(final int type, final byte[] value) throws InvalidParameterException {
        if (type > 256)  {
            throw new InvalidParameterException("type must be small enough to fit in a byte (i.e. less than 256) and should be chosen from static final constants defined in RadiusValues");
        }else if(null == value){
            throw new InvalidParameterException("Value cannot be NULL");
        }

        //  This implementation is oriented towards slow construction but fast retrieval
        //  of the bytes at send time.
        int length = HEADER_LENGTH + value.length;// 2 byte header
        try (ByteArrayOutputStream temp = new ByteArrayOutputStream(length)) {
            temp.write(type);
            temp.write(length);
            temp.write(value);
            temp.flush();
            this.packetBytes = temp.toByteArray();
        } catch(IOException ioex){//this should never happen
            throw new InvalidParameterException("Error constructing RadiusAttribute");
        }
    }
    /**
     * get the Radius Type for this Attribute( see rfc 2865 and 2866)
     * @return the Radius Type for this Attribute
     */
    public int getType(){
        return this.packetBytes[0];
    }
    /**
     * get the data stored for this RadiusAttribute
     * @return the byte[] stored as the value for this RadiusAttribute
     */
    public byte[] getValue(){
        int valueLength = this.packetBytes.length - HEADER_LENGTH;
        byte [] valueBytes = new byte[valueLength];
        System.arraycopy(this.packetBytes,2,valueBytes,0,valueLength);
        return valueBytes;
    }
    /**
     * get the bytes that will go into a RadiusPacket
     * @return the byte array to be used in construction of a RadiusPacket
     */
    protected final byte[] getBytes(){
        return this.packetBytes;
    }
}
