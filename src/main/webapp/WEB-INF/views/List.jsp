<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>kopo20의 게시판</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/List.css"> <!-- css분리 -->
    
    <script src="${pageContext.request.contextPath}/js/List.js"></script>
</head>
<body>
    <div class="header">
        <img src="${pageContext.request.contextPath}/images/realmain.png" alt="Logo">
    </div>
    <div class="container">
        <h1>리얼포스 게시판</h1>
        <hr>
        <h2>리얼포스를 안가지고 있다면 글작성 금지 ^^</h2>
        <table class="board">
            <thead>
                <tr>
                    <th>No</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${boardItems.content}">
                    <tr>
                        <td>${item.id}</td>
			            <td>
			                <form id="viewForm${item.id}" action="${pageContext.request.contextPath}/view/${item.id}" method="get" style="display: inline;">
			                    <input type="hidden" name="page" value="${pagination.cp}">
			                    <input type="hidden" name="sizePerpage" value="${pagination.pageSize}">
			                    <a href="#" onclick="document.getElementById('viewForm${item.id}').submit();" style="text-decoration:none; color:#333;">
			                        ${item.title}
			                    </a>
			                </form>
			            </td>
                        <td>${item.boardUserName}</td>
                        <td>
                            <fmt:formatDate value="${item.date}" pattern="yyyy-MM-dd" />
                        </td>
                        <td>${item.count}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="buttons">
            <input type="button" value="글쓰기" onclick="location.href='create'">
        </div>
        
		<div class="paging">
		    <c:if test="${pagination.startPage > 1}">
		        <a href="${pageContext.request.contextPath}/list?page=${pagination.pp}&sizePerpage=${pagination.pageSize}"><<</a>
		        <c:if test="${pagination.p != pagination.cp}">
		            <a href="${pageContext.request.contextPath}/list?page=${pagination.p}&sizePerpage=${pagination.pageSize}"><</a>
		        </c:if>
		    </c:if>
		    <c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}">
		        <a href="${pageContext.request.contextPath}/list?page=${i}&sizePerpage=${pagination.pageSize}">
		            <c:choose>
		                <c:when test="${i == pagination.cp}">
		                    <b>${i}</b>
		                </c:when>
		                <c:otherwise>
		                    ${i}
		                </c:otherwise>
		            </c:choose>
		        </a>
		    </c:forEach>
		    <c:if test="${pagination.endPage < pagination.totalPages}">
		        <c:if test="${pagination.n != pagination.cp}">
		            <a href="${pageContext.request.contextPath}/list?page=${pagination.n}&sizePerpage=${pagination.pageSize}">></a>
		        </c:if>
		        <c:if test="${pagination.nn != pagination.cp}">
		            <a href="${pageContext.request.contextPath}/list?page=${pagination.nn}&sizePerpage=${pagination.pageSize}">>></a>
		        </c:if>
		    </c:if>
		</div>
    </div>
</body>
</html>
