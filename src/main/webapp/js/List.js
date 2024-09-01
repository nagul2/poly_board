document.addEventListener('DOMContentLoaded', function() {
    const rows = document.querySelectorAll('table.board tbody tr');
    
    rows.forEach(row => {
        const viewForm = row.querySelector('form');
        const titleCell = row.querySelector('td:nth-child(2)');
        
        // 제목 칸 클릭 시 글 보기로 이동
        titleCell.addEventListener('click', function() {
            viewForm.submit();
        });

        // 제목 칸에만 hover 시 pointer 커서 표시
        titleCell.style.cursor = 'pointer';
    });
});
