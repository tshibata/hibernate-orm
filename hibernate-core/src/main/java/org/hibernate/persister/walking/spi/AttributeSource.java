/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.persister.walking.spi;

import org.hibernate.NotYetImplementedFor6Exception;

/**
* @author Steve Ebersole
*/
public interface AttributeSource {
	default int getPropertyIndex(String propertyName) {
		throw new NotYetImplementedFor6Exception( getClass() );
	}
}
