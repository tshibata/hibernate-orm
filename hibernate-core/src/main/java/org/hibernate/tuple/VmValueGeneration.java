/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.tuple;

import java.lang.reflect.Constructor;
import java.util.EnumSet;

import org.hibernate.HibernateException;
import org.hibernate.Internal;
import org.hibernate.Session;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.generator.InMemoryGenerator;

import static org.hibernate.internal.util.ReflectHelper.getDefaultConstructor;

/**
 * An {@link InMemoryGenerator} which delegates to a {@link ValueGenerator}.
 * Underlies the {@link GeneratorType} annotation.
 *
 * @author Gunnar Morling
 *
 * @deprecated since {@link GeneratorType} is deprecated
 */
@Internal
@Deprecated(since = "6.2")
public class VmValueGeneration implements InMemoryGenerator {

	private final EnumSet<EventType> eventTypes;
	private final ValueGenerator<?> generator;

	public VmValueGeneration(GeneratorType annotation) {
		Constructor<? extends ValueGenerator<?>> constructor = getDefaultConstructor( annotation.type() );
		try {
			generator = constructor.newInstance();
		}
		catch (Exception e) {
			throw new HibernateException( "Couldn't instantiate value generator", e );
		}
		eventTypes = annotation.when().eventTypes();
	}

	@Override
	public EnumSet<EventType> getEventTypes() {
		return eventTypes;
	}

	@Override
	public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
		return generator.generateValue( (Session) session, owner, currentValue );
	}
}
