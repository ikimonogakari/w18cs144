<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%!
String username_quot = request.getAttribute("username");
username_quot = """ + username_quot + """;
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>List Post</title>
</head>
<body>
    <div>
        <form action="post"  id="0">
            <input type="hidden" name="username" value=<%= username_quot %> >
            <input type="hidden" name="postid" value=0>
        </form>
    </div>
    <table>
        <tr>
            <th>Title</th>
            <th>Created</th>
            <th>Modified</th>
            <th>&nbsp;</th>
        </tr>
        <%
            Enumeration attrNames = request.getAttributeNames();
            int count = 0;
            while(attrNames.hasMoreElements()) {
                count++;
                String attrKey = (String) attrNames.nextElement();
                List<String> item = (List<String>) request.getAttribute(attrKey);
                out.print("<tr>\n");
                out.print("<form id=" + count + " action=\"post\" method=\"POST\">\n")
                out.print("<input type=\"hidden\" name=\"username\" value="+username_quot+">\n"); 
                out.print("<input type=\"hidden\" name=\"postid\" value="+item.get(0)+">\n"); // postid 
                out.print("<td>"+item.get(1)+"</td>\n"); //title
                out.print("<td>"+item.get(2)+"</td>\n"); //create
                out.print("<td>"+item.get(3)+"</td>\n"); //modify
                out.print("<td>\n");
                out.print("<button type="submit" name="action" value="open">Open</button>\n");
                out.print("<button type="submit" name="action" value="delete">Delete</button>\n");
                out.print("</td>\n");
                out.print("</form>\n");
                out.print("</tr>\n");
            }
        %>
    </table>
</body>
</html>
