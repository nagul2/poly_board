<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${id}번 게시글</title>
    
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/View.css">
    <script src="${pageContext.request.contextPath}/js/View.js"></script>
</head>
<body>	
    <div class="header">
        <img src="${pageContext.request.contextPath}/images/real2.png" alt="Logo">
    </div>
    
    <div class="container">
        <!-- Hidden input to store the boardId -->
        <input type="hidden" id="boardId" value="${id}">
        
        <h1>${title}</h1>

        <table class="details">
            <tr>
                <th>작성자</th>
                <td>${boardUserName}</td>
                <th>작성일</th>
                <td><fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <th>조회수</th>
                <td>${count}</td>
            </tr>
        </table>

        <div class="content">
            ${content}
        </div>
        
        <div class="buttons">
            <form id="backToListForm" action="${pageContext.request.contextPath}/list" method="get" style="display: inline;">
                <input type="hidden" name="page" value="${page}">
                <input type="hidden" name="sizePerpage" value="${sizePerpage}">
                <input type="submit" class="list" value="목록으로">
            </form>
            <form id="updateForm" action="${pageContext.request.contextPath}/update" method="get" style="display: inline;">
                <input type="hidden" name="id" value="${id}">
                <input type="hidden" name="page" value="${page}">
                <input type="hidden" name="sizePerpage" value="${sizePerpage}">
                <input type="submit" class="update" value="수정" onclick="return confirm('글을 수정하시겠습니까?')">
            </form>
            <form id="deleteForm" action="${pageContext.request.contextPath}/delete" method="post" style="display: inline;">
                <input type="hidden" name="id" value="${id}">
                <input type="submit" class="delete" value="삭제" onclick="return confirm('글을 삭제하시겠습니까?')">
            </form>
        </div>
        <br>
        
        <!-- 댓글 출력 부분 -->
        <c:forEach var="comment" items="${comments}">
            <c:if test="${comment.commentLevel == 1}">
                <!-- 부모 댓글 -->
                <div class="comment" style="margin-left: 0px;">
                    <div class="comment-header">
                        <span class="comment-author"><c:out value="${comment.commentUserName}" /></span>
                        <span class="comment-date"><fmt:formatDate value="${comment.date}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                    </div>
                    <div class="comment-content">
                        <c:out value="${comment.content}" />
                    </div>
                    <div class="comment-actions">
                        <a href="#" onclick="showEditForm('${comment.commentUserName}', event)">수정</a>
                        <form id="deleteCommentForm-${comment.commentUserName}" action="${pageContext.request.contextPath}/comment/delete" method="post" style="display:inline;">
                            <input type="hidden" name="commentUserName" value="${comment.commentUserName}" />
                            <input type="hidden" name="boardId" value="${id}" />
                            <a href="#" onclick="deleteComment('${comment.commentUserName}', event); return false;">삭제</a>
                        </form>
                        <a href="#" onclick="showReplyForm('${comment.commentUserName}', event)">댓글의 댓글</a>
                    </div>
                    <br>

                    <!-- 댓글 수정 폼 -->
                    <form id="editCommentForm-${comment.commentUserName}" class="edit-comment-form" style="display:none;" method="post" action="${pageContext.request.contextPath}/comment/edit">
                        <input type="hidden" name="commentUserName" value="${comment.commentUserName}" />
                        <textarea name="commentContent" id="editCommentContent-${comment.commentUserName}">${comment.content}</textarea>
                        <button type="submit">저장</button>
                        <button type="button" onclick="cancelEdit('${comment.commentUserName}', event)">취소</button>
                    </form>

                    <!-- 댓글의 댓글 입력 폼 -->
                    <form id="replyCommentForm-${comment.commentUserName}" class="reply-comment-form" style="display:none;" method="post" action="${pageContext.request.contextPath}/comment/add">
                        <input type="hidden" name="parentCommentUserName" value="${comment.commentUserName}" />
                        <input type="hidden" name="boardId" value="${id}" />
                        <input type="text" name="commentUserName" id="replyCommentUserName-${comment.commentUserName}" placeholder="작성자 이름" />
                        <textarea name="commentContent" id="replyCommentContent-${comment.commentUserName}" placeholder="댓글 내용을 입력하세요."></textarea>
                        <button type="submit">댓글 작성</button>
                        <button type="button" onclick="cancelReply('${comment.commentUserName}', event)">취소</button>
                    </form>

                    <!-- 대댓글 출력 부분 -->
                    <c:forEach var="reply" items="${comments}">
                        <c:if test="${reply.commentLevel > 1 && reply.parentCommentUserName == comment.commentUserName}">
                            <div class="comment reply" style="margin-left: ${reply.commentLevel * 20}px;">
                                <div class="comment-header">
                                    <span class="comment-reply-prefix">ㄴ</span>
                                    <span class="comment-author"><c:out value="${reply.commentUserName}" /></span>
                                    <span class="comment-date"><fmt:formatDate value="${reply.date}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                </div>
                                <div class="comment-content">
                                    <c:out value="${reply.content}" />
                                </div>
                                <div class="comment-actions">
                                    <a href="#" onclick="showEditForm('${reply.commentUserName}', event)">수정</a>
                                    <form id="deleteCommentForm-${reply.commentUserName}" action="${pageContext.request.contextPath}/comment/delete" method="post" style="display:inline;">
                                        <input type="hidden" name="commentUserName" value="${reply.commentUserName}" />
                                        <input type="hidden" name="boardId" value="${id}" />
                                        <a href="#" onclick="deleteComment('${reply.commentUserName}', event); return false;">삭제</a>
                                    </form>
                                </div>

                                <!-- 대댓글 수정 폼 -->
                                <form id="editCommentForm-${reply.commentUserName}" class="edit-comment-form" style="display:none;" method="post" action="${pageContext.request.contextPath}/comment/edit">
                                    <input type="hidden" name="commentUserName" value="${reply.commentUserName}" />
                                    <textarea name="commentContent" id="editCommentContent-${reply.commentUserName}">${reply.content}</textarea>
                                    <button type="submit">저장</button>
                                    <button type="button" onclick="cancelEdit('${reply.commentUserName}', event)">취소</button>
                                </form>

                                <!-- 대댓글의 대댓글 입력 폼 -->
                                <form id="replyCommentForm-${reply.commentUserName}" class="reply-comment-form" style="display:none;" method="post" action="${pageContext.request.contextPath}/comment/add">
                                    <input type="hidden" name="parentCommentUserName" value="${reply.commentUserName}" />
                                    <input type="hidden" name="boardId" value="${id}" />
                                    <input type="text" name="commentUserName" id="replyCommentUserName-${reply.commentUserName}" placeholder="작성자 이름" />
                                    <textarea name="commentContent" id="replyCommentContent-${reply.commentUserName}" placeholder="댓글 내용을 입력하세요."></textarea>
                                    <button type="submit">댓글 작성</button>
                                    <button type="button" onclick="cancelReply('${reply.commentUserName}', event)">취소</button>
                                </form>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
            </c:if>
        </c:forEach>
        
        <!-- 댓글의 댓글 입력 폼 -->
        <form id="replyCommentForm-${comment.commentUserName}" class="reply-comment-form" style="display:none;" method="post" action="${pageContext.request.contextPath}/comment/add">
            <input type="hidden" name="parentCommentUserName" value="${comment.commentUserName}" />
            <input type="hidden" name="boardId" value="${id}" />
            <input type="text" name="commentUserName" id="replyCommentUserName-${comment.commentUserName}" placeholder="작성자 이름" />
            <textarea name="commentContent" id="replyCommentContent-${comment.commentUserName}" placeholder="댓글 내용을 입력하세요."></textarea>
            <button type="submit">댓글 작성</button>
            <button type="button" onclick="cancelReply('${comment.commentUserName}', event)">취소</button>
        </form>
                
            

        <!-- 페이지네이션 -->
        <div class="paging">
            <!-- 처음 페이지 그룹이 아닌 경우에만 << 와 < 표시 -->
            <c:if test="${pagination.cp > 1}">
                <a href="?commentPage=1&sizePerCommentPage=${pagination.pageSize}"><<</a>
                <a href="?commentPage=${pagination.cp - 1}&sizePerCommentPage=${pagination.pageSize}"><</a>
            </c:if>
        
            <!-- 페이지 번호 표시 -->
            <c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}">
                <a href="?commentPage=${i}&sizePerCommentPage=${pagination.pageSize}" class="${pagination.cp == i ? 'active' : ''}">
                    ${i}
                </a>
            </c:forEach>
        
            <!-- 마지막 페이지 그룹이 아닌 경우에만 > 와 >> 표시 -->
            <c:if test="${pagination.cp < pagination.totalPages}">
                <a href="?commentPage=${pagination.cp + 1}&sizePerCommentPage=${pagination.pageSize}">></a>
                <a href="?commentPage=${pagination.totalPages}&sizePerCommentPage=${pagination.pageSize}">>></a>
            </c:if>
        </div>

        <!-- 댓글 작성 폼 -->
        <div class="new-comment">
            <h3>새 댓글 작성</h3>
            <form id="commentForm" method="post" action="${pageContext.request.contextPath}/comment/add">
                <input type="hidden" name="boardId" value="${id}" />
                <input type="text" id="newCommentUserName" name="commentUserName" placeholder="작성자 이름" />
                <textarea id="newCommentContent" name="commentContent" placeholder="댓글 내용을 입력하세요."></textarea>
                <button type="submit" onclick="return submitComment()">댓글 작성</button>
            </form>
        </div>
    </div>
</body>
</html>
