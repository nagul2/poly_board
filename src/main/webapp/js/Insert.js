function handleSubmit(event) {
    var boardUserName = document.getElementById("boardUserName").value;
    var title = document.getElementById("title").value;
    var content = document.getElementById("content").value;

    if (boardUserName.length > 20) {
        alert("작성자 명은 한글 20자까지 입력 가능합니다.");
        event.preventDefault();
        return false;
    }

    if (content.length > 500) {
        alert("내용은 500자까지 입력 가능합니다.");
        event.preventDefault();
        return false;
    }

    if (title.trim() === "" || boardUserName.trim() === "" || content.trim() === "") {
        alert("제목, 작성자, 내용을 모두 입력해 주세요.");
        event.preventDefault();
        return false;
    }

    if (confirm('글을 등록하시겠습니까?')) {
        alert('새글이 등록되었습니다.');
        document.getElementById('insertForm').submit();
    } else {
        event.preventDefault();
    }
}

function handleCancel() {
    window.location.href = 'Board_v3/list';
}
