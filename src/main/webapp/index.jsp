<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Library Management System</title>

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
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/">Library Management System</a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="${pageContext.request.contextPath}/">Home</a></li>
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
        <!-- Page Header -->
        <div class="page-header">
            <h1>Welcome to Library Management System</h1>
            <p class="lead">Manage your library books, members, and transactions efficiently.</p>
        </div>

        <!-- Quick Actions -->
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Quick Actions</h3>
                    </div>
                    <div class="panel-body">
                        <a href="${pageContext.request.contextPath}/books?action=new" class="btn btn-success btn-lg">
                            <span class="glyphicon glyphicon-plus"></span> Add New Book
                        </a>
                        <a href="${pageContext.request.contextPath}/members?action=new" class="btn btn-info btn-lg">
                            <span class="glyphicon glyphicon-user"></span> Register Member
                        </a>
                        <a href="${pageContext.request.contextPath}/issue" class="btn btn-warning btn-lg">
                            <span class="glyphicon glyphicon-share"></span> Issue Book
                        </a>
                        <a href="${pageContext.request.contextPath}/return" class="btn btn-danger btn-lg">
                            <span class="glyphicon glyphicon-check"></span> Return Book
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Feature Cards -->
        <div class="row">
            <div class="col-md-4">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-book"></span> Book Management
                        </h3>
                    </div>
                    <div class="panel-body">
                        <p>Add, edit, delete, and search books in the library catalog. Track availability and manage inventory.</p>
                        <a href="${pageContext.request.contextPath}/books" class="btn btn-success">
                            Manage Books <span class="glyphicon glyphicon-chevron-right"></span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-user"></span> Member Management
                        </h3>
                    </div>
                    <div class="panel-body">
                        <p>Register new members, update member information, and view member details including borrowed books.</p>
                        <a href="${pageContext.request.contextPath}/members" class="btn btn-info">
                            Manage Members <span class="glyphicon glyphicon-chevron-right"></span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-transfer"></span> Issue/Return
                        </h3>
                    </div>
                    <div class="panel-body">
                        <p>Issue books to members and process returns. Automatic fine calculation for overdue books.</p>
                        <a href="${pageContext.request.contextPath}/issue" class="btn btn-warning">
                            Issue/Return <span class="glyphicon glyphicon-chevron-right"></span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-stats"></span> Reports
                        </h3>
                    </div>
                    <div class="panel-body">
                        <p>Generate various reports including overdue books, inventory status, and transaction history.</p>
                        <a href="${pageContext.request.contextPath}/reports" class="btn btn-default">
                            View Reports <span class="glyphicon glyphicon-chevron-right"></span>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <span class="glyphicon glyphicon-info-sign"></span> About
                        </h3>
                    </div>
                    <div class="panel-body">
                        <p><strong>Library Management System v1.0</strong></p>
                        <ul>
                            <li>Fine Rate: $0.50 per day</li>
                            <li>Student Loan Period: 14 days</li>
                            <li>Faculty Loan Period: 30 days</li>
                            <li>Student Max Books: 5</li>
                            <li>Faculty Max Books: 10</li>
                        </ul>
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
