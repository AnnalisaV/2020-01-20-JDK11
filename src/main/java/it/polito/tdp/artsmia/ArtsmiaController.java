package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.CoupleArtists;
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
    	
       txtResult.appendText("Coppie di Artisti che hanno esibito contemporaneamente :\n\n");
    	for (CoupleArtists c : this.model.getCoppie()) {
    		txtResult.appendText(c.toString()+"\n");
    		
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	int id=0; 
    	if(this.txtArtista.getText().length()==0) {
    		txtResult.appendText("ERRORE : Inserire ID ARTISTA \n");
    		return; 
    	}
    	try {
    		id= Integer.parseInt(this.txtArtista.getText()); 
    	}catch(NumberFormatException nfe) {
    		txtResult.appendText("ERRORE : Inserire ID ARTISTA numerico \n");
    		return; 
    		
    	}
    	if(this.model.artistaNelGrafo(id)==false) {
    		txtResult.appendText("ERRORE : Artista non presente");
    		return; 
    	}
    	List<Artist> percorso= this.model.percorso(id);
    	
    	for (Artist a : percorso) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	if (this.boxRuolo.getValue()==null) {
    		txtResult.appendText("ERRORE : Selezionare un ruolo!\n");
    		return; 
    	}
    	this.model.creaGrafo(this.boxRuolo.getValue());
    	txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi", this.model.nVertex(), this.model.nArchi()));
    
    	//abilito il btn di Connessi e il calcolo del percorso
    	this.btnArtistiConnessi.setDisable(false);
    	this.btnCalcolaPercorso.setDisable(false);
    }

    public void setModel(Model model) {
    	this.model = model;
    	this.boxRuolo.getItems().addAll(this.model.getRoles());
    	this.btnArtistiConnessi.setDisable(true);
    	this.btnCalcolaPercorso.setDisable(true);
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
