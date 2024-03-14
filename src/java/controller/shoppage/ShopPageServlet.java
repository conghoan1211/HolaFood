/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.shoppage;

import dal.AccountDAO;
import dal.ManagerCateDAO;
import dal.ManagerSellerDAO;
import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.product.Product;
import model.profile.Seller;

/**
 *
 * @author hoang
 */
@WebServlet(name = "ShopPageServlet", urlPatterns = {"/shoppage"})
public class ShopPageServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ShopPageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShopPageServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String sid_raw = request.getParameter("sid");
        String cateid = request.getParameter("cateid");
        String suggest_id = request.getParameter("suggestid");

        if (cateid == null) {
            cateid = "5";
        }
        try {
            int sid = Integer.parseInt(sid_raw);
            int cid = Integer.parseInt(cateid);
            int suggestid = Integer.parseInt(suggest_id);

            Seller seller = ManagerSellerDAO.gI().getSellerBySeller_Id(sid);
            request.setAttribute("seller", seller);
            request.setAttribute("suggestid", suggestid);
            request.setAttribute("cid", cid);

            List<Product> listSuggestion = ManagerCateDAO.gI().getProductBySeller(constant.ConstShop.SUGGESTION_OF_SELLER, cid, seller.getSeller_id());
            List<Product> listMenuOfSeller = ManagerCateDAO.gI().getProductBySeller(constant.ConstShop.MENU_TODAY, 0, seller.getSeller_id());
            List<Product> outstandingProduct = ManagerCateDAO.gI().getProductBySeller(constant.ConstShop.OUTSTANDING_PRODUCT, 0, seller.getSeller_id());

            request.setAttribute("listmenuofseller", listMenuOfSeller);
            request.setAttribute("outstadingproduct", outstandingProduct);
            request.setAttribute("suggestionofseller", listSuggestion);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("shop_page.jsp").forward(request, response);
        }

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
