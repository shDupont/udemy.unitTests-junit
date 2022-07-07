package br.ce.dupont.servicos;

import br.ce.dupont.entidades.Usuario;

public interface SPCService {

	boolean possuiNegativacao(Usuario usuario) throws Exception;
}
