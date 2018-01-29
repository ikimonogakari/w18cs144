<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="blog.Blog, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Error</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" charset="UTF-8">
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
  </head>

  <body class="text-center">
    <div class="container">
      <div class="row">
        <div class="col">
            <div class="alert alert-danger" role="alert">
              <p class="text-center h1">Error Request</p>
            </div>
            <p class="text-center">Error Messege: <%= request.getAttribute("status") %></p>
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