package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.CoppiaArtisti;
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
    	txtResult.appendText("Calcola artisti connessi");
    	
    	List<CoppiaArtisti> coppie= this.model.getCoppie(); 
    	
    	if(coppie==null) {
    		txtResult.appendText("ERRORE, va creato prima il grafo!\n");
    		return; 
    	}
    	 //ci sono
    	Collections.sort(coppie);
    	for (CoppiaArtisti c : coppie) {
    		txtResult.appendText(String.format("(%d, %d) = %d\n",c.getId1(),c.getId2(),c.getPeso()));
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    	Integer id; 
    	try {
    	  id= Integer.parseInt(this.txtArtista.getText()); 
    	}
    	catch(NumberFormatException ne) {
    		txtResult.appendText("ERRORE Inserire un id nel formato corretto\n");
    		return; 
    		
    	}
    	
    	if(!this.model.contiene(id)) {
    		txtResult.appendText("Artista non presente nel grafo!! \n");
    		return; 
    		}
    	
    	List<Integer> percorso= this.model.trovaPercorso(id); 
    	txtResult.appendText("Percorso piu' lungo : "+percorso.size()+" vertici \n \n");
    	for(Integer i : percorso) {
    		txtResult.appendText(i+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo\n\n");
    	
    	String ruolo=this.boxRuolo.getValue(); 
    	
    	if (ruolo==null) {
    		txtResult.appendText("Errore! Selezionare un ruolo! \n");
    		return; 
    	}
    	
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.getNVertex(), this.model.getNArchi()));
    
    	this.btnCalcolaPercorso.setDisable(false); // abilito il calcolo del percorso post creazione del grafo
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRuoli()); 
    	this.btnCalcolaPercorso.setDisable(true); // lo disabilito finche' non si crea il grafo 
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
