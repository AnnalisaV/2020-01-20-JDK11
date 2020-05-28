package it.polito.tdp.artsmia.model;

import java.util.Collection;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao; 
	private Graph<Integer, DefaultWeightedEdge> graph; 
	private List<CoppiaArtisti> coppie; 
	
	public Model() {
		this.dao= new ArtsmiaDAO(); 
	}
	
	public Collection<String> getRuoli(){
		return this.dao.getRuoli(); 
	}
	
	public void creaGrafo(String ruolo) {
		
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		
		Graphs.addAllVertices(this.graph, this.dao.getArtisti(ruolo)); 
		
		this.coppie= this.dao.getCoppie(ruolo); 
		
		for(CoppiaArtisti c : this.coppie) {
			//per i vertici
			/*if(!this.graph.containsVertex(c.getId1())) {
				this.graph.addVertex(c.getId1()); 
			}
			if(!this.graph.containsVertex(c.getId2())) {
				this.graph.addVertex(c.getId2()); 
			}
			*/
			//per gli archi
			if(this.graph.getEdge(c.getId1(), c.getId2()) ==null) {
				Graphs.addEdgeWithVertices(this.graph, c.getId1(), c.getId2(), c.getPeso()); 
			}
			
		}
		
		System.out.println("Grafo creato! Vertici"+this.graph.vertexSet().size()+" archi "+this.graph.edgeSet().size()+"\n");
		
		}

	public int getNVertex() {
		return this.graph.vertexSet().size(); 
	}
	
	public int getNArchi() {
		return this.graph.edgeSet().size(); 
	}
}
