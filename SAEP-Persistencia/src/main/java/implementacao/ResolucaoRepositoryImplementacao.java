package implementacao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import modelo.ListaResolucao;
import modelo.ListaTipo;

public class ResolucaoRepositoryImplementacao implements ResolucaoRepository {

	private File resolucaoFile = new File("storage/Resolucao.xml");

	private File tipoFile = new File("storage/Tipo.xml");

	public Resolucao byId(String id) {

		Resolucao retorno = null;

		if (resolucaoFile.exists()) {

			XStream stream = new XStream();

			ListaResolucao resolucoes = (ListaResolucao) stream.fromXML(resolucaoFile);

			for (Resolucao resolucao : resolucoes.getListaResolucao()) {
				if (resolucao.getId().equals(id)) {
					retorno = resolucao;
				}
			}
		}

		return retorno;
	}

	public String persiste(Resolucao resolucao) {

		resolucaoFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());

		ListaResolucao resolucoes = new ListaResolucao();

		try {
			if (resolucaoFile.exists()) {
				resolucoes = (ListaResolucao) stream.fromXML(resolucaoFile);
			} else {
				resolucaoFile.createNewFile();
				resolucoes.setListaResolucao(new ArrayList<Resolucao>());
			}

			for (Resolucao resol : resolucoes.getListaResolucao()) {
				if (resol.getId().equals(resolucao.getId())) {
					return null;
				}
			}

			resolucoes.getListaResolucao().add(resolucao);

			final OutputStream out = new FileOutputStream(resolucaoFile);

			stream.toXML(resolucoes, out);
		} catch (IOException e) {
		}

		return resolucao.getId();
	}

	public void persisteTipo(Tipo tipo) {

		tipoFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());

		ListaTipo tipos = new ListaTipo();

		try {
			if (tipoFile.exists()) {
				tipos = (ListaTipo) stream.fromXML(tipoFile);
			} else {
				tipoFile.createNewFile();
				tipos.setListaTipo(new ArrayList<Tipo>());
			}

			for (Tipo tip : tipos.getListaTipo()) {
				if (tip.getId().equals(tipo.getId())) {
					return;
				}
			}

			tipos.getListaTipo().add(tipo);

			final OutputStream out = new FileOutputStream(tipoFile);

			stream.toXML(tipos, out);
		} catch (IOException e) {
		}

	}

	public boolean remove(String id) {

		boolean executado = false;

		if (resolucaoFile.exists()) {

			Resolucao resolucaoRemover = null;

			try {

				XStream stream = new XStream();

				ListaResolucao resolucoes = (ListaResolucao) stream.fromXML(resolucaoFile);

				for (Resolucao resolucao : resolucoes.getListaResolucao()) {
					if (resolucao.getId().equals(id)) {
						resolucaoRemover = resolucao;
					}
				}

				if (resolucaoRemover != null) {
					resolucoes.getListaResolucao().remove(resolucaoRemover);

					final OutputStream out = new FileOutputStream(resolucaoFile);

					stream.toXML(resolucoes, out);

					executado = true;

				}

			} catch (Exception e) {
			}
		}
		return executado;
	}

	public void removeTipo(String codigo) {

		if (tipoFile.exists()) {

			Tipo tipoRemover = null;

			try {

				if (tipoFile.exists()) {

					XStream stream = new XStream();

					ListaTipo tipos = (ListaTipo) stream.fromXML(tipoFile);

					for (Tipo tipo : tipos.getListaTipo()) {
						if (tipo.getId().equals(codigo)) {
							tipoRemover = tipo;
						}
					}

					if (tipoRemover != null) {
						tipos.getListaTipo().remove(tipoRemover);

						final OutputStream out = new FileOutputStream(tipoFile);

						stream.toXML(tipos, out);
					}
				}

			} catch (Exception e) {
			}
		}
	}

	public List<String> resolucoes() {
		final List<String> listaId = new ArrayList<String>();

		if (resolucaoFile.exists()) {

			XStream stream = new XStream();

			ListaResolucao resolucoes = (ListaResolucao) stream.fromXML(resolucaoFile);

			for (Resolucao resolucao : resolucoes.getListaResolucao()) {
				listaId.add(resolucao.getId());
			}
		}

		return listaId;
	}

	public Tipo tipoPeloCodigo(String codigo) {
		Tipo tipoRetorno = null;

		if (tipoFile.exists()) {

			XStream stream = new XStream();

			ListaTipo tipos = (ListaTipo) stream.fromXML(tipoFile);

			for (Tipo tipo : tipos.getListaTipo()) {
				if (tipo.getId().equals(codigo)) {
					tipoRetorno = tipo;
					break;
				}
			}
		}
		return tipoRetorno;
	}

	public List<Tipo> tiposPeloNome(String nome) {
		final List<Tipo> listaTipo = new ArrayList<Tipo>();

		if (tipoFile.exists()) {

			XStream stream = new XStream();

			ListaTipo tipos = (ListaTipo) stream.fromXML(tipoFile);

			for (Tipo tipo : tipos.getListaTipo()) {
				if (tipo.getNome().equals(nome)) {
					listaTipo.add(tipo);
				}
			}
		}
		return listaTipo;
	}

}