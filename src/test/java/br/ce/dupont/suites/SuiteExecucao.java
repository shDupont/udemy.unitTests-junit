package br.ce.dupont.suites;

import org.junit.runners.Suite.SuiteClasses;

import br.ce.dupont.servicos.CalculadoraTest;
import br.ce.dupont.servicos.CalculoValorLocacaoTest;
import br.ce.dupont.LocacaoServiceTest;

//@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {
	//Remova se puder!
}
