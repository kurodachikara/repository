package jp.kuroda.sampleBlog.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.kuroda.sampleBlog.model.Blog;
import jp.kuroda.sampleBlog.model.Comment;
import jp.kuroda.sampleBlog.model.Person;
import jp.kuroda.sampleBlog.repository.BlogRepository;

@Service
public class BlogService {
	@Autowired
	private BlogRepository blogRepository;
	
	
	public List<Blog> getBlogList(){
		return blogRepository.findAll();
	}
	public List<Blog> getMyBlogs(Person person){
		return blogRepository.findByPerson(person);
	}
	public List<Blog> getBlogList(String word){
		return blogRepository.findByTitleContains(word);
	}
	
	public Blog createBlog(Blog blog){
		blog.setPostDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		if(!blog.getFile().isEmpty()) {
			try {
				MultipartFile file=blog.getFile();
				StringBuffer data = new StringBuffer();
			    String base64 = new String(Base64.encodeBase64(file.getBytes()),"ASCII"); //画像をbase64に置き換えて文字列化
			    data.append("data:image/jpeg;base64,");
			    data.append(base64);
			    blog.setBase64_str(data.toString());
			}catch(Exception e) {
			}
		}
		blogRepository.save(blog);
		return blog;
	}
	public void editBlog(Blog blog,Comment comment) {
		if(!blog.getFile().isEmpty()) {
			try {
				MultipartFile file=blog.getFile();
				StringBuffer data = new StringBuffer();
			    String base64 = new String(Base64.encodeBase64(file.getBytes()),"ASCII"); //画像をbase64に置き換えて文字列化
			    data.append("data:image/jpeg;base64,");
			    data.append(base64);
			    blog.setBase64_str(data.toString());
			}catch(Exception e) {
			}
		}
		blog.setPostDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		blogRepository.save(blog);
	}
	public void deleteBlog(Blog blog) {
		blogRepository.deleteById(blog.getId());
	}
	public void deleteImage(Blog blog) {
		blog.setFile(null);
		blog.setImage_byte(null);
		blog.setBase64_str(null);
		blogRepository.save(blog);
	}
}
