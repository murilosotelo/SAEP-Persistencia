package modelo;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Radoc;

/**
 * Classe utilizada para agrupar as entidades Radoc.
 *
 */
public class ListaRadoc {

	private List<Radoc> listaRadoc;

	public List<Radoc> getListaRadoc() {
		if (this.listaRadoc == null) {
			this.listaRadoc = new ArrayList<Radoc>();
		}
		return listaRadoc;
	}

	public void setListaRadoc(List<Radoc> listaRadc) {
		this.listaRadoc = listaRadc;
	}

}
