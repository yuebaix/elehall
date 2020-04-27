package com.geercode.elehall.web.common.util;

import com.geercode.elehall.web.common.base.BaseMapStruct;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>Description : 分页帮助类</p>
 * <p>Created on  : 2019-10-12 17:10:50</p>
 *
 * @author jerryniu
 */
public class PageDtoHelper {
    private PageDtoHelper() {}

    public static <O, I> PageDto<O> toPage(Page<I> entityPage, BaseMapStruct<O, I> mapper) {
        List<I> entityList = entityPage.getContent();
        List<O> dtoList = mapper.toOuter(entityList);
        PageDto<O> dtoPage = new PageDto<O>().setPage(entityPage.getNumber() + 1)
                .setSize(entityPage.getSize())
                .setTotolPage(entityPage.getTotalPages())
                .setTotolItem(entityPage.getTotalElements())
                .setData(dtoList);
        return dtoPage;
    }

    public static PageDto<Map<String, Object>> toPage(List<Map<String, Object>> mapList, int page, int size, long count) {
        PageDto<Map<String, Object>> dtoPage = new PageDto<Map<String, Object>>().setPage(page + 1)
                .setSize(size)
                .setTotolPage(count/size + 1)
                .setTotolItem(count)
                .setData(mapList);
        return dtoPage;
    }
}
