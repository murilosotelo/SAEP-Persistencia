package modelo;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

/**
 * Classe utilizada para agrupar as entidades Tipo.
 *
 */
public class ListaTipo {

	private List<Tipo> listaTipo;

	public List<Tipo> getListaTipo() {
		if (this.listaTipo == null) {
			this.listaTipo = new ArrayList<Tipo>();
		}
		return listaTipo;
	}

	public void setListaTipo(List<Tipo> listaTipo) {
		this.listaTipo = listaTipo;
	}

}
