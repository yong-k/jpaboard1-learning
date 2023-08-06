package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    // JpaRepository를 상속받고,
    // <1)어떤 entity에 대한 것이고,
    //  2)해당 entity의 @Id(PK로 지정한)지정한 것의 타입>

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
