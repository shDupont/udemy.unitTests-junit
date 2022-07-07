package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.builders.FilmeBuilder;
import br.ce.wcaquino.servicos.builders.LocacaoBuilder;
import br.ce.wcaquino.servicos.builders.UsuarioBuilder;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.servicos.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.servicos.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.servicos.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.servicos.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {
    private LocacaoService service;
    private SPCService spc;
    private LocacaoDAO dao;
    private EmailService emailService;
    private static int contador =0;
    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        service = new LocacaoService();
        dao = Mockito.mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);

        emailService = Mockito.mock(EmailService.class);
        service.setEmailService(emailService);

        spc = Mockito.mock(SPCService.class);
        service.setSpcService(spc);

        //Zera o contador toda vez se a variavel contador não for static
        contador++;
        System.out.println(contador);
    }

    @After
    public void tearDown(){
        System.out.println("After");
    }

    @BeforeClass
    public static void setupClass(){
        System.out.println("Before Class");
    }

    @AfterClass
    public static void tearDownClass(){
        System.out.println("After Class");
    }

    @Test
    public void testeLocacao() throws Exception{
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        Locacao locacao = service.alugarFilme(usuario, filmes);

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        Locacao locacao = service.alugarFilme(usuario, filmes);
    }

    @Test
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        try {
            Locacao locacao = service.alugarFilme(null, filmes);
            Assert.fail();
        }  catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }

        System.out.println("Forma robusta");
    }

    @Test
    public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        Locacao locacao = service.alugarFilme(usuario, null);

        System.out.println("Forma nova");
    }

    @Test
    public void devePagar75NoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(11.0));
    }
    @Test
    public void devePagar50NoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25NoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0NoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0),
                new Filme("Filme 6", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void naoDeveDevolverFilmesNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0),
                new Filme("Filme 5", 1, 4.0),
                new Filme("Filme 6", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getDataRetorno(), caiEm(Calendar.MONDAY));
        assertThat(resultado.getDataRetorno(), caiNumaSegunda());
    }

    @Test
    public void naoDeveAlugarSPC() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        Mockito.when(spc.possiuNegativacao(usuario)).thenReturn(true);

        exception.expect(LocadoraException.class);
        exception.expectMessage("Usuario negativado");

        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas(){
        Usuario usuario = umUsuario().agora();
        //cenario
        List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umLocacao().comUsuario(usuario).comDataRetorno(DataUtils.obterDataComDiferencaDias(-2)).agora());
        Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
        //acao
        service.notificarAtrasos();

        //verificacao
        Mockito.verify(emailService).notificarAtraso(usuario);
    }
}
