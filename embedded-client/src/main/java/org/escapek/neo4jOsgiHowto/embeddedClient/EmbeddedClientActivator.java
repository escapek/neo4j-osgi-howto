/*
 * Copyright (C) 2011 njouanin - http://www.escapek.org/ - <EscapeK> 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.escapek.neo4jOsgiHowto.embeddedClient;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EmbeddedClientActivator implements BundleActivator
{
	private GraphDatabaseService graphDb;
	
	public void start(BundleContext context) throws Exception {
		
		System.out.print("Opening database in embedded mode: ");
		graphDb = new EmbeddedGraphDatabase( "graphdb" );
		System.out.println("OK");
		Transaction tx = graphDb.beginTx();
		try {
			System.out.print("Populating it ... ");
			Node firstNode = graphDb.createNode();
			Node secondNode = graphDb.createNode();
			Relationship relationship = firstNode.createRelationshipTo( secondNode, MyRelationshipTypes.KNOWS );
			 
			firstNode.setProperty( "message", "Hello, " );
			secondNode.setProperty( "message", "world!" );
			relationship.setProperty( "message", "brave Neo4j " );
			System.out.println("OK");
			
			System.out.print( firstNode.getProperty( "message" ) );
			System.out.print( relationship.getProperty( "message" ) );
			System.out.println( secondNode.getProperty( "message" ) );
			
			tx.success();
		} 
		catch (Exception e) {
			System.out.println("KO: " + e.getMessage());
		} 
		finally {
			tx.finish();
		}
	}

	public void stop(BundleContext context) throws Exception {
		System.out.print("Closing database: ");
		graphDb.shutdown();
		System.out.println("OK");
	}

}
