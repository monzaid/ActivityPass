package com.example.social_practice_activity_server.pojo;

import java.util.List;

/**
 * Page是分页的模型对象
 * @param <T> 是具体的模块的javaBean类
 */
public class Page<T> {
    public static final int PAGE_SIZE = 10;
    // 当前页码
    private int pageNo;
    // 总页码
    private int pageTotal;
    // 当前页显示数量
    private int pageSize = PAGE_SIZE;
    // 总记录数
    private int pageTotalCount;
    // 当前页数据
    private List<T> items;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        /* 数据边界的有效检查 */
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pageTotal) {
            pageNo = pageTotal;
        }

        this.pageNo = pageNo;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public void setPageTotalByPageTotalCount(){
        this.pageTotal = (pageTotalCount + pageSize - 1) / pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotalCount() {
        return pageTotalCount;
    }

    public void setPageTotalCount(int pageTotalCount) {
        this.pageTotalCount = pageTotalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNo=" + pageNo +
                ", pageTotal=" + pageTotal +
                ", pageSize=" + pageSize +
                ", pageTotalCount=" + pageTotalCount +
                ", items=" + items +
                '}';
    }
}
