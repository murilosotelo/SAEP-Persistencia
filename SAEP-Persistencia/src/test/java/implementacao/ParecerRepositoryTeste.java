package implementacao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import implementacao.ParecerRepositoryImplementacao;

/**
 * Classe de testes para {@link ParecerRepositoryImplementacao}
 *
 */
public class ParecerRepositoryTeste {

	private ParecerRepository parecerRepository;

	@Before
	public void setUp() {
		this.parecerRepository = new ParecerRepositoryImplementacao();
	}

	@Test
	public void adicionaNota() {
		Avaliavel origem = new Pontuacao("origem", new Valor("origem"));
		Avaliavel destino = new Pontuacao("destino", new Valor("destino"));

		Nota nota = new Nota(origem, destino, "erro");

		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.adicionaNota(parecer.getId(), nota);

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertTrue(parecerRecuperado.getNotas().size() > 0);
	}

	@Test
	public void testeRemoveNota() {
		Avaliavel origem = new Pontuacao("origem", new Valor("o1"));
		Avaliavel destino = new Pontuacao("destino", new Valor("o2"));

		Nota nota = new Nota(origem, destino, "erro");

		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.removeNota(parecer.getId(), nota.getItemOriginal());

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertTrue(parecerRecuperado.getNotas().size() == 0);
	}

	@Test
	public void testePersisteParecer() {

		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotNull(parecerRecuperado);
	}

	@Test(expected = IdentificadorExistente.class)
	public void testePersisteParecerJaExistente() {

		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.persisteParecer(parecer);

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotNull(parecerRecuperado);
	}

	@Test
	public void testeAtualizaFundamentacao() {
		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.atualizaFundamentacao(parecer.getId(), "fundamenta��oAlterada");

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotEquals(parecer.getFundamentacao(), parecerRecuperado.getFundamentacao());
	}

	@Test
	public void testeById() {
		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotNull(parecerRecuperado);
	}

	@Test
	public void testeRemoveParecer() {
		final Parecer parecer = this.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.removeParecer(parecer.getId());

		final Parecer parecerRecuperado = this.parecerRepository.byId(parecer.getId());

		Assert.assertNull(parecerRecuperado);

	}

	@Test
	public void testeRadocById() {
		final Radoc radoc = this.novoRadoc();

		this.parecerRepository.persisteRadoc(radoc);

		final Radoc radocRecuperado = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNotNull(radocRecuperado);
	}

	@Test
	public void testePersisteRadoc() {
		final Radoc radoc = this.novoRadoc();

		this.parecerRepository.persisteRadoc(radoc);

		final Radoc radocRecuperado = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNotNull(radocRecuperado);
	}

	@Test
	public void testeRemoveRadoc() {
		final Radoc radoc = this.novoRadoc();

		this.parecerRepository.persisteRadoc(radoc);

		this.parecerRepository.removeRadoc(radoc.getId());

		final Radoc radocRecuperado = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNull(radocRecuperado);
	}

	@Test(expected = ExisteParecerReferenciandoRadoc.class)
	public void testeRemoveRadocReferenciado() {

		final Parecer parecer = this.novoParecer();
		this.parecerRepository.persisteParecer(parecer);
		parecer.getRadocs();

		Radoc radoc = null;
		for (String id : parecer.getRadocs()) {
			radoc = this.novoRadocComIdParametro(id);
			this.parecerRepository.persisteRadoc(radoc);
		}

		this.parecerRepository.removeRadoc(radoc.getId());

		final Radoc radocRecuperado = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNull(radocRecuperado);
	}

	/**
	 * Método responsável por criar um novo parecer.
	 */
	public Parecer novoParecer() {

		final Random rd = new Random();

		final List<String> listaRadocs = new ArrayList<String>();
		listaRadocs.add(String.valueOf(rd.nextInt(50000)));
		listaRadocs.add(String.valueOf(rd.nextInt(50000)));

		final List<Pontuacao> listaPontuacao = new ArrayList<Pontuacao>();

		final Valor valor = new Valor(Float.valueOf("355"));

		final Pontuacao pontuacao = new Pontuacao("pontua��o1s", valor);

		listaPontuacao.add(pontuacao);

		final List<Nota> listaNota = new ArrayList<Nota>();

		Avaliavel origem = new Pontuacao("origem", new Valor("origem"));
		Avaliavel destino = new Pontuacao("destino", new Valor("destino"));

		Nota nota = new Nota(origem, destino, "erro");

		listaNota.add(nota);

		Parecer parecer = new Parecer(String.valueOf(rd.nextInt(1000000)), "NomeId", listaRadocs, listaPontuacao,
				"fundamenta��o", listaNota);

		return parecer;
	}

	/**
	 * Método responsável por criar um novo radoc.
	 */
	public Radoc novoRadoc() {

		Random rd = new Random();

		Map<String, Valor> valores = new HashMap<String, Valor>(1);
		valores.put("ano", new Valor(2016));

		final List<Relato> relatos = new ArrayList<Relato>();

		relatos.add(new Relato("relato", valores));

		Radoc radoc = new Radoc(String.valueOf(rd.nextInt(100000)), 0, relatos);

		return radoc;
	}

	/**
	 * Método responsável por criar um novo radoc com id fixo.
	 */
	public Radoc novoRadocComIdParametro(String id) {

		Map<String, Valor> valores = new HashMap<String, Valor>(1);
		valores.put("ano", new Valor(2016));

		final List<Relato> relatos = new ArrayList<Relato>();

		relatos.add(new Relato("relato", valores));

		Radoc radoc = new Radoc(id, 0, relatos);

		return radoc;
	}
}
