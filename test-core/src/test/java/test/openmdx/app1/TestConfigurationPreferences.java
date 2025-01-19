/*
 * ====================================================================
 * Project:     openMDX/Test Core, http://www.openmdx.org/
 * Description: Test Configuration Preferences 
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
package test.openmdx.app1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openmdx.junit5.JDOExtension;
import org.openmdx.junit5.OpenmdxTestCoreStandardExtension;
import org.openmdx.preferences2.jmi1.Entry;
import org.openmdx.preferences2.jmi1.Node;
import org.openmdx.preferences2.jmi1.Preferences;
import org.openmdx.preferences2.jmi1.Segment;

/**
 * Test Configuration Preferences
 */
@ExtendWith(OpenmdxTestCoreStandardExtension.class)
public class TestConfigurationPreferences {

	@RegisterExtension
	static JDOExtension jdoExtension = JDOExtension.withEntityManagerFactoryName("test-Main-EntityManagerFactory");
	
	private static final String PREFERENCES_SEGMENT_ID = "xri://@openmdx*org:openmdx:preferences2/provider/(@openmdx!configuration)/segment/org.openmdx.jdo.DataManager";

	@Test
	public void abbreviateExtraterrestrialNationality(){
		// 
		// Arrange
		//
		final Preferences preferences = getSegment().getPreferences("test.openmdx.app1.Data.PlugIn");
		final Node columnNode = preferences.getNode("dbColumn*nationality");
		final Node macroNode = preferences.getNode("dbColumn*nationality*stringMacro*0");
		final Entry columnEntry = columnNode.getEntry("canonicalColumnName");
		final Entry nameEntry = macroNode.getEntry("macroName");
		final Entry valueEntry = macroNode.getEntry("macroValue");
		// 
		// Act
		//
		final String columnPath = columnNode.getAbsolutePath();
		final String macroPath = macroNode.getAbsolutePath();
		final String macroName = nameEntry.getValue();
		final String macroValue = valueEntry.getValue();
		final String columnName = columnEntry.getValue();
		// 
		// Assert
		//
		Assertions.assertEquals("/dbColumn/nationality", columnPath);
		Assertions.assertEquals("/dbColumn/nationality/stringMacro/0", macroPath);
		Assertions.assertEquals("nationality", columnName);
		Assertions.assertEquals("Extra-Terrestrial", macroValue);
		Assertions.assertEquals("E.T.", macroName);
	}
	
	/**
	 * Retrieve the Test segment
	 * 
	 * @return the Test segment
	 */
	private Segment getSegment() {
		return jdoExtension.getEntityManager().getObjectById(Segment.class, PREFERENCES_SEGMENT_ID);
	}

}
