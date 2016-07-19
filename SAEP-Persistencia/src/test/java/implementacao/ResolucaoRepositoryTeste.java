package implementacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import implementacao.ResolucaoRepositoryImplementacao;

/**
 * Classe de testes da classe ResolucaoRespositoryImpl
 *
 */
public class ResolucaoRepositoryTeste {

	private ResolucaoRepository resolucaoRepository;

	@Before
	public void setUp() {
		this.resolucaoRepository = new ResolucaoRepositoryImplementacao();
	}

	@Test
	public void testeById() {
		String idSalvo = null;
		do {

			idSalvo = this.resolucaoRepository.persiste(this.novaResolucao());
		} while (idSalvo == null);
		final Resolucao resolucao = this.resolucaoRepository.byId(idSalvo);

		Assert.assertNotNull(resolucao);
	}

	@Test
	public void testePersiste() {

		final String idSalvo = this.resolucaoRepository.persiste(this.novaResolucao());

		Assert.assertNotNull(idSalvo);
	}

	@Test
	public void testeRemove() {

		final String id = this.resolucaoRepository.persiste(this.novaResolucao());

		final Boolean removido = this.resolucaoRepository.remove(id);

		Assert.assertTrue(removido);
	}

	@Test
	public void testeResolucoes() {

		final String id = this.resolucaoRepository.persiste(this.novaResolucao());

		final List<String> listaIds = this.resolucaoRepository.resolucoes();

		Assert.assertTrue(listaIds.contains(id));
	}

	@Test
	public void testePersisteTipo() {
		final Tipo tipo = this.novoTipo();

		this.resolucaoRepository.persisteTipo(tipo);

		final Tipo tipoRecuperado = this.resolucaoRepository.tipoPeloCodigo(tipo.getId());

		Assert.assertNotNull(tipoRecuperado);
	}

	@Test
	public void testeRemoveTipo() {

		final Tipo tipo = this.novoTipo();

		this.resolucaoRepository.persisteTipo(tipo);

		this.resolucaoRepository.removeTipo(tipo.getId());

		final Tipo tipoRecuperado = this.resolucaoRepository.tipoPeloCodigo(tipo.getId());

		Assert.assertNull(tipoRecuperado);
	}

	@Test
	public void testeTipoPeloCodigo() {
		final Tipo tipo = this.novoTipo();

		this.resolucaoRepository.persisteTipo(tipo);

		final Tipo tipoRecuperado = this.resolucaoRepository.tipoPeloCodigo(tipo.getId());

		Assert.assertNotNull(tipoRecuperado);
	}

	@Test
	public void testeTiposPeloNome() {
		final Tipo tipo = this.novoTipo();

		this.resolucaoRepository.persisteTipo(tipo);

		final List<Tipo> listaTipo = this.resolucaoRepository.tiposPeloNome(tipo.getNome());

		Assert.assertFalse(listaTipo.isEmpty());
	}

	/**
	 * Cria uma nova resolução.
	 */
	public Resolucao novaResolucao() {

		Random rd = new Random();

		List<Regra> regras = new ArrayList<Regra>();
		List<String> dd = new ArrayList<String>(1);
		dd.add("a");

		Regra regra = new Regra("var", 1, "desc", (float) 1.1, (float) 1.1, "exp", "entao", "senao", "tipoRelato",
				(float) 1.1, dd);
		regras.add(regra);

		Resolucao resolucao = new Resolucao(String.valueOf(rd.nextInt(1000000)), "nome", "descricao", new Date(),
				regras);

		return resolucao;
	}

	/**
	 * Cria uma nova resolução com um id fixo.
	 */
	public Resolucao novaResolucaoComIdFixo(String id) {
		List<Regra> regras = new ArrayList<Regra>();
		List<String> dd = new ArrayList<String>(1);
		dd.add("a");

		Regra regra = new Regra("var", 1, "desc", (float) 1.1, (float) 1.1, "exp", "entao", "senao", "tipoRelato",
				(float) 1.1, dd);
		regras.add(regra);

		Resolucao resolucao = new Resolucao(id, "nome", "descricao", new Date(), regras);

		return resolucao;
	}

	/**
	 * Cria um novo tipo.
	 */
	private Tipo novoTipo() {

		final Random rd = new Random();

		final Set<Atributo> lista = new HashSet<Atributo>();

		final Atributo atributo = new Atributo("nomeAtributo", "descAtributo", 1);

		lista.add(atributo);

		Tipo tipo = new Tipo(String.valueOf(rd.nextInt(100000)), "nomeTipo", "desc", lista);

		return tipo;
	}

}
