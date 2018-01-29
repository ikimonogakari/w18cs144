<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="blog.Blog, java.util.*" %>
<!DOCTYPE html>
<%
String username_quot = request.getParameter("username");
String postid_quot = request.getParameter("postid");
username_quot = "\"" + username_quot + "\"";
postid_quot = "\"" + postid_quot + "\"";
%>
<html>
<head>
        <title>Preview</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <!-- <p><%= request.getAttribute("get") %></p>
    <p><%= request.getAttribute("sql") %></p> -->
    <nav class="navbar navbar-light bg-light">
        <span class="navbar-brand mb-0 h1">Preview Post</span>
        <form action="post" id=0 method="POST">
                <input type="hidden" name="username" value=<%= username_quot %>>
                <input type="hidden" name="postid" value=<%= postid_quot %>>
                <input type="hidden" name="title" value=<%= "\""+request.getAttribute("title_h")+"\"" %>>
                <input type="hidden" name="body" value=<%= "\""+request.getAttribute("body_h")+"\"" %>>
                <button type="submit" name="action" value="open" class="btn btn-warning navbar-btn" aria-controls="navbarSupportedContent">
                    <span class="glyphicon glyphicon-plus"></span>Close
                </button>
            </button>
        </form>
    </nav>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="col-8">
                <%
                    String title_html = (String) request.getAttribute("title_html");
                    String body_html = (String) request.getAttribute("body_html");
                    if (title_html.equals("") && body_html.equals("")) {
                        out.print("<blockquote class=\"blockquote text-center\">\n");
                        out.print("<p class=\"text-muted\">Content is empty</p>\n");
                        out.print("</blockquote>");
                    }
                %>
                <div>
                    <div>
                        <h1><%= (String) request.getAttribute("title_html") %></h1>
                    </div>
                    <div>
                        <p><%= (String) request.getAttribute("body_html") %></p>
                    </div>
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
