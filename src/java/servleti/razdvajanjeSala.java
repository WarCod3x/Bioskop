/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servleti;

import binovi.Bioskop;
import binovi.Korisnik;
import binovi.Sala;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class razdvajanjeSala extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String akcija = request.getParameter("akcija");
        String sala = request.getParameter("idSale");
        int idSale = Integer.parseInt(sala);
        
        if("Izmeni".equals(akcija))
        {
            String dbUrl = "jdbc:mysql://localhost:3306/bioskop";
            String user = "root";
            String pass = "";
            
            try 
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(dbUrl,user,pass);
                Statement st = con.createStatement();
                ResultSet rs = null;
                ArrayList<Bioskop> listaBioskopa = new ArrayList<>();
                
                String upitBioskop = "SELECT * FROM bioskop";
                
                rs =st.executeQuery(upitBioskop);
                
                while(rs.next())
                {
                    listaBioskopa.add(new Bioskop(rs.getInt("id"), rs.getString("naziv"), rs.getString("adresa"), rs.getString("telefon"), 
                            rs.getString("opis"), rs.getString("slika"), rs.getInt("menadzerID")));
                }
                
                String upit = "SELECT * FROM sale WHERE id = '" + idSale + "'";
                
                rs = st.executeQuery(upit);
                if(rs.next())
                {
                    Sala salaIzmena = new Sala(idSale, rs.getInt("idBioskopa"), rs.getString("opis"), rs.getInt("brojMesta"), 
                            rs.getInt("brojSlobodnihMesta"));
                    
                    request.setAttribute("listaBioskopa", listaBioskopa);
                    request.setAttribute("sala", salaIzmena);
                    request.getRequestDispatcher("izmeniSalu.jsp").forward(request, response);
                }
                
                rs.close();
                st.close();
                con.close();
            } 
            catch (IOException | ClassNotFoundException | SQLException | ServletException e) 
            {
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("izmeniSalu.jsp").forward(request, response);
            }
            
            request.setAttribute("idSale", idSale);
            request.getRequestDispatcher("izmeniSalu.jsp").forward(request, response);
        }
        else if ("Obrisi".equals(akcija))
        {
            String dbUrl = "jdbc:mysql://localhost:3306/bioskop";
            String user = "root";
            String pass = "";
            
            try 
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(dbUrl,user,pass);
                Statement st = con.createStatement();
                PreparedStatement ps;
                
                String upitSala = "DELETE FROM sale WHERE id = ?";
                
                ps = con.prepareStatement(upitSala);
                ps.setInt(1, idSale);
                
                try 
                {
                ps.executeUpdate();
                } 
                catch (SQLException e) 
                {
                    request.setAttribute("msg", e.getMessage());
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
                
                request.setAttribute("msg", "Obrisali ste salu.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } 
            catch (IOException | ClassNotFoundException | SQLException | ServletException e) 
            {
                request.setAttribute("msg", e.getMessage());
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
