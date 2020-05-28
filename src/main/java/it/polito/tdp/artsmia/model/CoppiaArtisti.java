package it.polito.tdp.artsmia.model;

public class CoppiaArtisti implements Comparable<CoppiaArtisti>{

	private int id1; //id del primo artista
	private int id2; 
	private Integer peso;
	
	
	/**
	 * @param id1
	 * @param id2
	 * @param peso
	 */
	public CoppiaArtisti(int id1, int id2, int peso) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.peso = peso;
	}


	public int getId1() {
		return id1;
	}


	public void setId1(int id1) {
		this.id1 = id1;
	}


	public int getId2() {
		return id2;
	}


	public void setId2(int id2) {
		this.id2 = id2;
	}


	public int getPeso() {
		return peso;
	}


	public void setPeso(int peso) {
		this.peso = peso;
	}


	//decrescente di peso
	@Override
	public int compareTo(CoppiaArtisti other) {
		// TODO Auto-generated method stub
		return -this.peso.compareTo(other.peso);
	} 
	
	
	
}
