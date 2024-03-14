/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.acc;

import dal.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.profile.Account;

/**
 *
 * @author truon
 */
@WebServlet(name = "RegisterSeller", urlPatterns = {"/registerseller"})
public class RegisterSeller extends HttpServlet {

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
            out.println("<title>Servlet RegisterSeller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterSeller at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");
        AccountDAO accDAO = new AccountDAO();
        
        if (acc != null && accDAO.isSeller(String.valueOf(acc.getAccid()))) {
            response.sendRedirect("./home");
            return;
        }
        
        if (acc != null && acc.getRoleid() == 1) {
            request.getRequestDispatcher("registerSeller.jsp").forward(request, response);
        } else {
            response.sendRedirect("./login");
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

        String storeName = request.getParameter("store_name");
        String address = request.getParameter("address_store");
        String email = request.getParameter("email_store");
        String phoneNumber = request.getParameter("phone_store");

        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("./login");
            return;
        }
        if (storeName == null || storeName.length() > 40) {
            System.out.println(storeName);
            System.out.println("store");
            request.setAttribute("message", "Incorrect store name");
            request.getRequestDispatcher("registerSeller.jsp").forward(request, response);
            return;
        }
        //  ^[A-Za-z][A-Za-z0-9\\-_]+@[a-zA-Z]{2,}+(\\.[a-zA-Z]+){1,3}
        if (email == null || !(email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
            System.out.println(email);
            System.out.println("email");
            request.setAttribute("message", "Incorrect email");
            request.getRequestDispatcher("registerSeller.jsp").forward(request, response);
            return;
        }
        if (phoneNumber == null || !(phoneNumber.matches("\\d{10}"))) {
            System.out.println(phoneNumber);
            System.out.println("phone");
            request.setAttribute("message", "Incorrect phone number");
            request.getRequestDispatcher("registerSeller.jsp").forward(request, response);
            return;
        }
        String confirmationCode = generateConfirmationCode();

        session.setAttribute("acc_id", acc.getAccid());
        session.setAttribute("store_name", request.getParameter("store_name"));
        session.setAttribute("address_store", request.getParameter("address_store"));
        session.setAttribute("email_store", request.getParameter("email_store"));
        session.setAttribute("phone_store", request.getParameter("phone_store"));
        session.setAttribute("confirmationCode", confirmationCode);

        // Lưu mã xác nhận vào cơ sở dữ liệu hoặc cấu trúc dữ liệu tạm thời (ví dụ: lưu vào ServletContext)
        getServletContext().setAttribute("confirmationCode", confirmationCode);
        System.out.println("confirm code : " + confirmationCode);
        // Gửi email chứa mã xác nhận
        sendConfirmationEmail(email, confirmationCode);

        // Chuyển hướng người dùng đến trang thay đổi mật khẩu
        response.sendRedirect("checkemail.jsp");

    }

    private String generateConfirmationCode() {
        // Triển khai mã xác nhận ngẫu nhiên ở đây (ví dụ: sử dụng UUID)
        return UUID.randomUUID().toString();
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
