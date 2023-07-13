package Api;

import com.google.gson.Gson;
import controllers.UsuarioController;
import interfaces.ModelFactory;
import models.Erro;
import models.Usuario;
import models.UsuarioFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class login extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //COnverte uma string em um objeto no formato indicado
        Gson gson = new Gson();

        UsuarioFactory usuarioFactory = new UsuarioFactory();
        Usuario user = gson.fromJson(request.getReader(), Usuario.class);


        UsuarioController ucontrol = new UsuarioController(usuarioFactory);

        if (ucontrol.login(user)) {
            //sinalizo que funcionou
            request.getSession().setAttribute("usuario", user);
            //colocando o id da seção num local de acesso global
            getServletContext().setAttribute(request.getSession().getId(), request.getSession());
            //preparando a resposa que levará o id da seção para o react do outro lado
            user.setSessionID(request.getSession().getId());
            user.setIdade(user.getIdade());

            String json = gson.toJson(user);

            response.getWriter().println(json);
        } else {
            //sinalizo que deu ruim
            request.getSession().removeAttribute("usuario");

            Erro erro = new Erro();
            erro.setDescricao("Login Não Realizado!");
            erro.setCodigo("001");

            String json = gson.toJson(erro);
            response.getWriter().println(json);
        }
    }
}