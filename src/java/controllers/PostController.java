/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.List;
import models.Post;
import models.User;
import utilities.DatabaseUtilities;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author c0641903
 */
@ManagedBean
@ApplicationScoped
public class PostController {
    private List<Post> posts;
    private Post post;
    
    public PostController() {
        posts = new ArrayList<>();
        post = new Post();
        retrievePosts();
    }
    public PostController(List posts, Post post) {
        this.posts = posts;
        this.post = post;
    }

    public List<Post> getPosts() {
        return posts;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
    
    public Post getPostById(int id) {
        for (Post selectedPost : posts) {
            if (selectedPost.getId() == id) {
                return selectedPost;
            }
        }
        return null;
    }
    public Post getPostByTitle(String title) {
        for (Post selectedPost : posts) {
            if (selectedPost.getTitle().equals(title)) {
                return selectedPost;
            }
        }
        return null;
    }
    
    public String viewPost(Post post) {
        this.post = post;
        return "viewPost";
    }
    public String addPost() {
        post = new Post();
        return "editPost";
    }
    public String editPost() {
        return "editPost";
    }
    public String cancelPost() {
        int id = post.getId();
        retrievePosts();
        post = getPostById(id);
        return "index";
    }
    public String savePost(User user) {
        try (Connection connection = DatabaseUtilities.connect()) {
            if (post.getId() >= 0) {
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE posts "
                                + "SET title = ?, contents = ? "
                                + "WHERE id = ?"
                );
                statement.setString(1, post.getTitle());
                statement.setString(2, post.getContents());
                statement.setInt(3, post.getId());
                statement.executeUpdate();
            } else {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO posts("
                                + "user_id, "
                                + "title, "
                                + "created_time, "
                                + "contents"
                        + ") VALUES("
                                + "?, "
                                + "?, "
                                + "NOW(), "
                                + "?"
                        + ")"
                );
                statement.setInt(1, user.getId());
                statement.setString(2, post.getTitle());
                statement.setString(3, post.getContents());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
        retrievePosts();
        post = getPostByTitle(post.getTitle());
        return "viewPost";
    }
    
    public String deletePost() {
        try (Connection connection = DatabaseUtilities.connect()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM posts WHERE id = ?"
            );
            statement.setInt(1, post.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
        retrievePosts();
        return "index";
    }
    
    private void retrievePosts() {
        try (Connection connection = DatabaseUtilities.connect()) {
            posts = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM posts");
            while (resultSet.next()) {
                Post newPost = new Post(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("title"),
                        resultSet.getDate("created_time"),
                        resultSet.getString("contents")
                );
                posts.add(newPost);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
            posts = new ArrayList<>();
        }
    }
}