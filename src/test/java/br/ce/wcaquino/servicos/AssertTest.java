package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;
import org.junit.Assert;
import org.junit.Test;

public class AssertTest {
    @Test
    public void test(){
        Assert.assertTrue(true);
        Assert.assertEquals("Erro",1, 1);

        Usuario u1 = new Usuario("usuario 1");
        Usuario u2 = new Usuario("usuario 1");


    }
}
