<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="blog.Blog, java.util.*" %>
<!DOCTYPE html>
<%
List<Blog> blogs = (List<Blog>) request.getAttribute("blogs");
%>
<html>
    <head>
        <title>List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    </head>
<body>
    <nav class="navbar navbar-light bg-light">
        <span class="navbar-brand mb-0 h1"><%= request.getParameter("username") %>'s Posts</span>
        <form action="post" id=0 method="POST">
            <input type="hidden" name="username" value=<%= "\""+request.getParameter("username")+"\"" %>>
            <input type="hidden" name="postid" value="0">
            <button type="submit" name="action" value="open" class="btn btn-primary navbar-btn" aria-controls="navbarSupportedContent">
                <span class="glyphicon glyphicon-plus"></span>New
            </button>
        </form>
    </nav>
    <!-- <p><%= blogs.size() %></p>
    <p><%= request.getParameter("username") %><p> -->
    <div class="container-fluid">
        <div class="row">
            <div class="col-12">
                <div class="table-responsive">
                <table class="table table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Title</th>
                            <th>Created Time</th>
                            <th>Modified Time</th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        Iterator<Blog> iter = blogs.iterator(); 
                        int count = 0;
                        while(iter.hasNext()) {
                            count++;
                            Blog t_blog = iter.next();
                            out.print("<tr>\n");
                            out.print("<form id=" +count+ " action=\"post\" method=\"POST\">\n");
                            out.print("<input type=\"hidden\" name=\"username\" value=\"" + request.getParameter("username") + "\">\n"); 
                            out.print("<input type=\"hidden\" name=\"postid\" value=\"" + t_blog.postid + "\">\n");
                            out.print("<td><pre>"+ t_blog.title + "</pre></td>\n"); //title
                            out.print("<td>"+ t_blog.created + "</td>\n"); //create
                            out.print("<td>"+ t_blog.modified + "</td>\n"); //modify
                            out.print("<td>\n");
                            out.print("<div class=\"btn-group\" role=\"group\">\n");
                            out.print("<button type=\"submit\" name=\"action\" value=\"open\" class=\"btn btn-success btn-sm\">\n");
                            out.print("<span class=\"glyphicon glyphicon-plus\"></span>Open\n");
                            out.print("</button>\n");
                            out.print("<button type=\"submit\" name=\"action\" value=\"delete\" class=\"btn btn-danger btn-sm\">\n");
                            out.print("<span class=\"glyphicon glyphicon-plus\"></span>Delete\n");
                            out.print("</button>\n");
                            out.print("</td>\n");
                            out.print("</form>\n");
                            out.print("</tr>\n");
                        }
                    %>
                    </tbody>
                </table>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
