function deleteComment(commentUserName, event) {
    event.preventDefault();
    if (confirm('이 댓글을 삭제하시겠습니까? 해당 댓글의 대댓글도 모두 삭제됩니다.')) {
        const form = document.createElement('form');
        form.method = 'post';
		form.action = `${document.location.origin}/Board_v3/comment/delete`;
        
        const input1 = document.createElement('input');
        input1.type = 'hidden';
        input1.name = 'commentUserName';
        input1.value = commentUserName;

        const input2 = document.createElement('input');
        input2.type = 'hidden';
        input2.name = 'boardId';
        input2.value = document.getElementById('boardId').value;

        form.appendChild(input1);
        form.appendChild(input2);
        document.body.appendChild(form);
        form.submit();
    }
}

function toggleForm(formId, event) {
    event.preventDefault();
    
    // 현재 열려있는 폼이 있으면 닫기
    const openForms = document.querySelectorAll('.edit-comment-form, .reply-comment-form');
    openForms.forEach(form => {
        if (form.id !== formId) {
            form.style.display = 'none';
        }
    });

    // 해당 폼 열기/닫기 토글
    const form = document.getElementById(formId);
    if (form.style.display === 'block') {
        form.style.display = 'none';
    } else {
        form.style.display = 'block';
    }
}

function showEditForm(commentId, event) {
    toggleForm('editCommentForm-' + commentId, event);
}

function showReplyForm(commentId, event) {
    toggleForm('replyCommentForm-' + commentId, event);
}

function cancelEdit(commentId, event) {
    event.preventDefault();
    document.getElementById('editCommentForm-' + commentId).style.display = 'none';
}

function cancelReply(commentId, event) {
    event.preventDefault();
    document.getElementById('replyCommentForm-' + commentId).style.display = 'none';
}

function submitComment() {
    const commentUserName = document.getElementById("newCommentUserName").value;
    const content = document.getElementById("newCommentContent").value;
    if (commentUserName.length > 20) {
        alert("작성자 명은 한글 20자까지 입력 가능합니다.");
        return false;
    }
    if (content.length > 200) {
        alert("댓글 내용은 200자까지 입력 가능합니다.");
        return false;
    }
    if (commentUserName.trim() === "") {
        alert("작성자 명은 필수 항목입니다.");
        return false;
    }
    document.getElementById("commentForm").submit();
}

function saveScrollPosition() {
    localStorage.setItem('scrollPosition', window.scrollY);
}

function restoreScrollPosition() {
    const scrollPosition = localStorage.getItem('scrollPosition');
    if (scrollPosition) {
        window.scrollTo(0, parseInt(scrollPosition));
    }
    localStorage.removeItem('scrollPosition');
}

document.addEventListener("DOMContentLoaded", function() {
    restoreScrollPosition();

    const buttons = document.querySelectorAll('.comment-actions a, .new-comment button');
    buttons.forEach(button => {
        button.addEventListener('click', saveScrollPosition);
    });
});
