package com.ibay.tea.common;

import lombok.Data;

/**
 * 分页返回工具类
 */
@Data
public class PageResultInfo {
    /**
     * 总记录条数
     */
    private long total;
    /**
     * 当前页数
     */
    private long pageNum;
    /**
     * 当前页大小
     */
    private long pageSize;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 是否有前页
     */
    private boolean hasPreviousPage;
    /**
     * 是否有后页
     */
    private boolean hasNextPage;


    public PageResultInfo(long total, long pageNum, long pageSize,
                          Object data, boolean hasPreviousPage, boolean hasNextPage){
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }
}
