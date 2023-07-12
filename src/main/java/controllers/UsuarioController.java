package controllers;

import models.Usuario;

import java.util.ArrayList;

public class UsuarioController {
    public static ArrayList<Usuario> usuarios = createUsers();

    public UsuarioController(){}

    public static ArrayList<Usuario> createUsers(){
        ArrayList<Usuario> users = new ArrayList();

        Usuario u1 = new Usuario();
        u1.setNome("Nesser Andrade");
        u1.setEmail("nesser@teste.com");
        u1.setSenha("1234");
        u1.setIdade("33");

        users.add(u1);


        Usuario u2 = new Usuario();
        u2.setNome("Joao da Silva Sauro");
        u2.setEmail("joao@teste.com");
        u2.setSenha("1234");
        u2.setIdade("05");

        users.add(u2);

        return users;
    }

    public boolean login(Usuario user){

        boolean logado = false;

        for(Usuario u: UsuarioController.usuarios){

            if(u.getEmail().equals(user.getEmail()) && u.getSenha().equals(user.getSenha())){
                logado = true;

                //copiar os dados do meu banco de dados para o objeto de retorno do login
                user.setNome(u.getNome());
                user.setIdade(u.getIdade());

                break;
            }

        }

        return logado;
    }

}
