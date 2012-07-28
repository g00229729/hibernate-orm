/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
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
package org.hibernate.metamodel.spi.relational;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Represents a named schema/catalog pair and manages objects defined within.
 *
 * @author Steve Ebersole
 */
public class Schema {
	private static final CoreMessageLogger log = Logger.getMessageLogger(
			CoreMessageLogger.class,
			Schema.class.getName()
	);

	private final Name name;
	private Map<Identifier, InLineView> inLineViews = new HashMap<Identifier, InLineView>();
	private Map<Identifier, Table> tables = new HashMap<Identifier, Table>();
	private Map<Identifier, Sequence> sequences = new HashMap<Identifier, Sequence>();

	public Schema(Name name) {
		this.name = name;
	}

	public Schema(Identifier schema, Identifier catalog) {
		this( new Name( schema, catalog ) );
	}

	public Name getName() {
		return name;
	}

	/**
	 * Returns the table with the specified logical table name.
	 *
	 * @param logicalTableName - the logical name of the table
	 *
	 * @return the table with the specified table name,
	 *         or null if there is no table with the specified
	 *         table name.
	 */
	public Table locateTable(Identifier logicalTableName) {
		return tables.get( logicalTableName );
	}

	/**
	 * Creates a {@link Table} with the specified name.
	 *
	 * @param logicalTableName The logical table name
	 * @param physicalTableName - the name of the table
	 *
	 * @return the created table.
	 */
	public Table createTable(Identifier logicalTableName, Identifier physicalTableName) {
		Table table = new Table( this, logicalTableName, physicalTableName );
		tables.put( logicalTableName, table );
		return table;
	}

	public Iterable<Table> getTables() {
		return tables.values();
	}

	public InLineView getInLineView(Identifier logicalName) {
		return inLineViews.get( logicalName );
	}

	public InLineView createInLineView(Identifier logicalName, String subSelect) {
		InLineView inLineView = new InLineView( this, logicalName, subSelect );
		inLineViews.put( logicalName, inLineView );
		return inLineView;
	}

	public Sequence locateSequence(Identifier name) {
		return sequences.get( name );
	}

	public Sequence createSequence(Identifier name, int initialValue, int increment) {
		if ( sequences.containsKey( name ) ) {
			throw new HibernateException( "Sequence was already registered with that name [" + name.toString() + "]" );
		}

		Sequence sequence = new Sequence(
				new ObjectName( this.name.catalog, this.name.schema, name ),
				initialValue,
				increment
		);
		sequences.put( name, sequence );
		return sequence;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "Schema" );
		sb.append( "{name=" ).append( name );
		sb.append( '}' );
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Schema schema = (Schema) o;

		if ( name != null ? !name.equals( schema.name ) : schema.name != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	public Iterable<Sequence> getSequences() {
		return sequences.values();
	}

	public static class Name {
		private final Identifier schema;
		private final Identifier catalog;

		public Name(Identifier schema, Identifier catalog) {
			this.schema = schema;
			this.catalog = catalog;
		}

		public Name(String schema, String catalog) {
			this( Identifier.toIdentifier( schema ), Identifier.toIdentifier( catalog ) );
		}

		public Identifier getSchema() {
			return schema;
		}

		public Identifier getCatalog() {
			return catalog;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append( "Name" );
			sb.append( "{schema=" ).append( schema );
			sb.append( ", catalog=" ).append( catalog );
			sb.append( '}' );
			return sb.toString();
		}

		@Override
		public boolean equals(Object o) {
			if ( this == o ) {
				return true;
			}
			if ( o == null || getClass() != o.getClass() ) {
				return false;
			}

			Name name = (Name) o;

			if ( catalog != null ? !catalog.equals( name.catalog ) : name.catalog != null ) {
				return false;
			}
			if ( schema != null ? !schema.equals( name.schema ) : name.schema != null ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = schema != null ? schema.hashCode() : 0;
			result = 31 * result + ( catalog != null ? catalog.hashCode() : 0 );
			return result;
		}
	}
}
