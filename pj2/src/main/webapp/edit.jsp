<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="blog.Blog, java.util.*" %>
<!DOCTYPE html>
<%
String username_quot = request.getParameter("username");
String postid_quot = request.getParameter("postid");
username_quot = "\"" + username_quot + "\"";
postid_quot = "\"" + postid_quot + "\"";
Blog blog = (Blog) request.getAttribute("blog");
%>
<html>
<head>
    <title>Edit</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-light bg-light">
        <span class="navbar-brand mb-0 h1">Edit Post</span>
    </nav>
    <div class="container">
        <div class="row">
            <div class="col-12">
                <form action="post" method="POST">
                    <div class="form-group">
                        <div class="btn-group" role="group">
                            <button type="submit" class="btn btn-outline-primary" name="action" value="save">Save</button>
                            <button type="submit" class="btn btn-outline-warning" name="action" value="list">Close</button>
                            <button type="submit" class="btn btn-outline-info" name="action" value="preview">Preview</button>
                            <button type="submit" class="btn btn-outline-danger" name="action" value="delete">Delete</button>
                        </div>
                        <input type="hidden" name="username" value=<%= username_quot %> >
                        <input type="hidden" name="postid" value=<%= postid_quot %> >
                        <div>
                            <label for="title">Title</label>
                            <input type="text" class="form-control form-control-lg" id="title" name="title" value=<%= "\""+blog.title+"\"" %> placeholder="Enter title">
                        </div>
                        <div>
                            <label for="body">Body</label>
                            <textarea class="form-control" id="body" rows="10" name="body" placeholder="Type Markdown"><%= blog.body %></textarea>
                        </div>
                    </div>
                </form>
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
