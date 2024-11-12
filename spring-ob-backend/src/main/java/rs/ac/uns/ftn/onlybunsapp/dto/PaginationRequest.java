package rs.ac.uns.ftn.onlybunsapp.dto;

public class PaginationRequest {
    private int page;
    private int size;
    private String firstName;
    private String lastName;
    private String email;
    private Integer minPosts;
    private Integer maxPosts;
    private String sortBy;
    private String sortDirection;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMinPosts() {
        return minPosts;
    }

    public void setMinPosts(Integer minPosts) {
        this.minPosts = minPosts;
    }

    public Integer getMaxPosts() {
        return maxPosts;
    }

    public void setMaxPosts(Integer maxPosts) {
        this.maxPosts = maxPosts;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
