/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2012, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.service.schema.spi;

import java.util.List;

import org.hibernate.metamodel.spi.relational.Identifier;

/**
 * @author Steve Ebersole
 */
public interface ExistingForeignKeyMetadata {
	/**
	 * Obtain the identifier for this FK.
	 *
	 * @return The FK identifier.
	 */
	public Identifier getForeignKeyIdentifier();

	/**
	 * Get the list of column mappings that define the reference.
	 *
	 * @return The mapping list
	 */
	public List<ColumnReferenceMapping> getColumnReferenceMappingList();

	public static interface ColumnReferenceMapping {
		/**
		 * Obtain the information about the referencing column (the source column, which points to
		 * the referenced column).
		 *
		 * @return The referencing column.
		 */
		public ExistingColumnMetadata getReferencingColumnMetadata();

		/**
		 * Obtain the information about the referenced column (the target side).
		 *
		 * @return The referenced column
		 */
		public ExistingColumnMetadata getReferencedColumnMetadata();
	}
}
