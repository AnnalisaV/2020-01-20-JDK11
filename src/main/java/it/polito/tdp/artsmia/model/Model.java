package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao; 
	private Map<Integer, Artist> idMapArtists; 
	private Graph<Artist, DefaultWeightedEdge> graph; 
	
	private List<Artist> best; 
	
	
	public Model() {
		this.dao= new ArtsmiaDAO(); 
		this.idMapArtists= new HashMap<>(); 
		this.dao.getAllArtists(this.idMapArtists); 
	}
	
	public List<String> getRoles(){
		return this.dao.getRoles();
	}
	
	public void creaGrafo(String role) {
		this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertex
		Graphs.addAllVertices(this.graph, this.dao.getArtistByRole(role, idMapArtists)); 
		
		//edges 
		for (CoupleArtists c : this.dao.getCouples(idMapArtists, role)){
			if (this.graph.containsVertex(c.getA1()) && this.graph.containsVertex(c.getA2())) {
				Graphs.addEdge(this.graph, c.getA1(), c.getA2(), c.getPeso()); 
			}
		}
		
	}
	
	public int nVertex() {
		return this.graph.vertexSet().size();
	}
	public int nArchi() {
		return this.graph.edgeSet().size();
	}
	
	//Artisti connessi
	public List<CoupleArtists> getCoppie(){
		List<CoupleArtists> lista= new ArrayList<>();
		
		for (DefaultWeightedEdge e : this.graph.edgeSet()) {
			lista.add(new CoupleArtists(this.graph.getEdgeSource(e), this.graph.getEdgeTarget(e), (int)this.graph.getEdgeWeight(e)));
			
		}
		Collections.sort(lista);
		return lista; 
		
		//oppure si poteva fare lista= dao.getCoppie(); 
		//Collections.sort(lista); 
	}
	
	//se l'artista e' nel grafo
	public boolean artistaNelGrafo(int id) {
		if (this.graph.containsVertex(idMapArtists.get(id)))
			return true; 
		else return false; 
		
	}
	
	public List<Artist> percorso(int id){
		Artist partenza= idMapArtists.get(id); 
		
		this.best= new ArrayList<>(); //pulita
		
		List<Artist> percorso= new ArrayList<>(); 
		int peso=-1; 
		percorso.add(partenza); 
		
		ricorsione(percorso, peso); 
		return best; 
		
	}

	private void ricorsione(List<Artist> percorso, int weight) {
		
		//generale 
		Artist ultimo= percorso.get(percorso.size()-1);
		List<Artist> vicini= Graphs.neighborListOf(this.graph, ultimo); 
		for (Artist a : vicini) {
			//primissimo che decide quale sara' il peso
		        if (percorso.size()==1) {
			
		     int peso= (int)this.graph.getEdgeWeight(this.graph.getEdge(ultimo, a)); 
		        	percorso.add(a); 
		        	ricorsione(percorso, peso); 
		        	percorso.remove(percorso.size()-1); //anche remove(a)
		}
		
			//non voglio ciclo 
		        else if (!this.graph.containsVertex(a)){
				int qstPeso= (int)this.graph.getEdgeWeight(this.graph.getEdge(ultimo, a)); 
				if(qstPeso==weight) {
					percorso.add(a); 
					ricorsione(percorso, weight); //o anche con qstPeso
					percorso.remove(percorso.size()-1); 
				}
			}
		}
		

		//ottimizzazione 
		if (percorso.size() > best.size()) {
			best= new ArrayList<>(percorso); 
		}
		
	}
}
