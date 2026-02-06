<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Member Management - Library Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/members">Members</a></li>
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
            <h1><span class="glyphicon glyphicon-user"></span> Member Management</h1>
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
            <!-- Member Form -->
            <div class="col-md-4">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <c:choose>
                                <c:when test="${action == 'edit'}">Edit Member</c:when>
                                <c:otherwise>Register New Member</c:otherwise>
                            </c:choose>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <form action="${pageContext.request.contextPath}/members" method="post">
                            <c:choose>
                                <c:when test="${action == 'edit'}">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="memberId" value="${member.memberId}">
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="action" value="register">
                                </c:otherwise>
                            </c:choose>

                            <div class="form-group">
                                <label for="name">Full Name</label>
                                <input type="text" class="form-control" id="name" name="name"
                                       value="${member.name}" required placeholder="Full Name">
                            </div>

                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" id="email" name="email"
                                       value="${member.email}" placeholder="email@example.com">
                            </div>

                            <div class="form-group">
                                <label for="phone">Phone</label>
                                <input type="text" class="form-control" id="phone" name="phone"
                                       value="${member.phone}" placeholder="555-0100">
                            </div>

                            <div class="form-group">
                                <label for="memberType">Member Type</label>
                                <select class="form-control" id="memberType" name="memberType" required>
                                    <option value="student" ${member.memberType == 'student' ? 'selected' : ''}>Student</option>
                                    <option value="faculty" ${member.memberType == 'faculty' ? 'selected' : ''}>Faculty</option>
                                </select>
                            </div>

                            <button type="submit" class="btn btn-info btn-block">
                                <c:choose>
                                    <c:when test="${action == 'edit'}">
                                        <span class="glyphicon glyphicon-save"></span> Update Member
                                    </c:when>
                                    <c:otherwise>
                                        <span class="glyphicon glyphicon-plus"></span> Register Member
                                    </c:otherwise>
                                </c:choose>
                            </button>

                            <c:if test="${action == 'edit'}">
                                <a href="${pageContext.request.contextPath}/members" class="btn btn-default btn-block">
                                    <span class="glyphicon glyphicon-remove"></span> Cancel
                                </a>
                            </c:if>
                        </form>
                    </div>
                </div>

                <!-- Member Type Info -->
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Member Privileges</h3>
                    </div>
                    <div class="panel-body">
                        <h5><span class="label label-student">Student</span></h5>
                        <ul>
                            <li>Max Books: 5</li>
                            <li>Loan Period: 14 days</li>
                        </ul>
                        <h5><span class="label label-faculty">Faculty</span></h5>
                        <ul>
                            <li>Max Books: 10</li>
                            <li>Loan Period: 30 days</li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Member List -->
            <div class="col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Member List</h3>
                    </div>
                    <div class="panel-body">
                        <!-- Search Box -->
                        <div class="search-box">
                            <form action="${pageContext.request.contextPath}/members" method="get" class="form-inline">
                                <input type="hidden" name="action" value="search">
                                <div class="input-group">
                                    <input type="text" class="form-control" name="keyword"
                                           value="${keyword}" placeholder="Search by name, email, or phone">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary" type="submit">
                                            <span class="glyphicon glyphicon-search"></span> Search
                                        </button>
                                    </span>
                                </div>
                                <c:if test="${not empty keyword}">
                                    <a href="${pageContext.request.contextPath}/members" class="btn btn-default">
                                        Clear Search
                                    </a>
                                </c:if>
                            </form>
                        </div>

                        <!-- Member Table -->
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Phone</th>
                                        <th>Type</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="member" items="${members}">
                                        <tr>
                                            <td>${member.memberId}</td>
                                            <td>${member.name}</td>
                                            <td>${member.email}</td>
                                            <td>${member.phone}</td>
                                            <td>
                                                <span class="label ${member.memberType == 'faculty' ? 'label-faculty' : 'label-student'}">
                                                    ${member.memberType}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="${pageContext.request.contextPath}/members?action=view&id=${member.memberId}"
                                                       class="btn btn-info" title="View">
                                                        <span class="glyphicon glyphicon-eye-open"></span>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/members?action=edit&id=${member.memberId}"
                                                       class="btn btn-warning" title="Edit">
                                                        <span class="glyphicon glyphicon-edit"></span>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/members?action=delete&id=${member.memberId}"
                                                       class="btn btn-danger" title="Delete"
                                                       onclick="return confirm('Are you sure you want to delete this member?');">
                                                        <span class="glyphicon glyphicon-trash"></span>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty members}">
                                        <tr>
                                            <td colspan="6" class="text-center">No members found.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- View Member Modal -->
        <c:if test="${not empty viewMember}">
            <div class="modal fade in" id="viewModal" style="display: block; background: rgba(0,0,0,0.5);">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" onclick="window.location='${pageContext.request.contextPath}/members'">&times;</button>
                            <h4 class="modal-title">Member Details</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <table class="table table-bordered">
                                        <tr>
                                            <th>Member ID</th>
                                            <td>${viewMember.memberId}</td>
                                        </tr>
                                        <tr>
                                            <th>Name</th>
                                            <td>${viewMember.name}</td>
                                        </tr>
                                        <tr>
                                            <th>Email</th>
                                            <td>${viewMember.email}</td>
                                        </tr>
                                        <tr>
                                            <th>Phone</th>
                                            <td>${viewMember.phone}</td>
                                        </tr>
                                        <tr>
                                            <th>Type</th>
                                            <td>
                                                <span class="label ${viewMember.memberType == 'faculty' ? 'label-faculty' : 'label-student'}">
                                                    ${viewMember.memberType}
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Registered On</th>
                                            <td><fmt:formatDate value="${viewMember.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="col-md-6">
                                    <h4>Currently Issued Books</h4>
                                    <c:choose>
                                        <c:when test="${not empty memberTransactions}">
                                            <table class="table table-condensed table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Book</th>
                                                        <th>Due Date</th>
                                                        <th>Status</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="trans" items="${memberTransactions}">
                                                        <tr class="${trans.overdue ? 'overdue-row' : ''}">
                                                            <td>${trans.book.title}</td>
                                                            <td><fmt:formatDate value="${trans.dueDate}" pattern="yyyy-MM-dd"/></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${trans.overdue}">
                                                                        <span class="label label-danger">Overdue</span>
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
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted">No books currently issued.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <a href="${pageContext.request.contextPath}/members" class="btn btn-default">Close</a>
                            <a href="${pageContext.request.contextPath}/members?action=edit&id=${viewMember.memberId}" class="btn btn-warning">Edit</a>
                        </div>
                    </div>
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
