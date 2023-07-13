package models;

import interfaces.ModelFactory;

public class UsuarioFactory implements ModelFactory {
    @Override
    public Usuario createUsuario() {
        return new Usuario();
    }
}
