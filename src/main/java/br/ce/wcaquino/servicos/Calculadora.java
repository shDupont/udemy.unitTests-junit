package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDivPorZeroException;

public class Calculadora {
    public int somar(int a, int b){
        return a + b;
    }

    public int subtracao(int a, int b) {
        return a - b;
    }

    public int div(int a, int b) throws NaoPodeDivPorZeroException {
        if(b == 0){
            throw new NaoPodeDivPorZeroException();
        }
        return a / b;
    }
}
