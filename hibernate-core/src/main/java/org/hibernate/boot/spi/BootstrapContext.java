/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.boot.spi;

import java.util.Collection;
import java.util.Map;

import org.hibernate.Incubating;
import org.hibernate.Internal;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.boot.CacheRegionDefinition;
import org.hibernate.boot.archive.scan.spi.ScanEnvironment;
import org.hibernate.boot.archive.scan.spi.ScanOptions;
import org.hibernate.boot.archive.spi.ArchiveDescriptorFactory;
import org.hibernate.boot.internal.ClassmateContext;
import org.hibernate.boot.model.convert.spi.ConverterDescriptor;
import org.hibernate.boot.model.relational.AuxiliaryDatabaseObject;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.hibernate.jpa.spi.MutableJpaCompliance;
import org.hibernate.metamodel.spi.ManagedTypeRepresentationResolver;
import org.hibernate.query.sqm.function.SqmFunctionDescriptor;
import org.hibernate.resource.beans.spi.BeanInstanceProducer;
import org.hibernate.type.internal.BasicTypeImpl;
import org.hibernate.type.spi.TypeConfiguration;

import org.jboss.jandex.IndexView;

/**
 * Defines a context for things generally available to the process of
 * bootstrapping a SessionFactory that are expected to be released after
 * the SessionFactory is built.
 *
 * @author Steve Ebersole
 */
@Incubating
public interface BootstrapContext {
	StandardServiceRegistry getServiceRegistry();

	MutableJpaCompliance getJpaCompliance();

	TypeConfiguration getTypeConfiguration();

	BeanInstanceProducer getBeanInstanceProducer();

	MetadataBuildingOptions getMetadataBuildingOptions();

	default IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
		return getMetadataBuildingOptions().getIdentifierGeneratorFactory();
	}

	boolean isJpaBootstrap();

	/**
	 * Indicates that bootstrap was initiated from JPA bootstrapping.  Internally {@code false} is
	 * the assumed value.  We only need to call this to mark that as true.
	 */
	void markAsJpaBootstrap();

	/**
	 * Access the temporary ClassLoader passed to us as defined by
	 * {@link jakarta.persistence.spi.PersistenceUnitInfo#getNewTempClassLoader()}, if any.
	 *
	 * @return The tempo ClassLoader
	 */
	ClassLoader getJpaTempClassLoader();

	ClassLoaderAccess getClassLoaderAccess();

	/**
	 * Access to the shared Classmate objects used throughout Hibernate's
	 * bootstrap process.
	 *
	 * @return Access to the shared Classmate delegates.
	 */
	ClassmateContext getClassmateContext();

	/**
	 * Access to the ArchiveDescriptorFactory to be used for scanning
	 *
	 * @return The ArchiveDescriptorFactory
	 */
	ArchiveDescriptorFactory getArchiveDescriptorFactory();

	/**
	 * Access to the options to be used for scanning
	 *
	 * @return The scan options
	 */
	ScanOptions getScanOptions();

	/**
	 * Access to the environment for scanning.  Consider this temporary; see discussion on
	 * {@link ScanEnvironment}
	 *
	 * @return The scan environment
	 */
	ScanEnvironment getScanEnvironment();

	/**
	 * Access to the Scanner to be used for scanning.  Can be:<ul>
	 *     <li>A Scanner instance</li>
	 *     <li>A Class reference to the Scanner implementor</li>
	 *     <li>A String naming the Scanner implementor</li>
	 * </ul>
	 *
	 * @return The scanner
	 */
	Object getScanner();

	/**
	 * Retrieve the Hibernate Commons Annotations ReflectionManager to use.
	 *
	 * @return The Hibernate Commons Annotations ReflectionManager to use.
	 *
	 * @apiNote Supported for internal use only.  This method will go away as
	 * we migrate away from Hibernate Commons Annotations to Jandex for annotation
	 * handling and XMl->annotation merging.
	 */
	@Internal
	ReflectionManager getReflectionManager();

	/**
	 * Access to the Jandex index passed by call to
	 * {@link org.hibernate.boot.MetadataBuilder#applyIndexView(IndexView)}, if any.
	 * <p>
	 * Note that Jandex is currently not used.  See https://github.com/hibernate/hibernate-orm/wiki/Roadmap7.0
	 *
	 * @return The Jandex index
	 */
	IndexView getJandexView();

	/**
	 * Access to any SQL functions explicitly registered with the MetadataBuilder.  This
	 * does not include Dialect defined functions, etc.
	 * <p>
	 * Should never return {@code null}
	 *
	 * @return The SQLFunctions registered through MetadataBuilder
	 */
	Map<String, SqmFunctionDescriptor> getSqlFunctions();

	/**
	 * Access to any AuxiliaryDatabaseObject explicitly registered with the MetadataBuilder.  This
	 * does not include AuxiliaryDatabaseObject defined in mappings.
	 * <p>
	 * Should never return {@code null}
	 *
	 * @return The AuxiliaryDatabaseObject registered through MetadataBuilder
	 */
	Collection<AuxiliaryDatabaseObject> getAuxiliaryDatabaseObjectList();

	/**
	 * Access to collected AttributeConverter definitions.
	 * <p>
	 * Should never return {@code null}
	 *
	 * @return The AttributeConverterInfo registered through MetadataBuilder
	 */
	Collection<ConverterDescriptor> getAttributeConverters();

	/**
	 * Access to all explicit cache region mappings.
	 * <p>
	 * Should never return {@code null}
	 *
	 * @return Explicit cache region mappings
	 */
	Collection<CacheRegionDefinition> getCacheRegionDefinitions();

	ManagedTypeRepresentationResolver getRepresentationStrategySelector();

	/**
	 * Releases the "bootstrap only" resources held by this BootstrapContext.
	 * <p>
	 * Only one call to this method is supported, after we have completed the process of
	 * building the (non-inflight) Metadata impl.  We may want to delay this until we
	 * get into SF building.  Not sure yet.
	 *
	 * @todo verify this ^^
	 */
	void release();

	/**
	 * To support envers
	 */
	void registerAdHocBasicType(BasicTypeImpl<?> basicType);

	/**
	 * To support envers
	 */
	<T> BasicTypeImpl<T> resolveAdHocBasicType(String key);
}
