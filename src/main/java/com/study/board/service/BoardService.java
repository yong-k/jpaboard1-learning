package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired  //-- DI: Spring Bean이 알아서 읽어서 주입해줌
    private BoardRepository boardRepository;

    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception {
        // 현재 프로젝트 경로를 담는다. + 파일 저장할 디렉토리 파일 경로 추가
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        // 파일이름에 붙일 랜덤이름을 생성해준다.
        UUID uuid = UUID.randomUUID();

        // '랜덤이름_파일이름'으로 파일이름을 생성해준다.
        String fileName = uuid + "_" + file.getOriginalFilename();

        // "파일을 생성할건데, projectPath에 넣을거고, 이름은 fileName으로 담긴다"는 의미이다.
        File saveFile = new File(projectPath, fileName);

        // transferTo() 는 IOException에 대처하라는 경고문구가 뜨니까 예외 처리해줘야 한다.
        //--→ 여기서는 throws Exception으로 처리
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        // jpa @repository 자체에 save(@entity)라고 저장해주는 메서드 있음
        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    //public List<Board> boardList(Pageable pageable) {     // --Pageable 추가해준 후에 수정
    public Page<Board> boardList(Pageable pageable) {
        // jpa @repository 자체에 entity List를 반환해주는 findAll() 메서드 있음
        return boardRepository.findAll(pageable);
        //--→ findAll() 매개변수가 없을 경우에는 return 값이 List로 넘어오는데,
        //    이 경우에는 Page라는 클래스로 return하므로 return 타입을 Page로 수정한다.
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {
        // jpa @repository 자체에 Id로 지정한 걸로 entity 찾는 findById() 메서드 있음
        // findById()는 Optional로 값을 받아온다.
        // 뒤에 .get() 을 붙여주면 빨간 줄 사라진다.
        // └→ 값이 null일 경우 NoSuchElementException이 발생한다.
        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        // jpa @repository 자체에 Id로 지정한 걸로 entity 삭제하는 deleteById() 메서드 있음
        boardRepository.deleteById(id);
    }
}
