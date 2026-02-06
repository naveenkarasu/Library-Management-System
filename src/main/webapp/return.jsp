<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Return Book - Library Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/return">Return Book</a></li>
                    <li><a href="${pageContext.request.contextPath}/reports">Reports</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <!-- Page Header -->
        <div class="page-header">
            <h1><span class="glyphicon glyphicon-check"></span> Return Book</h1>
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

        <!-- Fine Amount Display -->
        <c:if test="${not empty fineAmount and fineAmount > 0}">
            <div class="alert alert-warning">
                <h4><span class="glyphicon glyphicon-usd"></span> Fine Collected</h4>
                <p>Fine Amount: <strong>$<fmt:formatNumber value="${fineAmount}" pattern="0.00"/></strong></p>
                <p class="small">Fine rate: $0.50 per day overdue</p>
            </div>
        </c:if>

        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-danger">
                    <div class="panel-heading">
                        <h3 class="panel-title">Currently Issued Books</h3>
                    </div>
                    <div class="panel-body">
                        <c:choose>
                            <c:when test="${not empty activeTransactions}">
                                <div class="table-responsive">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th>Trans. ID</th>
                                                <th>Book</th>
                                                <th>Member</th>
                                                <th>Issue Date</th>
                                                <th>Due Date</th>
                                                <th>Status</th>
                                                <th>Est. Fine</th>
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="trans" items="${activeTransactions}">
                                                <tr class="${trans.overdue ? 'overdue-row' : ''}">
                                                    <td>${trans.transactionId}</td>
                                                    <td>
                                                        <strong>${trans.book.title}</strong><br>
                                                        <small class="text-muted">${trans.book.isbn}</small>
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
                                                                <span class="label label-danger">
                                                                    Overdue (${trans.overdueDays} days)
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="label label-success">Active</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${trans.overdue}">
                                                                <span class="text-danger">
                                                                    $<fmt:formatNumber value="${trans.overdueDays * 0.50}" pattern="0.00"/>
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-success">$0.00</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <form action="${pageContext.request.contextPath}/return" method="post" style="display: inline;">
                                                            <input type="hidden" name="action" value="return">
                                                            <input type="hidden" name="transactionId" value="${trans.transactionId}">
                                                            <button type="submit" class="btn btn-sm btn-danger"
                                                                    onclick="return confirm('Return this book? ${trans.overdue ? 'Fine: $' : ''}${trans.overdue ? trans.overdueDays * 0.50 : ''}');">
                                                                <span class="glyphicon glyphicon-check"></span> Return
                                                            </button>
                                                        </form>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                    <span class="glyphicon glyphicon-info-sign"></span>
                                    No books are currently issued. All books have been returned!
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <!-- Fine Information -->
        <div class="row">
            <div class="col-md-6">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title"><span class="glyphicon glyphicon-info-sign"></span> Fine Policy</h3>
                    </div>
                    <div class="panel-body">
                        <ul>
                            <li><strong>Fine Rate:</strong> $0.50 per day</li>
                            <li>Fine is calculated from the day after the due date</li>
                            <li>Fine is charged at the time of return</li>
                            <li>Please return books on time to avoid fines</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><span class="glyphicon glyphicon-time"></span> Loan Periods</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <tr>
                                <th>Member Type</th>
                                <th>Loan Period</th>
                                <th>Max Books</th>
                            </tr>
                            <tr>
                                <td><span class="label label-student">Student</span></td>
                                <td>14 days</td>
                                <td>5 books</td>
                            </tr>
                            <tr>
                                <td><span class="label label-faculty">Faculty</span></td>
                                <td>30 days</td>
                                <td>10 books</td>
                            </tr>
                        </table>
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
