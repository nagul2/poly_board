<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>글작성</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Insert.css"> <!-- css분리 -->
    <script src="${pageContext.request.contextPath}/js/Insert.js"></script> <!-- js 분리 -->
	
</head>
<body>

    <div class="header">
        <img src="${pageContext.request.contextPath}/images/real3.png" alt="Logo">
    </div>

    <div class="form-container">
        <h2>게시글 작성</h2>
        <form id="insertForm" action="${pageContext.request.contextPath}/create" method="post" onsubmit="return handleSubmit(event);">
        	 <!-- 오류 메시지 출력 -->
            <c:if test="${not empty errorMessage}">
                <div class="error"><c:out value="${errorMessage}" /></div>
            </c:if>
        
            <div class="form-group">
                <label for="title">제목</label>
                <input type="text" id="title" name="title" placeholder="제목을 입력하세요" required>
            </div>
            <div class="form-group">
                <label for="boardUserName">작성자</label>
                <input type="text" id="boardUserName" name="boardUserName" placeholder="작성자명을 입력하세요" required>
            </div>
            <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" name="content" placeholder="내용을 입력하세요" required></textarea>
            </div>
            <div class="buttons">
                <input type="submit" class="submit" value="등록">
                <input type="button" class="cancel" value="취소" onclick="history.back()">
            </div>
        </form>
    </div>
</body>
</html>
