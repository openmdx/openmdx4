/*
 * ====================================================================
 * Project:     openMDXx, http://www.openmdx.org/
 * Description: Country Code 
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
package test.openmdx.datatypes1.dto;

/**
 * Country Code
 */
public class CountryCode implements Code {

    /**
     * Constructor 
     * 
     * @param countryCode ISO 3166-1 alpha-2 country code
     *
     */
    private CountryCode(
        String countryCode
    ) {
        this.countryCode = countryCode;
    }

    /**
     * ISO 3166-1 alpha-2 country code
     */
    private final String countryCode;
    
    /* (non-Javadoc)
     * @see test.openmdx.datatypes1.dto.Code#getValue()
     */
    @Override
    public String getValue(){
        return this.countryCode;
    }

    /**
     * Factory Method
     * 
     * @param countryCode ISO 3166-1 alpha-2 country code
     *
     * @return the corresponding country code object
     */
    public static CountryCode valueOf(String countryCode) {
        return countryCode == null ? null : new CountryCode(countryCode);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.countryCode.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CountryCode) {
            CountryCode that = (CountryCode)obj;
            return this.getValue().equals(that.getValue());
        } else {
            return false;
        }
    }

}
