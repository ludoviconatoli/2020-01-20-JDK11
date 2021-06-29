package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	
	public Model() {
		dao = new ArtsmiaDAO();
	}
	
	public List<String> getRuoli(){
		return dao.getRuoli();
	}
	
	public void creaGrafo(String ruolo) {
		idMap = new HashMap<>();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		dao.getArtisti(idMap, ruolo);
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Adiacenza a: dao.getArchi(idMap, ruolo)) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getArchi(String ruolo){
		List<Adiacenza> result = new ArrayList<>();
	
		result.addAll(dao.getArchi(idMap, ruolo));
		
		if(result != null) {
			Collections.sort(result, new Comparator<Adiacenza>() {

				@Override
				public int compare(Adiacenza o1, Adiacenza o2) {
					
					return Double.compare(o2.getPeso(), o1.getPeso());
				}
			});
		
			return result;

		}else {
			return null;
		}
	}
	
	private List<Artist> percorsoMigliore;
	private double pesoMigliore;
	
	public List<Artist> calcolaCamminoLungo(int id){
		
		
		if(!idMap.containsKey(id)) {
			return null;
		}else {
			Artist start = idMap.get(id);
			List<Artist> parziale = new ArrayList<>();
			percorsoMigliore = new ArrayList<>();
			parziale.add(start);
			
			cerca(parziale, -1);
			
			return percorsoMigliore;
		}
	}

	private void cerca(List<Artist> parziale, double peso) {
		
		Artist ultimo = parziale.get(parziale.size()-1);
		List<Artist> vicini = Graphs.neighborListOf(grafo, ultimo);
		
		for(Artist vicino: vicini) {
			if(!parziale.contains(vicino) && peso == -1) {
				
				parziale.add(vicino);
				cerca(parziale, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)));
				parziale.remove(vicino);
			}else {
				if(!parziale.contains(vicino) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)) == peso) {
					parziale.add(vicino);
					cerca(parziale, peso);
					parziale.remove(vicino);
				}
			}
		}
		
		if(parziale.size() > percorsoMigliore.size()) {
			this.percorsoMigliore = new ArrayList<>(parziale);
			pesoMigliore = peso;
		}
	}
	
	public double getPesoMigliore() {
		return this.pesoMigliore;
	}
}
