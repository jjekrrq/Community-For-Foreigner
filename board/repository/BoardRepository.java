package com.example.project.board.repository;

import com.example.project.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository  extends JpaRepository<Board, Long> {
}