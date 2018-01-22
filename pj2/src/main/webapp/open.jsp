<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%!
String username_quot = request.getParameter("username");
String postid_quot = request.getParameter("postid");
username_quot = "\"" + username_quot + "\"";
postid_quot = "\"" + postid_quot + "\"";
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Post</title>
</head>
<body>
    <div><h1>Edit Post</h1></div>
    <form action="post" method="POST">
        <div>
            <button type="submit" name="action" value="save">Save</button>
            <button type="submit" name="action" value="list">Close</button>
            <button type="submit" name="action" value="preview">Preview</button>
            <button type="submit" name="action" value="delete">Delete</button>
        </div>
        <input type="hidden" name="username" value=<%= username_quot %> >
        <input type="hidden" name="postid" value=<%= postid_quot %> >
        <div>
            <label for="title">Title</label>
            <input type="text" id="text" value=<%= "\"" + request.getAttribute("title") + "\"" %>>
            <!-- <input type="text" name="title" value="_Post 1_"> -->
        </div>
        <div>
            <label for="body">Body</label>
            <textarea style="height: 20rem;" id="body" name="body"><%= request.getAttribute("body") %>
            </textarea>
        </div>
    </form>
</body>
</html>
