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

    public void write(Board board) {
        // jpa @repository 자체에 save(@entity)라고 저장해주는 메서드 있음
        boardRepository.save(board);
    }

    public List<Board> boardList() {
        // jpa @repository 자체에 entity List를 반환해주는 findAll() 메서드 있음
        return boardRepository.findAll();
    }
}
