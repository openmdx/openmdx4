/*
 * ====================================================================
 * Project:     openMDX, http://www.openmdx.org/
 * Description: Package Pattern Comparator Test
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
package org.openmdx.application.mof.mapping.pimdoc.spi;

import java.util.Comparator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Package Pattern Comparator Test
 */
class PackagePatternComparatorTest {

	@Test
	void when_simpleEquals_then_zero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:state2:state2";
		final String right = "org:openmdx:state2:state2";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertEquals(0, result);
	}

	@Test
	void when_wildcardEquals_then_zero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:state2:**";
		final String right = "org:openmdx:state2:**";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertEquals(0, result);
	}

	@Test
	void when_WildcardFirst_then_greaterThanZero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:state2:**";
		final String right = "org:openmdx:state2:state2";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertTrue(result > 0);
	}

	@Test
	void when_WildcardLast_then_lessThanZero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:state2";
		final String right = "org:openmdx:state2:**";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertTrue(result < 0);
	}
	
	@Test
	void when_alphabeticallyBefore_then_lessThanZero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:state1:state1";
		final String right = "org:openmdx:state2:state2";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertTrue(result < 0);
	}

	@Test
	void when_superPackageBefore_then_lessThanZero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:openmdx";
		final String right = "org:openmdx:base:base";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertTrue(result < 0);
	}

	@Test
	void when_superPackageAfter_then_greaterThanZero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:base:base";
		final String right = "org:openmdx:openmdx";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertTrue(result > 0);
	}
	
	@Test
	void when_alphabeticallyAfter_then_graterThanZero() {
		//
		// Arrange
		//
		final Comparator<String> testee = new PackageGroupComparator();
		final String left = "org:openmdx:state2:state2";
		final String right = "org:openmdx:state1:state1";
		// 
		// Act		
		//
		final int result = testee.compare(left, right);
		//
		// Assert
		//
		Assertions.assertTrue(result > 0);
	}
	
	@Test
	void when_Asterisk_than_Tilde() {
		//
		// Arrange
		//
		final String packagePattern = "com:example:**";
		// 
		// Act		
		//
		final String transformed = PackageGroupComparator.orderWildcardLast(packagePattern);
		//
		// Assert
		//
		Assertions.assertEquals("com:example:~", transformed);
	}
	
	@Test
	void when_Transformed_than_greaterThanFullyQualifiedPackageo() {
		//
		// Arrange
		//
		final String packagePattern = "com:example:**";
		// 
		// Act		
		//
		final String transformed = PackageGroupComparator.orderWildcardLast(packagePattern);
		//
		// Assert
		//
		Assertions.assertTrue(transformed.compareTo("com:example:example") > 0);
	}

	@Test
	void when_qualifiedPackageName_then_wildcardPattern() {
		//
		// Arrange
		//
		final String wildcardPattern = "com:example:example";
		// 
		// Act		
		//
		final String transformed = PackageGroupComparator.getDescendants(wildcardPattern);
		//
		// Assert
		//
		Assertions.assertEquals("com:example:**", transformed);
	}
	
}
