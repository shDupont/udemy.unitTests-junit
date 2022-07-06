package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.servicos.matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {
    private LocacaoService service;
    private static int contador =0;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        service = new LocacaoService();

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
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

        Locacao locacao = service.alugarFilme(usuario, filmes);

        error.checkThat(locacao.getValor(), is(equalTo(5.0)));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));

        Locacao locacao = service.alugarFilme(usuario, filmes);
    }

    @Test
    public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException {
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

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
        Usuario usuario = new Usuario("Usuario 1");

        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        Locacao locacao = service.alugarFilme(usuario, null);

        System.out.println("Forma nova");
    }

    @Test
    public void devePagar75NoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(11.0));
    }
    @Test
    public void devePagar50NoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 4.0),
                new Filme("Filme 2", 1, 4.0),
                new Filme("Filme 3", 1, 4.0),
                new Filme("Filme 4", 1, 4.0));
        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25NoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");
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
        Usuario usuario = new Usuario("Usuario 1");
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
        Usuario usuario = new Usuario("Usuario 1");
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
}
