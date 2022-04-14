package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import it.polito.tdp.nobel.db.EsameDAO;

public class Model {

	private List<Esame> esami;
	private Set<Esame> best;
	private double bestMedia;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}


	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		//Ripristino soluzione migliore
		best = new HashSet<Esame>();
		bestMedia = 0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		
		cerca2(parziale, 0, m);
		
		return best;	
	}
	
	/**
	 * Questo algoritmo ha complessità n-fattoriale (N!)
	 * @param parziale
	 * @param l
	 * @param m
	 */
	private void cerca1(Set<Esame> parziale, int l, int m) {
		// Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m)
			return;
		
		if(sommaCrediti == m) {
			//Soluzione valida! Controlliamo se è la migliore
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > this.bestMedia) {
				best = new HashSet<Esame>(parziale);
				bestMedia = mediaVoti;
			}
			
			return;
		}
		
		//Sicuramente i crediti sono < m se siamo in questo punto
		if(l == esami.size())
			return;
		
		//Generiamo i sotto-problemi
		for(Esame e: esami) {
			if(! parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, l+1, m);
				parziale.remove(e);
			}
		}
		
	}

	/**
	 * Questo è l'algoritmo più veloce
	 * @param parziale
	 * @param l
	 * @param m
	 */
	private void cerca2(Set<Esame> parziale, int l, int m) {
		// Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		if(sommaCrediti > m)
			return;
		
		if(sommaCrediti == m) {
			//Soluzione valida! Controlliamo se è la migliore
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > this.bestMedia) {
				best = new HashSet<Esame>(parziale);
				bestMedia = mediaVoti;
			}
			
			return;
		}
		
		//Sicuramente i crediti sono < m se siamo in questo punto
		if(l == esami.size())
			return;
		
		//Proviamo ad aggiungere esam[l]
		parziale.add(esami.get(l));
		cerca2(parziale, l+1, m);
		
		//Proviamo a NON aggiumgere esami[l]
		parziale.remove(esami.get(l));
		cerca2(parziale, l+1, m);
		
	}
	
	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
