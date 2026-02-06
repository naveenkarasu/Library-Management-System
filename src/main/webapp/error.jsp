<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error - Library Management System</title>

    <!-- Bootstrap 3 CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">Library Management System</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/books">Books</a></li>
                    <li><a href="${pageContext.request.contextPath}/members">Members</a></li>
                    <li><a href="${pageContext.request.contextPath}/issue">Issue Book</a></li>
                    <li><a href="${pageContext.request.contextPath}/return">Return Book</a></li>
                    <li><a href="${pageContext.request.contextPath}/reports">Reports</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <div class="panel panel-danger" style="margin-top: 50px;">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-exclamation-sign"></span> Error Occurred
                        </h3>
                    </div>
                    <div class="panel-body text-center">
                        <h1 style="font-size: 72px; color: #e74c3c;">
                            <span class="glyphicon glyphicon-alert"></span>
                        </h1>
                        <h2>Oops! Something went wrong.</h2>
                        <p class="lead">We apologize for the inconvenience. Please try again later.</p>

                        <c:if test="${pageContext.errorData.statusCode == 404}">
                            <div class="alert alert-warning">
                                <strong>Error 404:</strong> The page you are looking for was not found.
                            </div>
                        </c:if>

                        <c:if test="${pageContext.errorData.statusCode == 500}">
                            <div class="alert alert-danger">
                                <strong>Error 500:</strong> Internal server error occurred.
                            </div>
                        </c:if>

                        <hr>

                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                            <span class="glyphicon glyphicon-home"></span> Go to Home
                        </a>
                        <button onclick="history.back()" class="btn btn-default btn-lg">
                            <span class="glyphicon glyphicon-arrow-left"></span> Go Back
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <p>&copy; 2017 Library Management System. All rights reserved.</p>
        </div>
    </footer>

    <!-- jQuery and Bootstrap JS -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
