package com.lecture.kukeboard.comment.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class CommentPath {

    private String path;

    private static final String CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int DEPTH_CHUNK_SIZE = 5;
    private static final int MAX_DEPTH = 5;

    private static final String MIN_CHUNK = String.valueOf(CHARSET.charAt(0)).repeat(DEPTH_CHUNK_SIZE);
    private static final String MAX_CHUNK = String.valueOf(CHARSET.charAt(CHARSET.length() - 1)).repeat(DEPTH_CHUNK_SIZE);

    protected CommentPath() {
    }

    public static CommentPath create(String path) {
        if (isChunkOverflow(path)) {
            throw new IllegalArgumentException("depth overflow");
        }
        CommentPath commentPath = new CommentPath();
        commentPath.path = path;
        return commentPath;
    }

    private static boolean isChunkOverflow(String path) {
        return calDepth(path) > MAX_DEPTH;
    }

    private static int calDepth(String path) {
        return path.length() / DEPTH_CHUNK_SIZE;
    }

    public int getDepth() {
        return calDepth(path);
    }

    public boolean isRoot() {
        return calDepth(path) == 1;
    }

    public String getParentPath() {
        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE);
    }

    public CommentPath createChildCommentPath(String descendantTopPath) {
        if (descendantTopPath == null) {
            return CommentPath.create(path + MIN_CHUNK);
        }

        String childrenTopPath = findChildrenTopPath(descendantTopPath);
        return CommentPath.create(childrenTopPath);
    }

    private String findChildrenTopPath(String descendantTopPath) {
        return descendantTopPath.substring(0, (getDepth() + 1) * DEPTH_CHUNK_SIZE);
    }
    
    private String increase(String path) {
        String lastChunk = path.substring(path.length() - DEPTH_CHUNK_SIZE);
        if (isChunkOverflow(lastChunk)) {
            throw new IllegalStateException("chunk overflowed");
        }

        int charsetLength = CHARSET.length();
        int value = 0;
        for (char ch : lastChunk.toCharArray()) {
            value = value * charsetLength + CHARSET.indexOf(ch);
        }

        value = value + 1;
        String result = "";

        for (int i = 0; i < DEPTH_CHUNK_SIZE; i++) {
            result = CHARSET.charAt(value % CHARSET.length()) + result;
            value /= CHARSET.length();
        }

        return path.substring(0, path.length() - DEPTH_CHUNK_SIZE) + result;
    }

    public String getPath() {
        return path;
    }
}
