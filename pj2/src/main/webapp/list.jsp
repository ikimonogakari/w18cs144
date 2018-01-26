<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="blog.Blog, java.util.*" %>
<!DOCTYPE html>
<%
List<Blog> blogs = (List<Blog>) request.getAttribute("blogs");
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>List Post</title>
</head>
<body>
    <div>
        <form action="post"  id="0">
            <input type="hidden" name="username" value=<%= "\"" +request.getParameter("username")+ "\"" %> >
            <input type="hidden" name="postid" value="0">
            <button type="submit" name="action" value="open">New Post</button>
        </form>
    </div>
    <p><%= blogs.size() %></p>
    <table>
        <tr>
            <th>Title</th>
            <th>Created</th>
            <th>Modified</th>
            <th>&nbsp;</th>
        </tr>
        <%
            Iterator<Blog> iter = blogs.iterator(); 
            int count = 0;
            while(iter.hasNext()) {
                count++;
                Blog t_blog = iter.next();
                out.print("<tr>\n");
                out.print("<form id=" + count + " action=\"post\" method=\"POST\">\n");
                out.print("<input type=\"hidden\" name=\"username\" value=\"" + request.getParameter("username") + "\">\n"); 
                out.print("<input type=\"hidden\" name=\"postid\" value=\"" + t_blog.postid + "\">\n"); // postid 
                out.print("<td>"+ t_blog.title  + "</td>\n"); //title
                out.print("<td>"+ t_blog.created +"</td>\n"); //create
                out.print("<td>"+ t_blog.modified +"</td>\n"); //modify
                out.print("<td>\n");
                out.print("<button type=\"submit\" name=\"action\" value=\"open\">Open</button>\n");
                out.print("<button type=\"submit\" name=\"action\" value=\"delete\">Delete</button>\n");
                out.print("</td>\n");
                out.print("</form>\n");
                out.print("</tr>\n");
            }
        %>
    </table>
</body>
</html>
