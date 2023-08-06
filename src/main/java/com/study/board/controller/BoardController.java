package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String boardWriteProcess(Board board, Model model) {
        boardService.write(board);
        //-- 나중에 이거 return 값으로 0,1 받아와서 분기 나눠서, 글 작성 완료/실패 처리할 수도 있음

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";    //-- message.html 만들고나서 수정함
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model) {
        model.addAttribute("list", boardService.boardList());
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
    public String boardUpdate(@PathVariable("id") Integer id, Board board) {
        // id로 기존 게시물을 찾아서, 수정된 내용으로 set한다.
        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        // 그 내용으로 다시 저장(덮어쓰기)
        boardService.write(board);

        return "redirect:/board/view?id=" + id;
    }
}
