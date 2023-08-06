package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired  //-- DI: Spring Bean이 알아서 읽어서 주입해줌
    private BoardRepository boardRepository;

    // 글 작성 처리
    public void write(Board board) {
        // jpa @repository 자체에 save(@entity)라고 저장해주는 메서드 있음
        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    public List<Board> boardList() {
        // jpa @repository 자체에 entity List를 반환해주는 findAll() 메서드 있음
        return boardRepository.findAll();
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {
        // jpa @repository 자체에 Id로 지정한 걸로 entity 찾는 findById를 메서드 있음
        // findById()는 Optional로 값을 받아온다.
        // 뒤에 .get() 을 붙여주면 빨간 줄 사라진다.
        // └→ 값이 null일 경우 NoSuchElementException이 발생한다.
        return boardRepository.findById(id).get();
    }
}
