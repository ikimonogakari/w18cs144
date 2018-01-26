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
    <meta charset="UTF-8">
    <title>Preview Post</title>
</head>
<body>
    <div><h1>Preview Post</h1></div>
    <div>
        <form action="post" method="POST">
            <input type="hidden" name="username" value=<%= username_quot %>>
            <input type="hidden" name="postid" value=<%= postid_quot %> >
            <input type="hidden" name="title" value=<%= "\"" + request.getParameter("title") + "\"" %>>
            <input type="hidden" name="body" value=<%= "\"" + request.getParameter("body") + "\"" %>>
            <button type="submit" name="action" value="open">Close Preview</button>
        </form>
    </div>
    <div>
        <h1 id="title">
            <p><%= (String) request.getAttribute("title_html") %></p>
        </h1>
        <div>
            <p><%= (String) request.getAttribute("body_html") %></p>
        </div>
    </div>
</body>
</html>
