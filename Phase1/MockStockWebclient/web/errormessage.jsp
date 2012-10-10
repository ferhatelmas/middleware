<%@page isErrorPage="true" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="headererror.html" %>
<!-- 
Error page for Java Exceptions and customized error 
-->
<div id="content">
    <div id="trader">
        <h2>Oops, an error occurred.</h2>
        <% if (session.getAttribute("error") != null) {%> 
        <p class="error"><%= session.getAttribute("error")%></p>
        <% session.setAttribute("error", null); } else {%>
        <p class="error">We are sorry, that shouldn't happen. However, if this error occurs again, do not hesitate to contact us.</p>
        <% }%>
    </div>
</div>
<%@include file="footer.html" %>