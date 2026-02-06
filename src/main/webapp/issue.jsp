<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Issue Book - Library Management System</title>

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
                    <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/books">Books</a></li>
                    <li><a href="${pageContext.request.contextPath}/members">Members</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/issue">Issue Book</a></li>
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
            <h1><span class="glyphicon glyphicon-share"></span> Issue Book</h1>
        </div>

        <!-- Alert Messages -->
        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>Success!</strong> ${success}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible">
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <strong>Error!</strong> ${error}
            </div>
        </c:if>

        <div class="row">
            <!-- Issue Form -->
            <div class="col-md-6">
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title">Issue a Book</h3>
                    </div>
                    <div class="panel-body">
                        <form action="${pageContext.request.contextPath}/issue" method="post">
                            <input type="hidden" name="action" value="issue">

                            <div class="form-group">
                                <label for="bookId">Select Book</label>
                                <select class="form-control" id="bookId" name="bookId" required>
                                    <option value="">-- Select a Book --</option>
                                    <c:forEach var="book" items="${availableBooks}">
                                        <option value="${book.bookId}">
                                            ${book.title} by ${book.author} (${book.available} available)
                                        </option>
                                    </c:forEach>
                                </select>
                                <p class="help-block">Only books with available copies are shown.</p>
                            </div>

                            <div class="form-group">
                                <label for="memberId">Select Member</label>
                                <select class="form-control" id="memberId" name="memberId" required>
                                    <option value="">-- Select a Member --</option>
                                    <c:forEach var="member" items="${members}">
                                        <option value="${member.memberId}">
                                            ${member.name} (${member.memberType}) - ID: ${member.memberId}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="alert alert-info">
                                <strong>Loan Periods:</strong><br>
                                Students: 14 days | Faculty: 30 days<br>
                                <strong>Book Limits:</strong><br>
                                Students: 5 books | Faculty: 10 books
                            </div>

                            <button type="submit" class="btn btn-warning btn-lg btn-block">
                                <span class="glyphicon glyphicon-share"></span> Issue Book
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Available Books Summary -->
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Available Books Summary</h3>
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                            <table class="table table-condensed table-striped">
                                <thead>
                                    <tr>
                                        <th>Title</th>
                                        <th>Author</th>
                                        <th>Available</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="book" items="${availableBooks}">
                                        <tr>
                                            <td>${book.title}</td>
                                            <td>${book.author}</td>
                                            <td>
                                                <span class="availability-badge
                                                    <c:choose>
                                                        <c:when test="${book.available <= 2}">availability-low</c:when>
                                                        <c:when test="${book.available <= 5}">availability-medium</c:when>
                                                        <c:otherwise>availability-high</c:otherwise>
                                                    </c:choose>">
                                                    ${book.available}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty availableBooks}">
                                        <tr>
                                            <td colspan="3" class="text-center">No books available for issue.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Members Summary</h3>
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive" style="max-height: 300px; overflow-y: auto;">
                            <table class="table table-condensed table-striped">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Type</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="member" items="${members}">
                                        <tr>
                                            <td>${member.memberId}</td>
                                            <td>${member.name}</td>
                                            <td>
                                                <span class="label ${member.memberType == 'faculty' ? 'label-faculty' : 'label-student'}">
                                                    ${member.memberType}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty members}">
                                        <tr>
                                            <td colspan="3" class="text-center">No members registered.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
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
