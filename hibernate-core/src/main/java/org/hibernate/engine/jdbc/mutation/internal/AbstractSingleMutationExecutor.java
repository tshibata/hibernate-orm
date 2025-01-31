/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html.
 */
package org.hibernate.engine.jdbc.mutation.internal;

import java.util.Locale;

import org.hibernate.engine.jdbc.mutation.JdbcValueBindings;
import org.hibernate.engine.jdbc.mutation.ParameterUsage;
import org.hibernate.engine.jdbc.mutation.group.PreparedStatementDetails;
import org.hibernate.sql.model.PreparableMutationOperation;
import org.hibernate.sql.model.jdbc.JdbcValueDescriptor;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractSingleMutationExecutor extends AbstractMutationExecutor {
	private final PreparableMutationOperation mutationOperation;
	private final JdbcValueBindingsImpl valueBindings;

	public AbstractSingleMutationExecutor(PreparableMutationOperation mutationOperation) {
		this.mutationOperation = mutationOperation;
		this.valueBindings = new JdbcValueBindingsImpl(
				mutationOperation.getMutationType(),
				mutationOperation.getMutationTarget(),
				this::findJdbcValueDescriptor
		);
	}

	protected PreparableMutationOperation getMutationOperation() {
		return mutationOperation;
	}

	protected abstract PreparedStatementGroupSingleTable getStatementGroup();

	@Override
	public PreparedStatementDetails getPreparedStatementDetails(String tableName) {
		final PreparedStatementDetails statementDetails = getStatementGroup().getSingleStatementDetails();
		assert statementDetails.getMutatingTableDetails().getTableName().equals( tableName );
		return statementDetails;
	}

	private JdbcValueDescriptor findJdbcValueDescriptor(String tableName, String columnName, ParameterUsage usage) {
		assert mutationOperation.getTableDetails().getTableName().equals( tableName )
				: String.format( Locale.ROOT, "table names did not match : `%s` & `%s`", tableName, mutationOperation.getTableDetails().getTableName()  );
		return mutationOperation.findValueDescriptor( columnName, usage );
	}

	@Override
	public JdbcValueBindings getJdbcValueBindings() {
		return valueBindings;
	}
}
