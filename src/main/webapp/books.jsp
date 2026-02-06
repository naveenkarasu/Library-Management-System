<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Book Management - Library Management System</title>

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
                    <li class="active"><a href="${pageContext.request.contextPath}/books">Books</a></li>
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
            <h1><span class="glyphicon glyphicon-book"></span> Book Management</h1>
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
            <!-- Book Form -->
            <div class="col-md-4">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <c:choose>
                                <c:when test="${action == 'edit'}">Edit Book</c:when>
                                <c:otherwise>Add New Book</c:otherwise>
                            </c:choose>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <form action="${pageContext.request.contextPath}/books" method="post">
                            <c:choose>
                                <c:when test="${action == 'edit'}">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="bookId" value="${book.bookId}">
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="action" value="add">
                                </c:otherwise>
                            </c:choose>

                            <div class="form-group">
                                <label for="isbn">ISBN</label>
                                <input type="text" class="form-control" id="isbn" name="isbn"
                                       value="${book.isbn}" required placeholder="e.g., 978-0-13-468599-1">
                            </div>

                            <div class="form-group">
                                <label for="title">Title</label>
                                <input type="text" class="form-control" id="title" name="title"
                                       value="${book.title}" required placeholder="Book Title">
                            </div>

                            <div class="form-group">
                                <label for="author">Author</label>
                                <input type="text" class="form-control" id="author" name="author"
                                       value="${book.author}" placeholder="Author Name">
                            </div>

                            <div class="form-group">
                                <label for="publisher">Publisher</label>
                                <input type="text" class="form-control" id="publisher" name="publisher"
                                       value="${book.publisher}" placeholder="Publisher Name">
                            </div>

                            <div class="form-group">
                                <label for="quantity">Quantity</label>
                                <input type="number" class="form-control" id="quantity" name="quantity"
                                       value="${not empty book.quantity ? book.quantity : 1}" min="1" required>
                            </div>

                            <c:if test="${action == 'edit'}">
                                <div class="form-group">
                                    <label for="available">Available</label>
                                    <input type="number" class="form-control" id="available" name="available"
                                           value="${book.available}" min="0" max="${book.quantity}" required>
                                </div>
                            </c:if>

                            <button type="submit" class="btn btn-primary btn-block">
                                <c:choose>
                                    <c:when test="${action == 'edit'}">
                                        <span class="glyphicon glyphicon-save"></span> Update Book
                                    </c:when>
                                    <c:otherwise>
                                        <span class="glyphicon glyphicon-plus"></span> Add Book
                                    </c:otherwise>
                                </c:choose>
                            </button>

                            <c:if test="${action == 'edit'}">
                                <a href="${pageContext.request.contextPath}/books" class="btn btn-default btn-block">
                                    <span class="glyphicon glyphicon-remove"></span> Cancel
                                </a>
                            </c:if>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Book List -->
            <div class="col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Book List</h3>
                    </div>
                    <div class="panel-body">
                        <!-- Search Box -->
                        <div class="search-box">
                            <form action="${pageContext.request.contextPath}/books" method="get" class="form-inline">
                                <input type="hidden" name="action" value="search">
                                <div class="input-group">
                                    <input type="text" class="form-control" name="keyword"
                                           value="${keyword}" placeholder="Search by title, author, or ISBN">
                                    <span class="input-group-btn">
                                        <button class="btn btn-primary" type="submit">
                                            <span class="glyphicon glyphicon-search"></span> Search
                                        </button>
                                    </span>
                                </div>
                                <c:if test="${not empty keyword}">
                                    <a href="${pageContext.request.contextPath}/books" class="btn btn-default">
                                        Clear Search
                                    </a>
                                </c:if>
                            </form>
                        </div>

                        <!-- Book Table -->
                        <div class="table-responsive">
                            <table class="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>ISBN</th>
                                        <th>Title</th>
                                        <th>Author</th>
                                        <th>Available</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="book" items="${books}">
                                        <tr>
                                            <td>${book.bookId}</td>
                                            <td>${book.isbn}</td>
                                            <td>${book.title}</td>
                                            <td>${book.author}</td>
                                            <td>
                                                <span class="availability-badge
                                                    <c:choose>
                                                        <c:when test="${book.available == 0}">availability-none</c:when>
                                                        <c:when test="${book.available <= 2}">availability-low</c:when>
                                                        <c:when test="${book.available <= 5}">availability-medium</c:when>
                                                        <c:otherwise>availability-high</c:otherwise>
                                                    </c:choose>">
                                                    ${book.available}/${book.quantity}
                                                </span>
                                            </td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="${pageContext.request.contextPath}/books?action=view&id=${book.bookId}"
                                                       class="btn btn-info" title="View">
                                                        <span class="glyphicon glyphicon-eye-open"></span>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/books?action=edit&id=${book.bookId}"
                                                       class="btn btn-warning" title="Edit">
                                                        <span class="glyphicon glyphicon-edit"></span>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/books?action=delete&id=${book.bookId}"
                                                       class="btn btn-danger" title="Delete"
                                                       onclick="return confirm('Are you sure you want to delete this book?');">
                                                        <span class="glyphicon glyphicon-trash"></span>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty books}">
                                        <tr>
                                            <td colspan="6" class="text-center">No books found.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- View Book Modal -->
        <c:if test="${not empty viewBook}">
            <div class="modal fade in" id="viewModal" style="display: block; background: rgba(0,0,0,0.5);">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" onclick="window.location='${pageContext.request.contextPath}/books'">&times;</button>
                            <h4 class="modal-title">Book Details</h4>
                        </div>
                        <div class="modal-body">
                            <table class="table table-bordered">
                                <tr>
                                    <th>Book ID</th>
                                    <td>${viewBook.bookId}</td>
                                </tr>
                                <tr>
                                    <th>ISBN</th>
                                    <td>${viewBook.isbn}</td>
                                </tr>
                                <tr>
                                    <th>Title</th>
                                    <td>${viewBook.title}</td>
                                </tr>
                                <tr>
                                    <th>Author</th>
                                    <td>${viewBook.author}</td>
                                </tr>
                                <tr>
                                    <th>Publisher</th>
                                    <td>${viewBook.publisher}</td>
                                </tr>
                                <tr>
                                    <th>Total Quantity</th>
                                    <td>${viewBook.quantity}</td>
                                </tr>
                                <tr>
                                    <th>Available</th>
                                    <td>${viewBook.available}</td>
                                </tr>
                                <tr>
                                    <th>Issued</th>
                                    <td>${viewBook.quantity - viewBook.available}</td>
                                </tr>
                                <tr>
                                    <th>Added On</th>
                                    <td><fmt:formatDate value="${viewBook.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <a href="${pageContext.request.contextPath}/books" class="btn btn-default">Close</a>
                            <a href="${pageContext.request.contextPath}/books?action=edit&id=${viewBook.bookId}" class="btn btn-warning">Edit</a>
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
