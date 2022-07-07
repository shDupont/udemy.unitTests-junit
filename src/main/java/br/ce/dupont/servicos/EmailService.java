package br.ce.dupont.servicos;


import br.ce.dupont.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}
