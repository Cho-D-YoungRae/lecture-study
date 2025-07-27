package com.lecture.kukeboard.article.service;

public final class PageLimitCalculator {

    public static Long calculatePageLimit(Long page, Long pageSize, Long movablePageCount) {
        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;
    }

    private PageLimitCalculator() {
    }
}
