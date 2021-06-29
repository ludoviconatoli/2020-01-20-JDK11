package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	
    	String ruolo = this.boxRuolo.getValue();
    	if(ruolo == null) {
    		this.txtResult.setText("Prima seleziona un ruoloe crea il grafo");
    		return;
    	}
    
    	if(model.getNVertici() == 0) {
    		this.txtResult.setText("Prima crea il grafo");
    		return;
    	}
    	
    	this.txtResult.appendText("Archi del grafo: \n\n");
    	List<Adiacenza> res = model.getArchi(ruolo);
    	if(res != null) {
    		for(Adiacenza a: res) {
        		this.txtResult.appendText(a.toString() + "\n");
        	}
    	}else {
    		this.txtResult.appendText("Non ci sono archi nel grafo!");
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String ruolo = this.boxRuolo.getValue();
    	if(ruolo == null) {
    		this.txtResult.setText("Prima seleziona un ruolo");
    		return;
    	}
    	
    	model.creaGrafo(ruolo);
    	txtResult.appendText("Creato grafo\n\n");
    	this.txtResult.appendText("#vertici: " + model.getNVertici() + "\n");
    	txtResult.appendText("#archi: " + model.getNArchi());
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(model.getRuoli());
    	this.txtResult.setStyle("-fx-font-family: monospace");
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
