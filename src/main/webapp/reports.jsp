<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reports - Library Management System</title>

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
                    <li><a href="${pageContext.request.contextPath}/issue">Issue Book</a></li>
                    <li><a href="${pageContext.request.contextPath}/return">Return Book</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/reports">Reports</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <!-- Page Header -->
        <div class="page-header">
            <h1><span class="glyphicon glyphicon-stats"></span> Reports</h1>
        </div>

        <!-- Report Navigation -->
        <div class="row">
            <div class="col-md-12">
                <ul class="nav nav-pills">
                    <li class="${reportType == 'overview' ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/reports?type=overview">
                            <span class="glyphicon glyphicon-dashboard"></span> Overview
                        </a>
                    </li>
                    <li class="${reportType == 'overdue' ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/reports?type=overdue">
                            <span class="glyphicon glyphicon-warning-sign"></span> Overdue Books
                        </a>
                    </li>
                    <li class="${reportType == 'inventory' ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/reports?type=inventory">
                            <span class="glyphicon glyphicon-list-alt"></span> Inventory Status
                        </a>
                    </li>
                    <li class="${reportType == 'issued' ? 'active' : ''}">
                        <a href="${pageContext.request.contextPath}/reports?type=issued">
                            <span class="glyphicon glyphicon-share"></span> Currently Issued
                        </a>
                    </li>
                </ul>
                <hr>
            </div>
        </div>

        <!-- Overview Report -->
        <c:if test="${reportType == 'overview'}">
            <div class="row">
                <div class="col-md-3">
                    <div class="stat-box">
                        <div class="stat-number">${totalBooks}</div>
                        <div class="stat-label">Total Book Titles</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-box stat-success">
                        <div class="stat-number">${totalAvailable}</div>
                        <div class="stat-label">Available Copies</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-box stat-warning">
                        <div class="stat-number">${totalIssued}</div>
                        <div class="stat-label">Issued Copies</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-box stat-danger">
                        <div class="stat-number">${overdueCount}</div>
                        <div class="stat-label">Overdue</div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-4">
                    <div class="stat-box stat-info">
                        <div class="stat-number">${totalMembers}</div>
                        <div class="stat-label">Total Members</div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="stat-box">
                        <div class="stat-number">${totalStudents}</div>
                        <div class="stat-label">Students</div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="stat-box">
                        <div class="stat-number">${totalFaculty}</div>
                        <div class="stat-label">Faculty</div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title">Quick Stats</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <tr>
                                    <th>Metric</th>
                                    <th>Value</th>
                                </tr>
                                <tr>
                                    <td>Total Book Titles</td>
                                    <td>${totalBooks}</td>
                                </tr>
                                <tr>
                                    <td>Total Available Copies</td>
                                    <td><span class="text-success">${totalAvailable}</span></td>
                                </tr>
                                <tr>
                                    <td>Total Issued Copies</td>
                                    <td><span class="text-warning">${totalIssued}</span></td>
                                </tr>
                                <tr>
                                    <td>Active Issues</td>
                                    <td>${activeIssues}</td>
                                </tr>
                                <tr>
                                    <td>Overdue Books</td>
                                    <td><span class="text-danger">${overdueCount}</span></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title">Member Statistics</h3>
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered">
                                <tr>
                                    <th>Category</th>
                                    <th>Count</th>
                                </tr>
                                <tr>
                                    <td>Total Members</td>
                                    <td>${totalMembers}</td>
                                </tr>
                                <tr>
                                    <td>Students</td>
                                    <td>${totalStudents}</td>
                                </tr>
                                <tr>
                                    <td>Faculty</td>
                                    <td>${totalFaculty}</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Overdue Report -->
        <c:if test="${reportType == 'overdue'}">
            <div class="panel panel-danger">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <span class="glyphicon glyphicon-warning-sign"></span> Overdue Books Report
                    </h3>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${not empty overdueTransactions}">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Trans. ID</th>
                                            <th>Book Title</th>
                                            <th>ISBN</th>
                                            <th>Member</th>
                                            <th>Due Date</th>
                                            <th>Days Overdue</th>
                                            <th>Estimated Fine</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="trans" items="${overdueTransactions}">
                                            <tr class="overdue-row">
                                                <td>${trans.transactionId}</td>
                                                <td>${trans.book.title}</td>
                                                <td>${trans.book.isbn}</td>
                                                <td>
                                                    ${trans.member.name}<br>
                                                    <small>${trans.member.email}</small>
                                                </td>
                                                <td><fmt:formatDate value="${trans.dueDate}" pattern="yyyy-MM-dd"/></td>
                                                <td><span class="label label-danger">${trans.overdueDays} days</span></td>
                                                <td class="text-danger">
                                                    <strong>$<fmt:formatNumber value="${trans.overdueDays * 0.50}" pattern="0.00"/></strong>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="alert alert-warning">
                                <strong>Total Overdue Books:</strong> ${overdueTransactions.size()}
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-success">
                                <span class="glyphicon glyphicon-ok"></span>
                                No overdue books. All books have been returned on time!
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:if>

        <!-- Inventory Report -->
        <c:if test="${reportType == 'inventory'}">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <span class="glyphicon glyphicon-list-alt"></span> Inventory Status Report
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="stat-box">
                                <div class="stat-number">${totalQuantity}</div>
                                <div class="stat-label">Total Copies</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="stat-box stat-success">
                                <div class="stat-number">${totalAvailable}</div>
                                <div class="stat-label">Available</div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="stat-box stat-warning">
                                <div class="stat-number">${totalIssued}</div>
                                <div class="stat-label">Issued</div>
                            </div>
                        </div>
                    </div>
                    <hr>
                    <div class="table-responsive">
                        <table class="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <th>Book ID</th>
                                    <th>ISBN</th>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>Publisher</th>
                                    <th>Total</th>
                                    <th>Available</th>
                                    <th>Issued</th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="book" items="${allBooks}">
                                    <tr>
                                        <td>${book.bookId}</td>
                                        <td>${book.isbn}</td>
                                        <td>${book.title}</td>
                                        <td>${book.author}</td>
                                        <td>${book.publisher}</td>
                                        <td>${book.quantity}</td>
                                        <td>
                                            <span class="availability-badge
                                                <c:choose>
                                                    <c:when test="${book.available == 0}">availability-none</c:when>
                                                    <c:when test="${book.available <= 2}">availability-low</c:when>
                                                    <c:when test="${book.available <= 5}">availability-medium</c:when>
                                                    <c:otherwise>availability-high</c:otherwise>
                                                </c:choose>">
                                                ${book.available}
                                            </span>
                                        </td>
                                        <td>${book.quantity - book.available}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${book.available == 0}">
                                                    <span class="label label-danger">Out of Stock</span>
                                                </c:when>
                                                <c:when test="${book.available <= 2}">
                                                    <span class="label label-warning">Low Stock</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="label label-success">In Stock</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Issued Books Report -->
        <c:if test="${reportType == 'issued'}">
            <div class="panel panel-warning">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <span class="glyphicon glyphicon-share"></span> Currently Issued Books
                    </h3>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${not empty issuedTransactions}">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Trans. ID</th>
                                            <th>Book</th>
                                            <th>Member</th>
                                            <th>Issue Date</th>
                                            <th>Due Date</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="trans" items="${issuedTransactions}">
                                            <tr class="${trans.overdue ? 'overdue-row' : ''}">
                                                <td>${trans.transactionId}</td>
                                                <td>
                                                    <strong>${trans.book.title}</strong><br>
                                                    <small class="text-muted">${trans.book.author}</small>
                                                </td>
                                                <td>
                                                    ${trans.member.name}<br>
                                                    <span class="label ${trans.member.memberType == 'faculty' ? 'label-faculty' : 'label-student'}">
                                                        ${trans.member.memberType}
                                                    </span>
                                                </td>
                                                <td><fmt:formatDate value="${trans.issueDate}" pattern="yyyy-MM-dd"/></td>
                                                <td><fmt:formatDate value="${trans.dueDate}" pattern="yyyy-MM-dd"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${trans.overdue}">
                                                            <span class="label label-danger">Overdue (${trans.overdueDays} days)</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="label label-success">Active</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="alert alert-info">
                                <strong>Total Issued:</strong> ${issuedTransactions.size()} books
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-success">
                                <span class="glyphicon glyphicon-ok"></span>
                                No books are currently issued.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:if>
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
