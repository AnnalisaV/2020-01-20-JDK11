package it.polito.tdp.artsmia.model;

public class CoupleArtists implements Comparable<CoupleArtists> {

	private Artist a1; 
	private Artist a2; 
	private Integer peso; //numero di exhibition in cui hanno esposto insieme 
	
	/**
	 * @param a1
	 * @param a2
	 * @param peso
	 */
	public CoupleArtists(Artist a1, Artist a2, Integer peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}

	public Artist getA1() {
		return a1;
	}

	public Artist getA2() {
		return a2;
	}

	public Integer getPeso() {
		return peso;
	}

	//decrescente di peso
	@Override
	public int compareTo(CoupleArtists o) {
		
		return o.peso.compareTo(this.peso);
	}

	@Override
	public String toString() {
		return this.a1.toString()+ " "+this.a2.toString()+" "+this.peso;
	}
	
	
	
}
