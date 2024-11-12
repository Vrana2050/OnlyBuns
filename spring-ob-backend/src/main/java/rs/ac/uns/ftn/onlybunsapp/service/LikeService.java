package rs.ac.uns.ftn.onlybunsapp.service;

public interface LikeService{
    public boolean save(long userId,long postId);
    public boolean delete(long userId,long postId);
}
