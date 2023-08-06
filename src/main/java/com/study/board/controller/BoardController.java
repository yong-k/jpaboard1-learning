package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write") //localhost:8090/board/write
    public String boardWriteForm() {
        return "boardwrite";
    }

    @PostMapping("/board/writeProcess")
    //public String boardWriteProcess(String title, String content) {   //-- Board Entity 만들기 전
    public String boardWriteProcess(Board board, Model model, MultipartFile file) throws Exception {

        boardService.write(board, file);
        //-- 나중에 이거 return 값으로 0,1 받아와서 분기 나눠서, 글 작성 완료/실패 처리할 수도 있음

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";    //-- message.html 만들고나서 수정함
        return "message";
    }

    // 페이징처리를 위해 org.springframework.data.domain의 Pageable 인터페이스 사용한다.
    // @PageableDefault 어노테이션을 사용하여 여러가지 설정해줄 수 있다.
    // page: default 페이지 수
    // size: 한 페이지 게시글 수
    // sort: 정렬 기준 컬럼
    // direction: 정렬 순서 (Sort.Direction.ASC / Sort.Direction.DESC)
    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page=0,size=10,sort="id",direction=Sort.Direction.DESC) Pageable pageable) {

        Page<Board> list = boardService.boardList(pageable);

        // ┌→ pageable에서 넘어온 현재 페이지 번호를 가져온다.(pageable의 페이지는 0에서 시작하기 때문에 1 더해준다.)
        int nowPage = list.getPageable().getPageNumber() + 1;       // 현재 페이지
        int startPage = Math.max(nowPage - 4, 1);                   // 블럭에서 보여줄 시작 페이지
        int endPage = Math.min(nowPage + 5, list.getTotalPages());  // 블럭에서 보여줄 마지막 페이지

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }

    @GetMapping("/board/view")  // localhost:8090/board/view?id=1  하면, 1이 매개변수 id로 들어감
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);

        // 게시물 삭제하고 리스트 페이지로 리다이렉트 시킨다.
        return "redirect:/board/list";
    }

    // pathVariable 사용해보자
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    // ★★ boardwrite.html에서 input태그 name="file"과 MultipartFile 변수 이름 file이 일치해야 받아와진다.
    public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws Exception {
        // id로 기존 게시물을 찾아서, 수정된 내용으로 set한다.
        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        // 그 내용으로 다시 저장(덮어쓰기)
        boardService.write(board, file);

        return "redirect:/board/view?id=" + id;
    }
}
