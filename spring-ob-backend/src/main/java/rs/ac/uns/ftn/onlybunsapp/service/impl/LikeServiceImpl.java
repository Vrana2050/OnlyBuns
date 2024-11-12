package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.model.PostUserLike;
import rs.ac.uns.ftn.onlybunsapp.repository.LikeRepository;
import rs.ac.uns.ftn.onlybunsapp.service.LikeService;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public boolean save(long userId, long postId) {
        if(HasLiked(userId,postId))
            return false;
        likeRepository.save(new PostUserLike(userId,postId));
        return true;
    }

    @Override
    public boolean delete(long userId, long postId) {
        PostUserLike postUserLike =  likeRepository.findByPostIdAndUserId(userId,postId);
        if(postUserLike == null)
            return false;
        likeRepository.delete(postUserLike);
        return true;
    }

    private Boolean HasLiked(long userId, long postId) {
        return likeRepository.findByPostIdAndUserId(userId,postId)!=null;
    }


}
