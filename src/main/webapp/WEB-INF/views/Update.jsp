<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 수정</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Update.css"> <!-- 공통 스타일시트 -->
    <script src="${pageContext.request.contextPath}/js/Update.js"></script>
</head>
<body>
 <div class="header">
        <img src="${pageContext.request.contextPath}/images/real1.png" alt="Logo">
    </div>
    
    <div class="form-container">
        <h2>게시글 수정</h2>
        <form id="updateForm" action="${pageContext.request.contextPath}/update?id=${boardItem.id}" method="post" onsubmit="return handleUpdateSubmit(event);">
            <div class="form-group">
                <label for="id">번호</label>
                <input type="text" id="id" name="id" value="${boardItem.id}" readonly> <!-- Readonly 필드 -->
            </div>
            
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" value="${boardItem.title}">
                <c:if test="${not empty errorMessage}">
                    <div class="error">
                        <c:out value="${errorMessage}" />
                    </div>
                </c:if>
            </div>
            
            <div class="form-group">
                <label for="boardUserName">작성자</label>
                <input type="text" id="boardUserName" name="boardUserName" value="${boardItem.boardUserName}" readonly> <!-- Readonly 필드 -->
            </div>
            
            <div class="form-group">
                <label for="date">작성일</label>
                <input type="text" id="date" name="date" value="<fmt:formatDate value='${boardItem.date}' pattern='yyyy-MM-dd'/>" readonly> <!-- Readonly 필드 -->
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content">${boardItem.content}</textarea>
            </div>
            
            <div class="buttons">
                <input type="submit" class="submit" value="수정 완료">
                <input type="button" class="cancel" value="취소" onclick="location.href='${pageContext.request.contextPath}/view/${boardItem.id}'">
            </div>
        </form>
    </div>
</body>
</html>
