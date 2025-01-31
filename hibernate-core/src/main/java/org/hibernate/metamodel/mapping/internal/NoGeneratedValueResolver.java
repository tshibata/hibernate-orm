/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.mapping.internal;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.metamodel.UnsupportedMappingException;
import org.hibernate.metamodel.mapping.GeneratedValueResolver;

/**
 * @author Steve Ebersole
 */
public class NoGeneratedValueResolver implements GeneratedValueResolver {
	/**
	 * Singleton access
	 */
	public static final NoGeneratedValueResolver INSTANCE = new NoGeneratedValueResolver();

//	@Override
//	public GenerationTiming getGenerationTiming() {
//		return GenerationTiming.NEVER;
//	}

	@Override
	public Object resolveGeneratedValue(Object[] row, Object entity, SharedSessionContractImplementor session, Object currentValue) {
		throw new UnsupportedMappingException( "NoGeneratedValueResolver does not support generated values" );
	}
}
