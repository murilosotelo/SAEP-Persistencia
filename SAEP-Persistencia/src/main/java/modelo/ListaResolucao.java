/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */

package modelo;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;

/**
 * Classe utilizada para agrupar as entidades Resolucao.
 *
 */
public class ListaResolucao {

	private List<Resolucao> listaResolucao;

	public List<Resolucao> getListaResolucao() {
		if (this.listaResolucao == null) {
			this.listaResolucao = new ArrayList<Resolucao>();
		}
		return listaResolucao;
	}

	public void setListaResolucao(List<Resolucao> listaResolucao) {
		this.listaResolucao = listaResolucao;
	}

}
