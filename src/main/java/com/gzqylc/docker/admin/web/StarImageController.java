package com.gzqylc.docker.admin.web;

import com.gzqylc.docker.admin.entity.StarImage;
import com.gzqylc.docker.admin.service.StarImageService;
import com.gzqylc.framework.AjaxResult;
import com.gzqylc.framework.Route;
import com.gzqylc.lang.web.base.BaseController;
import com.gzqylc.lang.web.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/starImage")
public class StarImageController extends BaseController {

    @Autowired
    private StarImageService service;

    @Route("list")
    public Page<StarImage> list(@PageableDefault(sort = BaseEntity.Fields.modifyTime, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<StarImage> list = service.findAll(pageable);
        return list;
    }


    @RequestMapping("star")
    public AjaxResult star( String name) {
        service.star(name);

        return AjaxResult.success("收藏成功");
    }

    @RequestMapping("unstar")
    public AjaxResult unstar( String name) {
        service.unstar(name);

        return AjaxResult.success("收藏成功");
    }
}
