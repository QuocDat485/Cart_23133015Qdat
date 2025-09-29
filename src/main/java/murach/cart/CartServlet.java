package murach.cart;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import murach.data.*;
import murach.business.*;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        ServletContext sc = getServletContext();
        
        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart";  // default action
        }

        // perform action and set URL to appropriate page
        String url = "/index.jsp";
        if (action.equals("shop")) {            
            url = "/index.jsp";    // the "index" page
        } 
        else if (action.equals("cart")) {
            String mode = request.getParameter("mode");
            if (mode == null || (!mode.equals("update") && !mode.equals("add"))) {
                mode = "add"; // Mặc định là add nếu mode không hợp lệ
            }
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart;
            
            synchronized(session) {
                cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                    session.setAttribute("cart", cart);
                }
            }

            int quantity = 1; // Mặc định số lượng là 1 cho hành động add
            if ("update".equals(mode) && quantityString != null && !quantityString.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 0) {
                        quantity = 1; // Reset to 1 if negative
                    }
                } catch (NumberFormatException nfe) {
                    quantity = 1; // Mặc định là 1 nếu không hợp lệ
                }
            }

            String path = sc.getRealPath("/WEB-INF/products.txt");
            Product product = ProductIO.getProduct(productCode, path);

            if (product != null) {
                LineItem lineItem = new LineItem();
                lineItem.setProduct(product);
                lineItem.setQuantity(quantity);

                if ("update".equals(mode)) {
                    cart.updateItemQuantity(lineItem, quantity);
                } else if ("add".equals(mode)) {
                    cart.addItem(lineItem); // Tăng số lượng lên 1 nếu đã tồn tại
                }
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";
        }
        else if (action.equals("checkout")) {
            url = "/checkout.jsp";
        }

        response.sendRedirect(request.getContextPath() + url); // Sử dụng redirect để tránh tăng số lượng khi refresh
    }
    
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || !action.equals("cart")) {
            doPost(request, response); // Chỉ gọi doPost nếu không phải action=cart
        } else {
            // Khi load lại trang cart.jsp, chỉ hiển thị giỏ hàng
            ServletContext sc = getServletContext();
            sc.getRequestDispatcher("/cart.jsp").forward(request, response);
        }
    }
}