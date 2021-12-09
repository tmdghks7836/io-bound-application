package class101.foo.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    Producer producer;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostCacheService postCacheService;

    // 1. 글을 작성한다.
    @PostMapping("/post")
    public Post createPost(@RequestBody Post post) throws JsonProcessingException {

        String jsonPost = objectMapper.writeValueAsString(post);
        producer.sendTo(jsonPost);

        return post;
        // return postRepository.save(post);
    }

    // 2-1. 글 목록을 조회한다.
    @GetMapping("/posts")
    public Page<Post> getPostList(@PageableDefault(size = 20) Pageable pageable) {

        if (pageable.getPageNumber() == 0) {
            return postCacheService.getFirstPostPage();
        }

        return postRepository.findAll(pageable);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id) {

        Post post = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("존재하지 않는 포스트입니다."));

        return post;
    }

    // 2-2 글 목록을 페이징하여 반환

    // 3. 글 번호로 조회

    // 4. 글 내용으로 검색 -> 해당 내용이 포함된 모든 글

}
