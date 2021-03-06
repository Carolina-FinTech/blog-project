package com.team3.blogproject.service;

import com.team3.blogproject.model.Post;
import com.team3.blogproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public void savePost(Post post) {
        this.postRepository.save(post);
    }

    @Override
    public Post getPostById(long id) {
        Optional<Post> optional = postRepository.findById(id);
        Post post = null;
        if (optional.isPresent()) {
            post = optional.get();
        } else {
            throw new RuntimeException("Post not found with ID ::" + id);
        }
        return post;
    }

    @Override
    public void deletePostById(long id) {
        this.postRepository.deleteById(id);
    }
    
    @Override
    public Page<Post> findPaginated(int pageNum, int pageSize) {
        Sort sort = Sort.by("id").descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize).withSort(sort);
        return this.postRepository.findAll(pageable);
    }

    @Override
    public List<Post> findLatest5() {
        return this.postRepository
                .findAll( PageRequest.of(0, 5,Sort.Direction.DESC,"id") ).stream()
                .sorted( (a,b) -> b.getDate().compareTo(a.getDate()) )
                .collect(Collectors.toList());
    }
}