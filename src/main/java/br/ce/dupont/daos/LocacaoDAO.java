package br.ce.dupont.daos;

import java.util.List;

import br.ce.dupont.entidades.Locacao;

public interface LocacaoDAO {

	void salvar(Locacao locacao);

	List<Locacao> obterLocacoesPendentes();
}
