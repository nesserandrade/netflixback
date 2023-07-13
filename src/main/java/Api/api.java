/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Api;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.reflect.TypeToken;
import models.Erro;
import models.FiltroIdade;
import models.RequisicaoAPI;
import models.Usuario;

/**
 *
 * @author gutol
 */
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

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = request.getReader();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();

        String requestBody = stringBuilder.toString();

        Gson gson = new Gson();
        RequisicaoAPI requisicao = gson.fromJson(requestBody, RequisicaoAPI.class);

        //HttpSession session = request.getSession();  //não encontra
        HttpSession session = (HttpSession) getServletContext().getAttribute(requisicao.getSessionID());
        session.setAttribute("path", requisicao);
        if (session != null && session.getAttribute("path") != null) {
            RequisicaoAPI requisicaoSessao = (RequisicaoAPI) session.getAttribute("path");

            String API_DNS = "https://api.themoviedb.org/3";
            String caminho = requisicaoSessao.getPath();

            URL url = new URL(API_DNS + caminho);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseAPI = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    responseAPI.append(inputLine);
                }
                in.close();

                String apiResponse = responseAPI.toString();

                int idade = Integer.valueOf(requisicaoSessao.getIdade());
                System.out.println(idade);
                if(idade < 18) {

                    Type type = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> jsonMap = gson.fromJson(apiResponse, type);

                    List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");

                    List<FiltroIdade> filtros = results.stream()
                            .map(result -> gson.fromJson(gson.toJson(result), FiltroIdade.class))
                            .collect(Collectors.toList());

                    List<FiltroIdade> filteredShows = filtros.stream()
                            .filter(filtro -> !Boolean.valueOf(filtro.getAdult()))
                            .collect(Collectors.toList());

                    jsonMap.put("results", filteredShows);

                    String filteredJson = gson.toJson(jsonMap);

                    response.getWriter().println(filteredJson);

                }
                else if (idade >= 18) {
                    Type type = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> jsonMap = gson.fromJson(apiResponse, type);

                    List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");

                    List<FiltroIdade> filtros = results.stream()
                            .map(result -> gson.fromJson(gson.toJson(result), FiltroIdade.class))
                            .collect(Collectors.toList());

                    List<FiltroIdade> filteredShows = filtros.stream()
                            .collect(Collectors.toList());

                    jsonMap.put("results", filteredShows);

                    String Json = gson.toJson(jsonMap);

                    response.getWriter().println(Json);
                }

            } else {
                Erro erro = new Erro();
                erro.setDescricao("Falha na API!");
                erro.setCodigo("001");

                String json = gson.toJson(erro);
                response.getWriter().println(json);

            }

        }


    }
}
