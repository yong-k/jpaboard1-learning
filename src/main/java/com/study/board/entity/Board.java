package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Board테이블에 관한 Entity구나 하고, JPA가 읽어들인다.
@Entity
@Data   //--  @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode을 한꺼번에 설정
public class Board {

    @Id // primary key를 의미한다.
    // ┌→ 전략을 어떻게 할건지
    @GeneratedValue(strategy = GenerationType.IDENTITY) //-- MySQL, MariaDB에서 쓰는거
    //@GeneratedValue(strategy = GenerationType.SEQUENCE) //-- Oracle에서 쓰는거
    //@GeneratedValue(strategy = GenerationType.AUTO)     //-- 알아서 지정해주는거
    private Integer id;
    private String title;
    private String content;
}
