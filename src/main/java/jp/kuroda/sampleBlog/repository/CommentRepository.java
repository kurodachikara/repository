package jp.kuroda.sampleBlog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.kuroda.sampleBlog.model.Blog;
import jp.kuroda.sampleBlog.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
}
