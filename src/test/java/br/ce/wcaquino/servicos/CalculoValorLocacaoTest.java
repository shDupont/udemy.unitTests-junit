package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String str;

    private LocacaoService service;

    @Before
    public void setup(){
        service = new LocacaoService();
    }

    private static Filme filme1 = new Filme("Filme 1", 2, 4.0);

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1), 4.0, "1 Filme"},
                {Arrays.asList(filme1, filme1,filme1), 11.0, "3 Filmes"},
                {Arrays.asList(filme1, filme1,filme1,filme1), 13.0, "4 Filmes"},
                {Arrays.asList(filme1, filme1,filme1,filme1,filme1), 14.0, "5 Filmes"},
                {Arrays.asList(filme1, filme1,filme1,filme1,filme1,filme1), 14.0, "6 Filmes"},
                {Arrays.asList(filme1, filme1,filme1,filme1,filme1,filme1,filme1), 18.0, "7 Filmes"},
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
        Usuario usuario = new Usuario("Usuario 1");

        Locacao resultado = service.alugarFilme(usuario, filmes);

        assertThat(resultado.getValor(), is(valorLocacao));
    }

    @Test
    public void print(){
        System.out.println(valorLocacao);
    }
}
