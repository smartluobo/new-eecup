package com.ibay.tea.common.utils;

import com.github.pagehelper.PageInfo;
import com.ibay.tea.common.PageResultInfo;

import java.io.Serializable;
import java.util.List;

public class PageUtil implements Serializable {

    public static PageResultInfo getData(PageInfo<?> pageInfo) {
        return new PageResultInfo(pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize(),
                pageInfo.getList(), pageInfo.isHasPreviousPage(), pageInfo.isHasNextPage());
    }

    /**
     * manual  build PageInfo
     *
     * @param list
     * @param pageNum
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> PageInfo buildPageInfo(List<T> list, int pageNum, int pageSize) {
        PageInfo pageInfo = new PageInfo<>();
        int size = list.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = pageNum * pageSize;
        if (fromIndex >= size) {
            fromIndex = size;
        }
        if (toIndex >= size) {
            toIndex = size;
        }
        int pages = 0;
        if (pageSize > 0) {
            pages = size / pageSize + ((size % pageSize == 0) ? 0 : 1);
        }
        boolean hasNextPage = pageNum < pages;
        boolean hasPreviousPage = pageNum > 1;
        pageInfo.setHasNextPage(hasNextPage);
        pageInfo.setHasPreviousPage(hasPreviousPage);
        pageInfo.setTotal(list.size());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        list = list.subList(fromIndex, toIndex);
        pageInfo.setList(list);
        return pageInfo;
    }
}
