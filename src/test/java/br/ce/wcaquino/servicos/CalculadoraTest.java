package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDivPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup(){
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores(){
        //cenario
        int a = 5;
        int b = 3;
        //acao
        int resultado = calc.somar(a,b);

        //verificando
        Assert.assertEquals(8, resultado);

    }

    @Test
    public void deveSubtrairDoisValores(){
        int a = 8;
        int b = 5;

        int resultado = calc.subtracao(a,b);

        Assert.assertEquals(3, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDivPorZeroException{
        int a = 6;
        int b = 3;

        int resultado = calc.div(a,b);

        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDivPorZeroException.class)
    public void deveLancarExcecaoDivPorZero() throws NaoPodeDivPorZeroException {
        int a = 6;
        int b = 0;


        calc.div(a,b);
    }
}
