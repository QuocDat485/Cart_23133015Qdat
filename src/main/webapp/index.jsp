<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>CD list</title>
    <link rel="stylesheet" href="styles/main.css">
</head>
<body>
    <h1>CD list</h1>
    <table>
        <tr>
            <th>Description</th>
            <th>Price</th>
            <th>&nbsp;</th> <!-- Cột cho nút Add to Cart -->
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td><c:out value="${product.description}" /></td>
                <td class="right"><c:out value="${product.priceCurrencyFormat}" /></td>
                <td>
                    <form action="cart" method="post">
                        <input type="hidden" name="productCode" value="<c:out value='${product.code}' />">
                        <input type="hidden" name="quantity" value="1"> <!-- Mặc định quantity là 1 -->
                        <input type="submit" value="Add to Cart">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>