/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servleti;

import binovi.Korisnik;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class registracija extends HttpServlet {

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
        String email = (String)request.getParameter("email");
        String korisnickoIme = request.getParameter("korisnickoIme");
        String lozinka = request.getParameter("sifra");
        String potvrda = request.getParameter("potvrda");
        String telefon = request.getParameter("telefon");
        String tipKorisnika = "Klijent";
        int poeni = 0;
        
        String dbUrl = "jdbc:mysql://localhost:3306/bioskop";
        String user = "root";
        String pass = "";
        
        try 
        {
           Class.forName("com.mysql.jdbc.Driver");
           Connection con = DriverManager.getConnection(dbUrl,user,pass);
           Statement st = con.createStatement();
           ResultSet rs = null;
           String upit = "SELECT korisnickoIme, lozinka FROM korisnik";
           rs = st.executeQuery(upit);
           
           while(rs.next())
           {
               if(korisnickoIme.equals(rs.getString("korisnickoIme")))
               {
                   request.setAttribute("msg", "Korisnicko ime vec postoji. Molimo Vas izaberite drugo korisnicko ime.");
                   request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
               }
           }
           st.close();
           con.close();
        } 
        catch (ClassNotFoundException e) 
        {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
        }
        catch (SQLException se)
        {
            String poruka = se.getMessage();
            request.setAttribute("poruka", poruka);
            request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
        }
        
        if(korisnickoIme != null && korisnickoIme.length()>0 && email != null && email.length() > 0
                && lozinka != null && lozinka.length() > 0 && potvrda != null && potvrda.length() > 0
                && telefon != null && telefon.length() > 0)
        {
            if(lozinka.equals(potvrda))
            {
                try 
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(dbUrl,user,pass);
                    String upitZaUnosUBazu = "INSERT INTO korisnik(korisnickoIme,lozinka,email,telefon,poeni,tipKorisnika) VALUES (?,?,?,?,?,?)";
                    
                    PreparedStatement ps = con.prepareStatement(upitZaUnosUBazu);
                    ps.setString(1, korisnickoIme);
                    ps.setString(2, lozinka);
                    ps.setString(3, email);
                    ps.setString(4, telefon);
                    ps.setInt(5, poeni);
                    ps.setString(6, tipKorisnika);
                    
                    try 
                    {
                        ps.executeUpdate();
                    } 
                    catch (SQLException e) 
                    {
                        String poruka = e.getMessage();
                        request.setAttribute("msg", poruka);
                        request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
                    }
                    request.setAttribute("msg", "Uspesno ste se registrovali. Ulogujte se radi potvrde registracije.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    ps.close();
                    con.close();
                } 
                catch (ClassNotFoundException e) 
                {
                    
                }
                catch (SQLException se)
                {
                    String poruka = se.getMessage();
                    request.setAttribute("poruka", poruka);
                    request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
                }
            }
            else
            {
                request.setAttribute("msg", "Lozinke se ne poklapaju");
                request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
            }
        }
        else 
        {
            request.setAttribute("msg", "Morate popuniti sva polja");
            request.getRequestDispatcher("registracijaForma.jsp").forward(request, response);
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
