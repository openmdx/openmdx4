/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Primitive Type Decoder Test
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

package org.w3c.spi;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openmdx.kernel.text.spi.Decoder;

/**
 * Primitive Type Decoder Test
 */
public class PrimitiveTypeDecoderTest {

    @Test
    public void whenNoCastThenRaw() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "foo.bar";
        //
        // Act
        //
        final Object decodedValue = testee.decode(encodedValue);
        //
        // Assert
        //
        Assertions.assertEquals(encodedValue, decodedValue);
    }

    @Test
    public void whenNullThenNull() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = null;
        //
        // Act
        //
        final Object decodedValue = testee.decode(encodedValue);
        //
        // Assert
        //
        Assertions.assertNull(decodedValue);
    }

    @Test
    public void whenEmptyValueThenEmpty() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "";
        //
        // Act
        //
        final Object decodedValue = testee.decode(encodedValue);
        //
        // Assert
        //
        Assertions.assertEquals("",decodedValue);
    }

    @Test
    public void whenEmptyStringThenEmpty() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "(java.lang.String)";
        //
        // Act
        //
        final Object decodedValue = testee.decode(encodedValue);
        //
        // Assert
        //
        Assertions.assertEquals("",decodedValue);
    }
    
    @Test
    public void whenIntegerCastThenInteger() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "(java.lang.Integer)4711";
        //
        // Act
        //
        final Object decodedValue = testee.decode(encodedValue);
        //
        // Assert
        //
        Assertions.assertEquals(Integer.valueOf(4711), decodedValue);
    }

    @Test
    public void whenFractionThenIllegalArgumentException() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "(java.lang.Integer)47.11";
        //
        // Act/Assert
        //
        try {
        	testee.decode(encodedValue);
        	Assertions.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        	// 47.11 is a fraction
        }
    }

    @Test
    public void whenUnsupportedThenIllegalArgumentException() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "(com.example.Foo)bar";
        //
        // Act/Assert
        //
        try {
        	testee.decode(encodedValue);
        	Assertions.fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        	// Type com.example.Foo is not supported
        }
    }
    
    @Test
    public void whenDecimalCastThenBigDecimal() {
        //
        // Arrange
        //
        final Decoder testee = PrimitiveTypeParsers.getDecoder(PrimitiveTypeParsers.getStandardParser());
        final String encodedValue = "(java.math.BigDecimal)1";
        //
        // Act
        //
        final Object decodedValue = testee.decode(encodedValue);
        //
        // Assert
        //
        Assertions.assertEquals(BigDecimal.ONE, decodedValue);
    }

    
}
