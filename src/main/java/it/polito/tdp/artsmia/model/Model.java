package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
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
	private List<Integer> best; 
	
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
		
		System.out.println("Grafo creato! Vertici "+this.graph.vertexSet().size()+" archi "+this.graph.edgeSet().size()+"\n");
		
		}

	public int getNVertex() {
		return this.graph.vertexSet().size(); 
	}
	
	public int getNArchi() {
		return this.graph.edgeSet().size(); 
	}
	
	public List<CoppiaArtisti> getCoppie(){
		return this.coppie; 
	}
	
	/**
	 * Ricerca del percorso migliore  partire da un certo Artista
	 * @param sorgente
	 * @return
	 */
	public List<Integer> trovaPercorso(Integer sorgente) {
		
		this.best= new ArrayList<>(); //sempre pulito
		 List<Integer> percorso= new ArrayList<>(); //parziale 
		 percorso.add(sorgente); 
		 
		                     //peso che non avro' mai nel grafo
		 ricorsione(percorso, -1); 
		 return this.best; 
		
	}
	                                                //valore da mantenere per discriminare chi prendere
	private void ricorsione(List<Integer> percorso, int peso) {
		//caso terminale e' implicito nel for perche' quando finisce la lista di vicini
		//si termina da se' 
		
		Integer last= percorso.get(percorso.size()-1); //da chi voglio partire
		List<Integer> vicini= Graphs.neighborListOf(this.graph, last); 
		
		for(Integer i : vicini) {
			if(!percorso.contains(i) && peso==-1) {
				//solo all'inizio quindi si aggiunge un vicino qualunque
			percorso.add(i);               //solo gli archi con questo peso 
			ricorsione(percorso,(int)this.graph.getEdgeWeight(this.graph.getEdge(last,  i)) ); 
			percorso.remove(i); 
		}
			else {
				//sono in step successivi dell ricorsione, devo prendere solo il peso giusto
				if(!percorso.contains(i) && this.graph.getEdgeWeight(this.graph.getEdge(last,  i)) == peso) {
					percorso.add(i);     //ora e' fisso
					ricorsione(percorso, peso);
					percorso.remove(i); 
				}
				
			}
		
		}
		

		//caso di ottimizzazione
		if (percorso.size()>this.best.size()) {
			this.best= new ArrayList<>(percorso); 
		}
		
		
		
	}
	
	
	public boolean contiene(Integer id) {
		if (this.graph.containsVertex(id)) {
			return true; 
		}
		else return false; 
	}
}
