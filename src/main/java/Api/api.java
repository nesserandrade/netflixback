package Api;

import com.google.gson.Gson;
import models.Erro;
import models.Usuario;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class api extends HttpServlet {


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

        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(request.getReader(), Usuario.class);

        System.out.println("Teste: ");
        System.out.println(request.getReader());
        System.out.println(usuario.getSessionID());

        //HttpSession session = request.getSession();  //não encontra
        HttpSession session = (HttpSession) getServletContext().getAttribute(usuario.getSessionID());

        if(session != null && session.getAttribute("usuario") != null){
            usuario =  (Usuario) session.getAttribute("usuario");
            String json = gson.toJson(usuario);

            response.getWriter().println(json);

        } else {
            Erro erro = new Erro();
            erro.setDescricao("Login Não Realizado!");
            erro.setCodigo("001");

            String json = gson.toJson(erro);
            response.getWriter().println(json);

        }

    }



}
