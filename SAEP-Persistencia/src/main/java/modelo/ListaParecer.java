package modelo;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Parecer;

/**
 * Classe utilizada para agrupar as entidades Parecer.
 *
 */
public class ListaParecer {

	private List<Parecer> listaParecer;

	public List<Parecer> getListaParecer() {
		if (this.listaParecer == null) {
			this.listaParecer = new ArrayList<Parecer>();
		}
		return listaParecer;
	}

	public void setListaParecer(List<Parecer> listaParecer) {
		this.listaParecer = listaParecer;
	}
}
