package implementacao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorDesconhecido;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import modelo.ListaParecer;
import modelo.ListaRadoc;

public class ParecerRepositoryImplementacao implements ParecerRepository {
	private static final String IDENTIFICADOR = "id";

	Logger log = Logger.getLogger(ParecerRepositoryImplementacao.class.getName());

	private File parecerFile = new File("storage/Parecer.xml");

	private File radocFile = new File("storage/Radoc.xml");

	public void adicionaNota(String parecer, Nota nota) {

		if (parecerFile.exists()) {

			try {
				XStream stream = new XStream();
				stream.alias("listaParecer", ListaParecer.class);
				stream.alias("parecer", Parecer.class);
				stream.alias("nota", Nota.class);
				stream.alias("pontuacao", Pontuacao.class);

				ListaParecer pareceres = (ListaParecer) stream.fromXML(parecerFile);

				for (Parecer parecerSalvo : pareceres.getListaParecer()) {
					if (parecerSalvo.getId().equals(parecer)) {

						parecerSalvo.getNotas().add(nota);
						break;
					}
				}

				OutputStream out = new FileOutputStream(parecerFile);
				stream.toXML(pareceres, out);

			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	public void removeNota(String id, Avaliavel original) {

		Nota notaRemover = null;

		if (parecerFile.exists()) {

			try {
				XStream stream = new XStream();
				stream.alias("listaParecer", ListaParecer.class);
				stream.alias("parecer", Parecer.class);
				stream.alias("nota", Nota.class);
				stream.alias("pontuacao", Pontuacao.class);

				ListaParecer pareceres = (ListaParecer) stream.fromXML(parecerFile);

				for (Parecer parecerSalvo : pareceres.getListaParecer()) {
					if (parecerSalvo.getId().equals(id)) {

						for (Nota nota : parecerSalvo.getNotas()) {
							if (((Pontuacao) nota.getItemOriginal()).getAtributo()
									.equals(((Pontuacao) original).getAtributo())) {
								notaRemover = nota;
								break;
							}
						}

						parecerSalvo.getNotas().remove(notaRemover);
						break;
					}
				}

				OutputStream out = new FileOutputStream(parecerFile);
				stream.toXML(pareceres, out);

			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

	}

	public void persisteParecer(Parecer parecer) {
		parecerFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());
		stream.alias("listaParecer", ListaParecer.class);
		stream.alias("parecer", Parecer.class);
		stream.alias("nota", Nota.class);
		stream.alias("pontuacao", Pontuacao.class);

		ListaParecer pareceres = new ListaParecer();

		try {
			if (parecerFile.exists()) {
				pareceres = (ListaParecer) stream.fromXML(parecerFile);
			} else {
				parecerFile.createNewFile();
				pareceres.setListaParecer(new ArrayList<Parecer>());
			}

			for (Parecer parec : pareceres.getListaParecer()) {
				if (parec.getId().equals(parecer.getId())) {
					throw new IdentificadorExistente(ParecerRepositoryImplementacao.IDENTIFICADOR);
				}
			}

			pareceres.getListaParecer().add(parecer);

			final OutputStream out = new FileOutputStream(parecerFile);

			stream.toXML(pareceres, out);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public void atualizaFundamentacao(String parecer, String fundamentacao) {

		Parecer parecerRemover = null;
		Parecer parecerAtualizado = null;

		if (parecerFile.exists()) {

			try {
				XStream stream = new XStream();
				stream.alias("listaParecer", ListaParecer.class);
				stream.alias("parecer", Parecer.class);
				stream.alias("nota", Nota.class);
				stream.alias("pontuacao", Pontuacao.class);

				ListaParecer pareceres = (ListaParecer) stream.fromXML(parecerFile);

				for (Parecer parecerSalvo : pareceres.getListaParecer()) {
					if (parecerSalvo.getId().equals(parecer)) {
						parecerRemover = parecerSalvo;
						parecerAtualizado = new Parecer(parecerSalvo.getId(), parecerSalvo.getResolucao(),
								parecerSalvo.getRadocs(), parecerSalvo.getPontuacoes(), fundamentacao,
								parecerSalvo.getNotas());
						break;
					}
				}

				if (parecerAtualizado == null) {
					throw new IdentificadorDesconhecido("Identificador do parecer n�o localizado.");
				}
				pareceres.getListaParecer().remove(parecerRemover);
				pareceres.getListaParecer().add(parecerAtualizado);

				OutputStream out = new FileOutputStream(parecerFile);
				stream.toXML(pareceres, out);
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		}

	}

	public Parecer byId(String id) {
		Parecer retorno = null;

		if (parecerFile.exists()) {

			XStream stream = new XStream();
			stream.alias("listaParecer", ListaParecer.class);
			stream.alias("parecer", Parecer.class);
			stream.alias("nota", Nota.class);
			stream.alias("pontuacao", Pontuacao.class);
			ListaParecer pareceres = (ListaParecer) stream.fromXML(parecerFile);

			for (Parecer parecer : pareceres.getListaParecer()) {
				if (parecer.getId().equals(id)) {
					retorno = parecer;
				}
			}
		}
		return retorno;
	}

	private Boolean existeParecerComRadoc(Radoc radoc) {
		Boolean retorno = false;

		if (parecerFile.exists()) {

			XStream stream = new XStream();
			stream.alias("listaParecer", ListaParecer.class);
			stream.alias("parecer", Parecer.class);
			stream.alias("nota", Nota.class);
			stream.alias("pontuacao", Pontuacao.class);

			ListaParecer pareceres = (ListaParecer) stream.fromXML(parecerFile);

			for (Parecer parecer : pareceres.getListaParecer()) {
				if (parecer.getRadocs().contains(radoc.getId())) {
					retorno = true;
					break;
				}
			}
		}
		return retorno;
	}

	public void removeParecer(String id) {

		if (parecerFile.exists()) {

			Parecer parecerRemover = null;
			try {

				XStream stream = new XStream();
				stream.alias("listaParecer", ListaParecer.class);
				stream.alias("parecer", Parecer.class);
				stream.alias("nota", Nota.class);
				stream.alias("pontuacao", Pontuacao.class);

				ListaParecer pareceres = (ListaParecer) stream.fromXML(parecerFile);

				for (Parecer parecer : pareceres.getListaParecer()) {
					if (parecer.getId().equals(id)) {
						parecerRemover = parecer;
						break;
					}
				}

				if (parecerRemover != null) {
					pareceres.getListaParecer().remove(parecerRemover);

					OutputStream out = new FileOutputStream(parecerFile);

					stream.toXML(pareceres, out);
				}
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		}
	}

	public Radoc radocById(String identificador) {
		Radoc retorno = null;

		if (radocFile.exists()) {

			XStream stream = new XStream();

			ListaRadoc radocs = (ListaRadoc) stream.fromXML(radocFile);

			for (Radoc radoc : radocs.getListaRadoc()) {
				if (radoc.getId().equals(identificador)) {
					retorno = radoc;
				}
			}
		}

		return retorno;
	}

	public String persisteRadoc(Radoc radoc) {
		radocFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());

		ListaRadoc radocs = new ListaRadoc();

		try {
			if (radocFile.exists()) {
				radocs = (ListaRadoc) stream.fromXML(radocFile);
			} else {
				radocFile.createNewFile();
				radocs.setListaRadoc(new ArrayList<Radoc>());
			}

			for (Radoc rado : radocs.getListaRadoc()) {
				if (rado.getId().equals(radoc.getId())) {
					throw new IdentificadorExistente("Identificadorr duplicado, n�o foi poss�vel persistir Radoc.");
				}
			}

			radocs.getListaRadoc().add(radoc);

			final OutputStream out = new FileOutputStream(radocFile);

			stream.toXML(radocs, out);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return radoc.getId();
	}

	public void removeRadoc(String identificador) {
		if (radocFile.exists()) {

			Radoc radocRemover = null;
			try {

				XStream stream = new XStream();

				ListaRadoc radocs = (ListaRadoc) stream.fromXML(radocFile);

				for (Radoc radoc : radocs.getListaRadoc()) {
					if (radoc.getId().equals(identificador)) {
						radocRemover = radoc;
						break;
					}
				}

				if (radocRemover != null && !existeParecerComRadoc(radocRemover)) {
					radocs.getListaRadoc().remove(radocRemover);

					OutputStream out;
					out = new FileOutputStream(radocFile);

					stream.toXML(radocs, out);
				} else {
					throw new ExisteParecerReferenciandoRadoc(
							"Radoc n�o pode ser excluido pois existe um parecer o referenciando.");
				}
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
}
